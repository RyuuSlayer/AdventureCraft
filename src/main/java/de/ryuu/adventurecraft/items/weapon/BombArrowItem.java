package de.ryuu.adventurecraft.items.weapon;

import de.ryuu.adventurecraft.AdventureCraftTabs;
import de.ryuu.adventurecraft.entity.projectile.EntityBombArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BombArrowItem extends ItemArrow {

    public BombArrowItem() {
        this.setCreativeTab(AdventureCraftTabs.tab_AdventureCraftWeaponTab);
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        return new EntityBombArrow(worldIn, shooter);
    }

}