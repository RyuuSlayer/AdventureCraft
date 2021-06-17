package de.ryuu.adventurecraft.items;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ACITriggerableItem {
    void trigger(World world, EntityPlayerMP player, ItemStack stack);
}