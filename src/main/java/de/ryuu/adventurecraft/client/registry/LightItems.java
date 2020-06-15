package de.ryuu.adventurecraft.client.registry;

import de.ryuu.adventurecraft.AdventureCraft;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class LightItems {

    static Item.Settings newSettings() {
        return new Item.Settings().group(AdventureCraft.GROUP);
    }

    public static void init() {

    }

    protected static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, AdventureCraft.id(name), item);
    }
}