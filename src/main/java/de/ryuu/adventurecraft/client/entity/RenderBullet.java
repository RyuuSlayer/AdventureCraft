package de.ryuu.adventurecraft.client.entity;

import de.ryuu.adventurecraft.entity.projectile.EntityBullet;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBullet extends Render<EntityBullet> {

    public RenderBullet(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Override
    public boolean shouldRender(EntityBullet livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBullet entity) {
        return null;
    }

}