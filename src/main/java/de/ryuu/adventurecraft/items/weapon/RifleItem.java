package de.ryuu.adventurecraft.items.weapon;

import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.AdventureCraftSounds;
import net.minecraft.util.SoundEvent;

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