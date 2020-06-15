package de.ryuu.adventurecraft;

import de.ryuu.adventurecraft.client.init.AdventureCraftEntities;
import de.ryuu.adventurecraft.client.network.EntityDispatcher;
import de.ryuu.adventurecraft.client.network.Packets;
import de.ryuu.adventurecraft.client.particle.LightParticle.Factory;
import de.ryuu.adventurecraft.client.registry.LightBlocks;
import de.ryuu.adventurecraft.client.registry.LightParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class AdventureCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Light Block registry
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_0, new Factory(LightBlocks.blockLight_0.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_1, new Factory(LightBlocks.blockLight_1.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_2, new Factory(LightBlocks.blockLight_2.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_3, new Factory(LightBlocks.blockLight_3.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_4, new Factory(LightBlocks.blockLight_4.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_5, new Factory(LightBlocks.blockLight_5.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_6, new Factory(LightBlocks.blockLight_6.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_7, new Factory(LightBlocks.blockLight_7.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_8, new Factory(LightBlocks.blockLight_8.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_9, new Factory(LightBlocks.blockLight_9.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_10, new Factory(LightBlocks.blockLight_10.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_11, new Factory(LightBlocks.blockLight_11.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_12, new Factory(LightBlocks.blockLight_12.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_13, new Factory(LightBlocks.blockLight_13.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_14, new Factory(LightBlocks.blockLight_14.asItem()));
        ParticleFactoryRegistry.getInstance().register(LightParticles.lightParticle_15, new Factory(LightBlocks.blockLight_15.asItem()));

        // Bomb Item registry
        EntityRendererRegistry.INSTANCE.register(AdventureCraftEntities.BOMB, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));
        EntityRendererRegistry.INSTANCE.register(AdventureCraftEntities.DIAMOND_BOMB, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));
        EntityRendererRegistry.INSTANCE.register(AdventureCraftEntities.GOLDEN_BOMB, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));
        EntityRendererRegistry.INSTANCE.register(AdventureCraftEntities.NAVAL_MINE, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));
        EntityRendererRegistry.INSTANCE.register(AdventureCraftEntities.PULVERIS, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));

        ClientSidePacketRegistry.INSTANCE.register(Packets.SPAWN, EntityDispatcher::spawnFrom);

        // BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.GUNPOWDER_BLOCK, RenderLayer.getCutout());
    }
}