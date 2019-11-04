package de.ryuum3gum1n.adventurecraft.blocks.util;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;
import de.ryuum3gum1n.adventurecraft.blocks.ACBlockContainer;
import de.ryuum3gum1n.adventurecraft.blocks.ACITriggerableBlock;
import de.ryuum3gum1n.adventurecraft.client.gui.blocks.GuiCameraBlock;
import de.ryuum3gum1n.adventurecraft.invoke.EnumTriggerState;
import de.ryuum3gum1n.adventurecraft.tileentity.CameraBlockTileEntity;

public class CameraBlock extends ACBlockContainer implements ACITriggerableBlock {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CameraBlockTileEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if(!worldIn.isRemote){
			if(heldItem != null && heldItem.getItem() == AdventureCraftItems.camera){
				NBTTagCompound tag = new NBTTagCompound();
				tag.setLong("selected", pos.toLong());
				heldItem.setTagCompound(tag);
				playerIn.sendMessage(new TextComponentString("Selected camera block!"));
			}
			return true;
		}
		if(!AdventureCraft.proxy.isBuildMode())
			return false;
		if(playerIn.isSneaking())
			return true;

		if(heldItem == null || heldItem.getItem() != AdventureCraftItems.camera){
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiCameraBlock((CameraBlockTileEntity)worldIn.getTileEntity(pos)));
		}
		return true;
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
			return;

		TileEntity tileentity = world.getTileEntity(position);

		if (tileentity instanceof CameraBlockTileEntity) {
			((CameraBlockTileEntity)tileentity).trigger(triggerState);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Currently a W.I.P.");
		tooltip.add("More features will come soon");
	}

}