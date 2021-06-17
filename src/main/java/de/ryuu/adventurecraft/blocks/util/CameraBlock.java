package de.ryuu.adventurecraft.blocks.util;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.blocks.ACBlockContainer;
import de.ryuu.adventurecraft.blocks.ACITriggerableBlock;
import de.ryuu.adventurecraft.client.gui.blocks.GuiCameraBlock;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.tileentity.CameraBlockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class CameraBlock extends ACBlockContainer implements ACITriggerableBlock {

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CameraBlockTileEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (!worldIn.isRemote) {
            if (heldItem != null && heldItem.getItem() == AdventureCraftItems.camera) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setLong("selected", pos.toLong());
                heldItem.setTagCompound(tag);
                playerIn.sendMessage(new TextComponentString("Selected camera block!"));
            }
            return true;
        }
        if (!AdventureCraft.proxy.isBuildMode())
            return false;
        if (playerIn.isSneaking())
            return true;

        if (heldItem == null || heldItem.getItem() != AdventureCraftItems.camera) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new GuiCameraBlock((CameraBlockTileEntity) worldIn.getTileEntity(pos)));
        }
        return true;
    }

    @Override
    public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
        if (world.isRemote)
            return;

        TileEntity tileentity = world.getTileEntity(position);

        if (tileentity instanceof CameraBlockTileEntity) {
            ((CameraBlockTileEntity) tileentity).trigger(triggerState);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("Currently a W.I.P.");
        tooltip.add("More features will come soon");
    }

}