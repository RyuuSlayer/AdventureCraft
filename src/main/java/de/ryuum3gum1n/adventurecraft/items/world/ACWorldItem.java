package de.ryuum3gum1n.adventurecraft.items.world;

import net.minecraft.item.Item;
import de.ryuum3gum1n.adventurecraft.AdventureCraftTabs;

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