package de.ryuu.adventurecraft.items.weapon;

import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.AdventureCraftSounds;
import net.minecraft.util.SoundEvent;

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