package de.ryuum3gum1n.adventurecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import de.ryuum3gum1n.adventurecraft.AdventureCraftSounds;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;

public class RifleItem extends ACGunItem {

	@Override
	protected ACGunClipItem getClip() {
		return AdventureCraftItems.rifleClip;
	}

	@Override
	protected float getDamage() {
		return 3F;
	}

	@Override
	protected double range() {
		return 100D;
	}

	@Override
	protected SoundEvent fireSound() {
		return AdventureCraftSounds.RifleFire;
	}

	@Override
	protected int fireSpeed() {
		return 0;
	}

}