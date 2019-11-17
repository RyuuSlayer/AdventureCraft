package de.ryuum3gum1n.adventurecraft.entity.projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.util.vector.Quaternion;

import de.ryuum3gum1n.adventurecraft.client.entity.RenderBoomerang;

public class EntityBoomerang extends EntityThrowable {

	private int rotation = 0;
	private ItemStack stack;
	private boolean isReturning = false;
	public EntityPlayerSP player = Minecraft.getMinecraft().player;
	int i = 0;

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
		if (world.isRemote)
			return;
		if (result.typeOfHit == Type.ENTITY) {
			Entity ent = result.entityHit;
			if (ent == getThrower()) {
				setDead();
			} else {
				ent.attackEntityFrom(DamageSource.causeIndirectDamage(this, getThrower()), 4F);
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
		if (ticksExisted > 20) {
			ticksExisted = 0;
			if (isReturning) {
				if (i < 0 ) {
					i++;
					onUpdate();
				} else {
					setDead();
				}
				
			} else {
				System.out.println(this.getPosition());
				System.out.println("mx: "+this.motionX+", "+"my: "+this.motionY+", "+"mz: "+this.motionZ);
				returnToThrower();
				
			}
		}
		super.onUpdate();
	}

	private void returnToThrower() {
		isReturning = true;		
//		calculating the distance to use as a motion vector
		double dx = Math.abs(Math.abs(player.posX) - Math.abs(this.posX));
		double dy = Math.abs(Math.abs((player.posY+(double)player.eyeHeight)-0.3) - Math.abs(this.posY));
		double dz = Math.abs(Math.abs(player.posZ) - Math.abs(this.posZ));
//		variables that determine the direction of the motion
		int xm = 0;
		int zm = 0;
		int ym = 0;
		if (this.motionX < 0) {
			xm = 1;
		}
		else {
			xm = -1;
		}
		if (this.motionZ < 0) {
			zm = 1;
		}
		else {
			zm = -1;
		}
		if (this.motionY < 0) {
			ym = 1;
		}
		else {
			ym = -1;
		}
//		final calculation to apply the new motion
		this.motionX = ((dx*(double)xm*0.08)+player.motionX*2);
		this.motionY = (dy*(double)ym*0.08);
		this.motionZ = ((dz*(double)zm*0.08)+player.motionZ*2);
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