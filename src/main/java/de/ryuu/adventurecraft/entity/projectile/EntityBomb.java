package de.ryuu.adventurecraft.entity.projectile;

import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.util.BombExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityBomb extends EntityThrowable {

    private int explosion_delay = 60;
    private boolean should_play_sound = true;
    private boolean updateMovementLogic = true;

    public EntityBomb(World world) {
        super(world);
        this.setSize(0.1F, 0.1F);
    }

    public EntityBomb(World world, EntityLivingBase thrower) {
        super(world, thrower);
        this.setSize(0.1F, 0.1F);
    }

    public EntityBomb(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.setSize(0.1F, 0.1F);
    }

    public void setFuse(int fuse) {
        explosion_delay = fuse;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            explode(); // explode on impact
            updateMovementLogic = false;
            return;
        }

        if (result.typeOfHit == Type.BLOCK) {
            Vec3d nextPos = new Vec3d(motionX, motionY, motionZ).add(getPositionVector());

            if (this.world.isAirBlock(new BlockPos(nextPos))) {
                switch (result.sideHit) {
                    case NORTH:
                    case SOUTH:
                    case EAST:
                    case WEST: {
                        rotationPitch = 90;
                        setVelocityNew(0, 0, 0);
                    }
                    break;
                    case UP: {
                        rotationPitch = -rotationPitch;
                        setVelocityNew(motionX * 0.5f, 0, motionZ * 0.5f);
                    }
                    break;
                    case DOWN: {
                        updateMovementLogic = false;
                        rotationPitch = -rotationPitch;
                        setVelocityNew(0, 0, 0);
                    }
                    break;
                }
            } else {
                /*
                 * if(world.getBlockState(getPosition()).isFullBlock()) {
                 * setPositionAndUpdate(posX, posY + 1f/16f, posZ); setVelocityNew(0, motionY,
                 * 0); }
                 */
            }

            /*
             * if(world.getBlockState(result.getBlockPos()).isFullBlock()){
             * updateMovementLogic = false; //setVelocityNew(0, 0, 0); }
             */
        }
    }

    @Override
    public void onUpdate() {
        if (updateMovementLogic)
            super.onUpdate();
        if (should_play_sound) {
            world.playSound(posX, posY, posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.AMBIENT, 100f, 1f, false);
            should_play_sound = false;
        }
        explosion_delay--;
        if (explosion_delay <= 0) {
            explode();
        }
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + .3, posZ, 0, 0, 0, null);
        if (explosion_delay <= 20)
            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + .3, posZ, 0, 0, 0, null);
    }

    // For some reason setVelocity(x, y, z) is Client-Side only
    public void setVelocityNew(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        velocityChanged = true;
    }

    private void explode() {
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, 0, 0, 0, null);
        if (!world.isRemote) {
            BombExplosion explosion = new BombExplosion(world, this, posX, posY, posZ);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("explosion_delay", explosion_delay);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        explosion_delay = tag.getInteger("explosion_delay");
    }

    @SuppressWarnings("rawtypes")
    public static class EntityBombRenderFactory implements IRenderFactory {
        @Override
        public Render createRenderFor(RenderManager manager) {
            Minecraft mc = Minecraft.getMinecraft();
            return new RenderSnowball(manager, AdventureCraftItems.bomb, mc.getRenderItem());
        }
    }

}