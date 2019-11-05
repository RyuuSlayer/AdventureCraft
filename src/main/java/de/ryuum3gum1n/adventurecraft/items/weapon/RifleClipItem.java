package de.ryuum3gum1n.adventurecraft.items.weapon;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RifleClipItem extends ACGunClipItem {

	@Override
	public int clipSize() {
		return 32;
	}

	@Override
	public void onFire() {
	}
	
	@Override
	public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
		super.addInformation(itemstack, world, list, flag);
		list.add("Ammunition for the rifle");
	}

}