package de.ryuu.adventurecraft.client.init;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class AdventureCraftEntities {

    public static EntityType<BombEntity> BOMB;
    public static EntityType<BombEntity> GOLDEN_BOMB;
    public static EntityType<BombEntity> DIAMOND_BOMB;
    public static EntityType<BombEntity> PULVERIS;
    public static EntityType<BombEntity> NAVAL_MINE;

    public static BlockEntityType<ExplosiveBarrelBlockEntity> EXPLOSIVE_BARREL;

    public static void init() {
        BOMB = Registry.register(Registry.ENTITY_TYPE, AdventureCraft.MOD_ID + ":bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, BombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        GOLDEN_BOMB = Registry.register(Registry.ENTITY_TYPE, AdventureCraft.MOD_ID + ":golden_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, GoldenBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        DIAMOND_BOMB = Registry.register(Registry.ENTITY_TYPE, AdventureCraft.MOD_ID + ":diamond_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, DiamondBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        PULVERIS = Registry.register(Registry.ENTITY_TYPE, AdventureCraft.MOD_ID + ":pulveris", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, PulverisEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        NAVAL_MINE = Registry.register(Registry.ENTITY_TYPE, AdventureCraft.MOD_ID + ":naval_mine", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, NavalMineEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());

        // EXPLOSIVE_BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE, MODID + ":explosive_barrel", BlockEntityType.Builder.create(ExplosiveBarrelBlockEntity::new, BlastBlocks.EXPLOSIVE_BARREL));
    }
}