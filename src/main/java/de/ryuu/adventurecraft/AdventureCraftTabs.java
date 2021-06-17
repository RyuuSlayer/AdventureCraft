package de.ryuu.adventurecraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AdventureCraftTabs {
    public static CreativeTabs tab_AdventureCraftTab = new CreativeTabs("adventurecraftTab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(AdventureCraftItems.filler);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            // Add useful items from 'Vanilla'
            items.add(new ItemStack(Blocks.COMMAND_BLOCK));
            items.add(new ItemStack(Blocks.MOB_SPAWNER));
            items.add(new ItemStack(Blocks.BARRIER));
            items.add(new ItemStack(Blocks.STRUCTURE_BLOCK));
            super.displayAllRelevantItems(items);
        }
    };
    public static CreativeTabs tab_AdventureCraftWorldTab = new CreativeTabs("adventurecraftWorldTab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(AdventureCraftItems.goldKey);
        }
    };
    public static CreativeTabs tab_AdventureCraftWeaponTab = new CreativeTabs("adventurecraftWeaponTab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(AdventureCraftItems.bomb);
        }
    };

    // Empty stub method for 'touching' the class
    public static void init() {
    }

}