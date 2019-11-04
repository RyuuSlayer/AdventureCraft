package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.tileentity.BlockUpdateDetectorTileEntity;

public class GuiUpdateDetectorBlock extends QADGuiScreen {
	BlockUpdateDetectorTileEntity tileEntity;

	public GuiUpdateDetectorBlock(BlockUpdateDetectorTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel(
				"Block Update Detector Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2,
				2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(),
				new BlockInvokeHolder(position, "detectorInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
	}

}