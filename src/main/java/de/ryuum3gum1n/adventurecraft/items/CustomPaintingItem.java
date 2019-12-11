package de.ryuum3gum1n.adventurecraft.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class CustomPaintingItem extends ACItem implements ACITriggerableItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		BlockPos blockpos = pos.offset(facing);
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, stack)) {
			EntityPainting painting = new EntityPainting(worldIn, blockpos, facing);

			if (painting != null && painting.onValidSurface()) {
				if (!worldIn.isRemote) {
					painting.playPlaceSound();
					try {
						painting.art = EnumArt.valueOf(stack.getTagCompound().getString("art"));
					} catch (Exception e) {
						e.printStackTrace();
						painting.art = EnumArt.BOMB;
					}
					worldIn.spawnEntity(painting);
				}
			} else if (!painting.onValidSurface()) {
				Minecraft.getMinecraft().player.sendMessage(
						new TextComponentString(TextFormatting.RED + "Paintings cannot be placed on this block!"));
			}

			return EnumActionResult.SUCCESS;
		} else if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString(TextFormatting.RED + "Paintings cannot be placed here!"));
			return EnumActionResult.FAIL;
		} else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if (!stack.hasTagCompound())
			return;
		EnumArt painting;
		try {
			painting = EnumArt.valueOf(stack.getTagCompound().getString("art"));
		} catch (Exception e) {
			painting = EnumArt.BOMB;
		}
		tooltip.add("Painting: " + painting.title);
		tooltip.add("Size: " + painting.sizeX / 16 + "x" + painting.sizeY / 16);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
		if (world.isRemote)
			return;
		if (stack.hasTagCompound())
			return;
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("art", EnumArt.KEBAB.name());
		stack.setTagCompound(tag);
	}

	@Override
	public void trigger(World world, EntityPlayerMP player, ItemStack stack) {
		if (world.isRemote)
			return;
		int current = EnumArt.valueOf(stack.getTagCompound().getString("art")).ordinal();
		current++;
		if (current >= EnumArt.values().length)
			current = 0;
		EnumArt art = EnumArt.values()[current];
		stack.getTagCompound().setString("art", art.name());
		player.sendMessage(new TextComponentString("Changed painting to " + TextFormatting.GOLD + art.title
				+ TextFormatting.GREEN + " (" + art.sizeX / 16 + "x" + art.sizeY / 16 + ")"));
	}

}