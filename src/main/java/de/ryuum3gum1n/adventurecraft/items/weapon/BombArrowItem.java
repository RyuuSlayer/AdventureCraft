package de.ryuum3gum1n.adventurecraft.items.weapon;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import de.ryuum3gum1n.adventurecraft.AdventureCraftTabs;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityBombArrow;

public class BombArrowItem extends ItemArrow {

	public BombArrowItem() {
		this.setCreativeTab(AdventureCraftTabs.tab_AdventureCraftWeaponTab);
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		return new EntityBombArrow(worldIn, shooter);
	}
	
	@Override
	public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
		super.addInformation(itemstack, world, list, flag);
		list.add("Explodes on contact");
	}

}