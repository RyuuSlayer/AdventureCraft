package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.tileentity.RedstoneTriggerBlockTileEntity;

public class GuiRedstoneTriggerBlock extends QADGuiScreen {
	RedstoneTriggerBlockTileEntity tileEntity;

	public GuiRedstoneTriggerBlock(RedstoneTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Redstone Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvokeOn(),  new BlockInvokeHolder(position, "triggerInvokeOn"),  InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		InvokePanelBuilder.build(this, this, 2, 42, tileEntity.getInvokeOff(), new BlockInvokeHolder(position, "triggerInvokeOff"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
	}

}