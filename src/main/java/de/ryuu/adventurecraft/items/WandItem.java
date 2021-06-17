package de.ryuu.adventurecraft.items;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.network.packets.PlayerNBTDataMergePacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;

public class WandItem extends ACItem implements ACITriggerableItem {

    public static final void applyWand(EntityPlayer player, BlockPos pos) {
        NBTTagCompound compound = player.getEntityData();
        NBTTagCompound acWand = null;

        if (!compound.hasKey("acWand")) {
            acWand = new NBTTagCompound();
            compound.setTag("acWand", acWand);
        } else {
            acWand = compound.getCompoundTag("acWand");
        }

        {
            // Double Call Prevention Hack
            long timeNow = player.world.getTotalWorldTime();
            long timePre = acWand.getLong("DCPH");

            if (timeNow == timePre) {
                return;
            } else {
                acWand.setLong("DCPH", timeNow);
            }
        }

        int[] pos$$ = new int[]{pos.getX(), pos.getY(), pos.getZ()};

        acWand.setIntArray("cursor", Arrays.copyOf(pos$$, 3));

        if (!acWand.hasKey("boundsA") || !acWand.getBoolean("enabled")) {
            acWand.setIntArray("boundsA", pos$$);
            acWand.setIntArray("boundsB", pos$$);
            acWand.setBoolean("enabled", true);
        } else {
            boolean flip = acWand.getBoolean("flip");
            if (flip) {
                acWand.setIntArray("boundsB", pos$$);
            } else {
                acWand.setIntArray("boundsA", pos$$);
            }
            acWand.setBoolean("flip", !flip);
        }

        AdventureCraft.network.sendTo(new PlayerNBTDataMergePacket(compound), (EntityPlayerMP) player);
    }

    public static final int[] getBoundsFromPlayerOrNull(EntityPlayer player) {
        return getBoundsFromPlayerDataOrNull(player.getEntityData());
    }

    public static final int[] getBoundsFromPlayerDataOrNull(NBTTagCompound playerData) {
        if (playerData.hasKey("acWand"))
            return getBoundsFromAcWandOrNull(playerData.getCompoundTag("acWand"));
        return null;
    }

    public static final int[] getBoundsFromAcWandOrNull(NBTTagCompound acWand) {
        if (!acWand.hasKey("boundsA") || !acWand.hasKey("boundsB")) {
            return null;
        }

        int[] a = acWand.getIntArray("boundsA");
        int[] b = acWand.getIntArray("boundsB");

        int ix = Math.min(a[0], b[0]);
        int iy = Math.min(a[1], b[1]);
        int iz = Math.min(a[2], b[2]);
        int ax = Math.max(a[0], b[0]);
        int ay = Math.max(a[1], b[1]);
        int az = Math.max(a[2], b[2]);

        return new int[]{ix, iy, iz, ax, ay, az};
    }

    public static final void setBounds(EntityPlayer player, int ix, int iy, int iz, int ax, int ay, int az) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound wandData = playerData.getCompoundTag("acWand");

        int _ix = Math.min(ix, ax);
        int _iy = Math.min(iy, ay);
        int _iz = Math.min(iz, az);
        int _ax = Math.max(ix, ax);
        int _ay = Math.max(iy, ay);
        int _az = Math.max(iz, az);

        if (_iy < 0)
            _iy = 0;
        if (_ay > 255)
            _ay = 255;

        wandData.setIntArray("boundsA", new int[]{_ix, _iy, _iz});
        wandData.setIntArray("boundsB", new int[]{_ax, _ay, _az});

        AdventureCraft.network.sendTo(new PlayerNBTDataMergePacket(playerData), (EntityPlayerMP) player);
    }

    public static long getBoundsVolume(int[] bounds) {
        int x = Math.abs(bounds[3] - bounds[0]);
        int y = Math.abs(bounds[4] - bounds[1]);
        int z = Math.abs(bounds[5] - bounds[2]);
        return x * y * z;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side,
                                      float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return EnumActionResult.PASS;

        applyWand(player, pos);

        return EnumActionResult.PASS;
    }

    @Override // Clears the wand selection
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return ActionResult.newResult(EnumActionResult.PASS, stack);

        { // do far-away raytrace...
            float lerp = 1f;
            float dist = 256;

            Vec3d start = getPositionEyes(lerp, player);
            Vec3d direction = player.getLook(lerp);
            Vec3d end = start.addVector(direction.x * dist, direction.y * dist, direction.z * dist);

            RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

            if (result.getBlockPos() != null) {
                applyWand(player, result.getBlockPos());
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Override
    // Warning: Forge Method
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        // Check if we are on the server-side.
        if (!player.world.isRemote) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            String cmd = "/ac_editentity " + entity.getUniqueID().toString();
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(playerMP, cmd);
        }

        // by returning TRUE, we prevent damaging the entity being hit.
        return true;
    }

    @Override
    public void trigger(World world, EntityPlayerMP player, ItemStack stack) {
        NBTTagCompound compound = player.getEntityData();
        compound.getCompoundTag("acWand").setBoolean("enabled", false);
        compound.getCompoundTag("acWand").setIntArray("boundsA", new int[]{0, 0, 0});
        compound.getCompoundTag("acWand").setIntArray("boundsB", new int[]{0, 0, 0});
        player.sendMessage(new TextComponentString("Wand selection has been cleared!"));
        AdventureCraft.network.sendTo(new PlayerNBTDataMergePacket(compound), player);
    }

}