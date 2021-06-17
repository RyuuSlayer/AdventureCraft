package de.ryuu.adventurecraft.entity.projectile;

import de.ryuu.adventurecraft.client.entity.RenderBoomerang;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.util.ArrayList;
import java.util.List;

public class EntityBoomerang extends EntityThrowable {

    public EntityPlayerSP player = Minecraft.getMinecraft().player;
    public EntityItem carryitem = null;
    public List<Entity> items = new ArrayList<>();
    public boolean changedLever = false;
    int i = 0;
    private int rotation = 0;
    private ItemStack stack;
    private boolean isReturning = false;


    public EntityBoomerang(World world) {
        super(world);
    }


    public EntityBoomerang(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityBoomerang(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }


    @Override
    protected void onImpact(RayTraceResult result) {
        if (world.isRemote) {
            return;
        }

        if (result.typeOfHit == Type.ENTITY) {
            Entity ent = result.entityHit;
            if (ent == getThrower()) {
                setDead();
            } else {
                ent.attackEntityFrom(DamageSource.causeIndirectDamage(this, getThrower()), 4F);
            }
        } else if (this.world.getBlockState(result.getBlockPos()).getBlock() == Blocks.LEVER) {
            if (!changedLever) {
                IBlockState leverstate = this.world.getBlockState(result.getBlockPos());
                leverstate = leverstate.cycleProperty(BlockLever.POWERED);
                this.world.setBlockState(result.getBlockPos(), leverstate);
                this.world.playSound((EntityPlayer) null, result.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0F);
                this.world.notifyNeighborsOfStateChange(result.getBlockPos(), leverstate.getBlock(), false);
                EnumFacing enumfacing = ((BlockLever.EnumOrientation) leverstate.getValue(BlockLever.FACING)).getFacing();
                this.world.notifyNeighborsOfStateChange(result.getBlockPos().offset(enumfacing.getOpposite()), leverstate.getBlock(), false);
                System.out.println("lever! @ " + result.getBlockPos());
                System.out.println("lever check @ " + leverstate.getValue(BlockLever.POWERED));
                changedLever = true;
            }

        } else {
            if (isReturning) {
                setDead();
            } else {
                returnToThrower();
            }
        }
    }

    @Override
    public void onUpdate() {
        rotation += 30;
        if (rotation > 360) {
            rotation = 0;
        }

//		creates a new boomerang hitbox for better item collecting
        AxisAlignedBB newAABB = this.getEntityBoundingBox().grow(0.5);
//		detects items in the hitbox, then adds them to a list to allow pickung up multiple items
        if (this.world.findNearestEntityWithinAABB(EntityItem.class, newAABB, this) != null) {
            Entity item = this.world.findNearestEntityWithinAABB(EntityItem.class, newAABB, this);
            item.setNoGravity(true);
            items.add(item);
        }
//		applies the attributes to the item that allow the carry effect
        if (!items.isEmpty()) {
            for (Entity item : items) {
                item.posX = this.posX + this.motionX;
                item.motionX = this.motionX;
                item.posY = this.posY;
                item.motionY = this.motionY;
                item.posZ = this.posZ + this.motionZ;
                item.motionZ = this.motionZ;
            }
        }


        if (ticksExisted > 20) {
            ticksExisted = 0;
            if (isReturning) {
                if (i < 0) {
                    i++;
                    onUpdate();
                } else {
                    setDead();

//					sets item gravity, so item does not float off if boomerang misses
                    if (!items.isEmpty()) {
                        for (Entity item : items) {
                            if (item.hasNoGravity()) {
                                item.setNoGravity(true);
                            }
                        }
                    }
                }

            } else {
                returnToThrower();

            }
        }
        super.onUpdate();
    }

    private void returnToThrower() {
        isReturning = true;
//		calculating the distance to use as a motion vector
        double dx = Math.abs(Math.abs(player.posX) - Math.abs(this.posX));
        double dy = Math.abs(Math.abs((player.posY + (double) player.eyeHeight) - 0.3) - Math.abs(this.posY));
        double dz = Math.abs(Math.abs(player.posZ) - Math.abs(this.posZ));
//		variables that determine the direction of the motion
        int xm = 0;
        int zm = 0;
        int ym = 0;

        if (this.motionX < 0) {
            xm = 1;
        } else {
            xm = -1;
        }
        if (this.motionZ < 0) {
            zm = 1;
        } else {
            zm = -1;
        }
        if (this.motionY < 0) {
            ym = 1;
        } else {
            ym = -1;
        }
//		final calculation to apply the new motion
        this.motionX = ((dx * (double) xm * 0.08) + player.motionX * 2);
        this.motionY = (dy * (double) ym * 0.08);
        this.motionZ = ((dz * (double) zm * 0.08) + player.motionZ * 2);
//		if (!items.isEmpty()) {
//			for (Entity item : items) {
//				item.posX = this.posX;
//				item.motionX = this.motionX;
//				item.posY = this.posY+0.5;
//				item.motionY = this.motionY;
//				item.posZ = this.posZ;
//				item.motionZ = this.motionZ;
//			}
//		}
    }

    @Override
    public void setDead() {
        super.setDead();
        if (getThrower() != null) {
            stack.getTagCompound().setBoolean("thrown", false);
        }
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

    }

    @SuppressWarnings("rawtypes")
    public static class EntityBoomerangFactory implements IRenderFactory {
        @Override
        public Render createRenderFor(RenderManager manager) {
            return new RenderBoomerang(manager);
        }
    }

}