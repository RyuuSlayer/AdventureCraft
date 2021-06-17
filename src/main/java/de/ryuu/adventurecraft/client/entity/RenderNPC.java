package de.ryuu.adventurecraft.client.entity;

import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import de.ryuu.adventurecraft.entity.NPC.EnumNPCSkin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderNPC extends RenderLivingBase<EntityNPC> {

    public RenderNPC(RenderManager manager) {
        super(manager, new ModelNPC(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        if (entity.getNPCData().getSkin() == EnumNPCSkin.Custom)
            return new ResourceLocation("adventurecraft:textures/entity/" + entity.getNPCData().getCustomSkin());
        return entity.getNPCData().getSkin().getResourceLocation();
    }

    @Override
    protected boolean canRenderName(EntityNPC entity) {
        return entity.getNPCData().shouldShowName();
    }

    @SuppressWarnings("rawtypes")
    public static class NPCRenderFactory implements IRenderFactory {
        @Override
        public Render createRenderFor(RenderManager manager) {
            return new RenderNPC(manager);

        }
    }

}