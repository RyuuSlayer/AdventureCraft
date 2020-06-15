package de.ryuu.adventurecraft.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.particle.DefaultParticleType;

public class LightParticle extends SpriteBillboardParticle {

    protected LightParticle(ClientWorld world, double x, double y, double z, ItemConvertible itemConvertible) {
        super(world, x, y, z);
        this.setSprite(MinecraftClient.getInstance().getItemRenderer().getModels().getSprite(itemConvertible));
        this.gravityStrength = 0.0F;
        this.maxAge = 40;
        this.collidesWithWorld = false;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.TERRAIN_SHEET;
    }

    public float getSize(float tickDelta) {
        return 0.5F;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final Item theItem;

        public Factory(Item item) {
            theItem = item;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new LightParticle(world, x, y, z, theItem);
        }
    }
}