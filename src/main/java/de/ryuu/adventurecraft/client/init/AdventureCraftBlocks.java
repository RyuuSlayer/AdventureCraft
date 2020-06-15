package de.ryuu.adventurecraft.client.init;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.block.ExplosiveBarrelBlock;
import de.ryuu.adventurecraft.client.block.GunpowderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class AdventureCraftBlocks {

    public static Block GUNPOWDER_BLOCK;
    public static Block EXPLOSIVE_BARREL;

    public static void init() {
        GUNPOWDER_BLOCK = registerBlock(new GunpowderBlock(FabricBlockSettings.of(Material.SAND, MaterialColor.BLACK).strength(0.5F, 0.5f).sounds(BlockSoundGroup.SAND).breakByTool(FabricToolTags.SHOVELS).build()), "gunpowder_block", ItemGroup.BUILDING_BLOCKS);
        EXPLOSIVE_BARREL = registerBlock(new ExplosiveBarrelBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).breakByTool(FabricToolTags.AXES).nonOpaque().build()), "explosive_barrel", ItemGroup.DECORATIONS);
    }

    private static Block registerBlock(Block block, String name, ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, AdventureCraft.MOD_ID + ":" + name, block);

        if (itemGroup != null) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(itemGroup));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            AdventureCraftItems.registerItem(item, name);
        }

        return block;
    }
}