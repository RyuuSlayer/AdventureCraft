package de.ryuu.adventurecraft;

import de.ryuu.adventurecraft.client.init.AdventureCraftBlocks;
import de.ryuu.adventurecraft.client.init.AdventureCraftEntities;
import de.ryuu.adventurecraft.client.init.AdventureCraftItems;
import de.ryuu.adventurecraft.client.registry.LightBlocks;
import de.ryuu.adventurecraft.client.registry.LightItems;
import de.ryuu.adventurecraft.client.registry.LightParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AdventureCraft implements ModInitializer {

    public static final String MOD_ID = "adventurecraft";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(id("group"), () -> new ItemStack(LightBlocks.blockLight_0));

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        LightBlocks.init();
        LightItems.init();
        LightParticles.init();
        AdventureCraftItems.init();
        AdventureCraftEntities.init();
        AdventureCraftBlocks.init();
    }
}