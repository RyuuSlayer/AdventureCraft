package de.ryuum3gum1n.adventurecraft.blocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.blocks.ACBlockContainer;
import de.ryuum3gum1n.adventurecraft.blocks.ACITriggerableBlock;
import de.ryuum3gum1n.adventurecraft.client.gui.blocks.GuiUpdateDetectorBlock;
import de.ryuum3gum1n.adventurecraft.invoke.EnumTriggerState;
import de.ryuum3gum1n.adventurecraft.tileentity.BlockUpdateDetectorTileEntity;

public class BlockUpdateDetector extends ACBlockContainer implements ACITriggerableBlock {

	public BlockUpdateDetector() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote)
			return true;
		if (!AdventureCraft.proxy.isBuildMode())
			return false;
		if (playerIn.isSneaking())
			return true;

		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiUpdateDetectorBlock((BlockUpdateDetectorTileEntity) worldIn.getTileEntity(pos)));

		return true;
	}

	@Deprecated
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos from) {
		BlockUpdateDetectorTileEntity tEntity = (BlockUpdateDetectorTileEntity) worldIn.getTileEntity(pos);
		if (tEntity != null) {
			tEntity.triggerUpdateInvoke(EnumTriggerState.ON);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BlockUpdateDetectorTileEntity();
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		BlockUpdateDetectorTileEntity tEntity = (BlockUpdateDetectorTileEntity) world.getTileEntity(position);
		if (tEntity != null) {
			tEntity.triggerUpdateInvoke(triggerState);
		}
	}

}