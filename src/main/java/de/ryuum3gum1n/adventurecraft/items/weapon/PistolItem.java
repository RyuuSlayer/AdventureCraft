package de.ryuum3gum1n.adventurecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import de.ryuum3gum1n.adventurecraft.AdventureCraftSounds;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;

public class PistolItem extends ACGunItem {

	@Override
	protected ACGunClipItem getClip() {
		return AdventureCraftItems.pistolClip;
	}

	@Override
	protected float getDamage() {
		return 4F;
	}

	@Override
	protected double range() {
		return 60D;
	}

	@Override
	protected SoundEvent fireSound() {
		return AdventureCraftSounds.PistolFire;
	}

	@Override
	protected int fireSpeed() {
		return 100;
	}

	@Override
	protected boolean isPistol() {
		return true;
	}

}