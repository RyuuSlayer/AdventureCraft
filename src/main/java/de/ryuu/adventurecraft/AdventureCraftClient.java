package de.ryuu.adventurecraft;

import com.google.common.collect.Maps;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import de.ryuu.adventurecraft.client.particle.LightParticle.Factory;
import de.ryuu.adventurecraft.client.registry.LightBlocks;
import de.ryuu.adventurecraft.client.registry.LightParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.util.Util;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static net.minecraft.world.gen.GeneratorOptions.method_28608;

@Environment(EnvType.CLIENT)
public class AdventureCraftClient implements ClientModInitializer {

    private static final Logger LOGGER = LogManager.getLogger();
    public static AdventureCraftClient MENU_INSTANCE;
    private final String worldName = "Your Map";

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

        // Menu Button registry
        MENU_INSTANCE = new AdventureCraftClient();
    }

    // TODO: Put all this "World creating from menu" stuff into its seperate class

    // Menu: Create World Button
    public void createWorld() {

        MinecraftClient.getInstance().openScreen(null);

        GeneratorOptions BUILD_MAP_GENERATOR = new GeneratorOptions(0L, false, false, method_28608(DimensionType.method_28517(0L), new FlatChunkGenerator(getAdventureCraftWorldGeneratorConfig())));

        GameRules gameRules = new GameRules();
        gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
        gameRules.get(GameRules.DO_WEATHER_CYCLE).set(false, null);
        gameRules.get(GameRules.DO_MOB_SPAWNING).set(false, null);

        LevelInfo levelInfo = new LevelInfo(worldName, GameMode.CREATIVE, false, Difficulty.NORMAL, false, gameRules, DataPackSettings.SAFE_MODE);

        File gameDir = MinecraftClient.getInstance().runDirectory;
        LevelStorage levelStorage = new LevelStorage(gameDir.toPath().resolve("saves"), gameDir.toPath().resolve("backups"), null);
        LevelProperties levelProperties = null;
        try {
            LevelStorage.Session session = levelStorage.createSession(worldName);

            CompoundTag worldData = new CompoundTag();

            worldData.putInt("Difficulty", 2);

            // World Generator
            worldData.putString("generatorName", "flat");
            DataResult tagDataResult = GeneratorOptions.CODEC.encodeStart(NbtOps.INSTANCE, BUILD_MAP_GENERATOR);
            tagDataResult.resultOrPartial(Util.method_29188("WorldGenSettings: ", LOGGER::error)).ifPresent((tag) -> worldData.put("WorldGenSettings", (Tag) tag));

            // Cheat Mode
            worldData.putInt("GameType", GameMode.CREATIVE.getId());
            worldData.putBoolean("allowCommands", true);

            // World Initialized
            worldData.putBoolean("initialized", true);

            // Set Day
            worldData.putLong("Time", 6000);
            worldData.putLong("DayTime", 6000);

            worldData.put("GameRules", gameRules.toNbt());

            Dynamic<Tag> dynamic = new Dynamic<>(NbtOps.INSTANCE, worldData);
            SaveVersionInfo lv = SaveVersionInfo.fromDynamic(dynamic);

            levelInfo = LevelInfo.method_28383(dynamic, DataPackSettings.SAFE_MODE); // Sets Cheat mode enabled

            levelProperties = new LevelProperties(levelInfo, BUILD_MAP_GENERATOR, Lifecycle.stable())
                    .method_29029(dynamic, MinecraftClient.getInstance().getDataFixer(), 16, null, levelInfo, lv, BUILD_MAP_GENERATOR, Lifecycle.stable());

            // Spawn Location
            levelProperties.setSpawnX(0);
            levelProperties.setSpawnY(55);
            levelProperties.setSpawnZ(0);

            session.method_27426(DynamicRegistryManager.create(), levelProperties, worldData);
            session.save(worldName);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start the World
        assert levelProperties != null;
        MinecraftClient.getInstance().method_29607(worldName, levelProperties.getLevelInfo(), DynamicRegistryManager.create(), BUILD_MAP_GENERATOR);
    }

    private FlatChunkGeneratorConfig getAdventureCraftWorldGeneratorConfig() {
        FlatChunkGeneratorConfig flatChunkGeneratorConfig = new FlatChunkGeneratorConfig(new StructuresConfig(Optional.of(StructuresConfig.DEFAULT_STRONGHOLD), Maps.newHashMap()));
        flatChunkGeneratorConfig.setBiome(Biomes.PLAINS);
        flatChunkGeneratorConfig.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
        flatChunkGeneratorConfig.getLayers().add(new FlatChunkGeneratorLayer(3, Blocks.STONE));
        flatChunkGeneratorConfig.getLayers().add(new FlatChunkGeneratorLayer(52, Blocks.SANDSTONE));
        flatChunkGeneratorConfig.updateLayerBlocks();
        return flatChunkGeneratorConfig;
    }

    // Menu: Delete World Button
    public void deleteWorld() {
        LevelStorage levelStorage = MinecraftClient.getInstance().getLevelStorage();
        try {
            LevelStorage.Session session = levelStorage.createSession(worldName);
            session.deleteSessionLock();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menu: Loading World
    public void loadWorld() {
        if (MinecraftClient.getInstance().getLevelStorage().levelExists(worldName)) {
            MinecraftClient.getInstance().startIntegratedServer(worldName);
        }
    }

    // Menu: Check whether or not the world exists
    public boolean saveExist() {
        return MinecraftClient.getInstance().getLevelStorage().levelExists(worldName);
    }
}