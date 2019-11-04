package de.ryuum3gum1n.adventurecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AdventureCraftTabs {
	// Empty stub method for 'touching' the class
	public static final void init() {
	}

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

	public static CreativeTabs tab_AdventureCraftDecorationTab = new CreativeTabs("adventurecraftDecoTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.DYE, 1, this.getIconItemDamage());
		}

		@SideOnly(Side.CLIENT)
		public int getIconItemDamage() {
			return (int) ((Minecraft.getSystemTime() / 100D) % 16);
		}
	};

	public static CreativeTabs tab_AdventureCraftWorldTab = new CreativeTabs("adventurecrafttWorldTab") {
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

}