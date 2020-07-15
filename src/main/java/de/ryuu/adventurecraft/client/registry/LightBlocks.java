package de.ryuu.adventurecraft.client.registry;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.block.LightBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class LightBlocks {

    public static final Block blockLight_0 = register("light_block_0", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 0));
    public static final Block blockLight_1 = register("light_block_1", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 1));
    public static final Block blockLight_2 = register("light_block_2", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 2));
    public static final Block blockLight_3 = register("light_block_3", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 3));
    public static final Block blockLight_4 = register("light_block_4", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 4));
    public static final Block blockLight_5 = register("light_block_5", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 5));
    public static final Block blockLight_6 = register("light_block_6", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 6));
    public static final Block blockLight_7 = register("light_block_7", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 7));
    public static final Block blockLight_8 = register("light_block_8", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 8));
    public static final Block blockLight_9 = register("light_block_9", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 9));
    public static final Block blockLight_10 = register("light_block_10", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 10));
    public static final Block blockLight_11 = register("light_block_11", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 11));
    public static final Block blockLight_12 = register("light_block_12", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 12));
    public static final Block blockLight_13 = register("light_block_13", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 13));
    public static final Block blockLight_14 = register("light_block_14", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 14));
    public static final Block blockLight_15 = register("light_block_15", new LightBlock(Settings.copy(Blocks.BARRIER).noCollision().lightLevel(value -> value.get(LightBlock.lightLevel)), 15));

    public static void init() {

    }

    static <T extends Block> T register(String name, T block, Item.Settings settings) {
        return register(name, block, new BlockItem(block, settings));
    }

    static <T extends Block> T register(String name, T block) {
        return register(name, block, new Item.Settings().group(AdventureCraft.GROUP));
    }

    static <T extends Block> T register(String name, T block, Function<T, BlockItem> itemFactory) {
        return register(name, block, itemFactory.apply(block));
    }

    static <T extends Block> T register(String name, T block, BlockItem item) {
        T b = Registry.register(Registry.BLOCK, AdventureCraft.id(name), block);
        if (item != null) {
            LightItems.register(name, item);
        }
        return b;
    }
}