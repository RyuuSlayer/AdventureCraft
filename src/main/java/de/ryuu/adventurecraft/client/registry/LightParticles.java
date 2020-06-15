package de.ryuu.adventurecraft.client.registry;

import de.ryuu.adventurecraft.AdventureCraft;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class LightParticles {

    public static final DefaultParticleType lightParticle_0 = register("light_block_0", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_1 = register("light_block_1", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_2 = register("light_block_2", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_3 = register("light_block_3", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_4 = register("light_block_4", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_5 = register("light_block_5", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_6 = register("light_block_6", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_7 = register("light_block_7", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_8 = register("light_block_8", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_9 = register("light_block_9", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_10 = register("light_block_10", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_11 = register("light_block_11", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_12 = register("light_block_12", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_13 = register("light_block_13", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_14 = register("light_block_14", FabricParticleTypes.simple());
    public static final DefaultParticleType lightParticle_15 = register("light_block_15", FabricParticleTypes.simple());

    public static void init() {

    }

    protected static DefaultParticleType register(String name, DefaultParticleType particle) {
        return Registry.register(Registry.PARTICLE_TYPE, AdventureCraft.id(name), particle);
    }
}