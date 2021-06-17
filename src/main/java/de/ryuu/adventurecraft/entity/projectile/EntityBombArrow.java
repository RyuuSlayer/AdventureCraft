package de.ryuu.adventurecraft.entity.projectile;

import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.client.entity.RenderBombArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityBombArrow extends EntityArrow {

    public EntityBombArrow(World worldIn) {
        super(worldIn);
    }

    public EntityBombArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityBombArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(AdventureCraftItems.bombArrow);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!world.isRemote) {
            EntityBomb bomb = new EntityBomb(world, this.posX, this.posY, this.posZ);
            bomb.setFuse(0);
            world.spawnEntity(bomb);
            setDead();
        }
    }

    @SuppressWarnings("rawtypes")
    public static class EntityBombArrowRenderFactory implements IRenderFactory {
        @Override
        public Render createRenderFor(RenderManager manager) {
            return new RenderBombArrow(manager);
        }
    }

}