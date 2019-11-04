package de.ryuum3gum1n.adventurecraft.script.wrappers.item;

import java.util.List;

import net.minecraft.item.Item;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class ItemObjectWrapper implements IObjectWrapper {
	private Item item;

	public ItemObjectWrapper(Item item) {
		this.item = item;
	}

	@Override
	public Item internal() {
		return item;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getUnlocalizedName() {
		return item.getUnlocalizedName();
	}

}