package de.ryuu.adventurecraft.items.world;

import de.ryuu.adventurecraft.AdventureCraftTabs;
import net.minecraft.item.Item;

public class ACWorldItem extends Item {

    public ACWorldItem() {
        this.setCreativeTab(AdventureCraftTabs.tab_AdventureCraftWorldTab);
        this.setMaxStackSize(64);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

}