package de.ryuum3gum1n.adventurecraft.script.wrappers.potion;

import java.util.List;

import net.minecraft.potion.Potion;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class PotionObjectWrapper implements IObjectWrapper {
	private Potion potion;

	public PotionObjectWrapper(Potion potion) {
		this.potion = potion;
	}

	@Override
	public Potion internal() {
		return potion;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public int getId() {
		return Potion.getIdFromPotion(potion);
	}

	public String getName() {
		return potion.getName();
	}

}