package de.ryuu.adventurecraft.items.weapon;

public abstract class ACGunClipItem extends ACWeaponItem {

    public ACGunClipItem() {
        this.setMaxStackSize(64);
    }

    public abstract int clipSize();

    public abstract void onFire();

}