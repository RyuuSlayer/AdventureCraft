package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuum3gum1n.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.tileentity.CollisionTriggerBlockTileEntity;

public class GuiCollisionTriggerBlock extends QADGuiScreen {
	CollisionTriggerBlockTileEntity tileEntity;

	public GuiCollisionTriggerBlock(CollisionTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Collision Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getCollisionStartInvoke(), new BlockInvokeHolder(position, "collisionStartTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		InvokePanelBuilder.build(this, this, 2, 16+2+20, tileEntity.getCollisionStopInvoke(), new BlockInvokeHolder(position, "collisionStopTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

		// TODO: Add entityFilter input+apply

	}

}