package de.ryuum3gum1n.adventurecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import de.ryuum3gum1n.adventurecraft.AdventureCraftSounds;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;

public class ShotgunItem extends ACGunItem {
	
	@Override
	protected ACGunClipItem getClip() {
		return AdventureCraftItems.shotgunClip;
	}

	@Override
	protected float getDamage() {
		return 10F;
	}

	@Override
	protected double range() {
		return 5D;
	}

	@Override
	protected SoundEvent fireSound() {
		return AdventureCraftSounds.ShotgunFire;
	}
	
	@Override
	public SoundEvent reloadSound() {
		return AdventureCraftSounds.ShotgunReload;
	}

	@Override
	protected int fireSpeed() {
		return 35;
	}

}