package de.ryuum3gum1n.adventurecraft.items.weapon;

public abstract class ACGunClipItem extends ACWeaponItem{
	
	public ACGunClipItem(){
		this.setMaxStackSize(64);
	}
	
	public abstract int clipSize();
	
	public abstract void onFire();

}