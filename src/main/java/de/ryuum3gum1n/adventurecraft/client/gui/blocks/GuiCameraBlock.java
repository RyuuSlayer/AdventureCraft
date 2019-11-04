package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.tileentity.CameraBlockTileEntity;

public class GuiCameraBlock extends QADGuiScreen {
	private CameraBlockTileEntity te;

	public GuiCameraBlock(CameraBlockTileEntity tileEntity) {
		this.te = tileEntity;
	}

	@Override
	public void buildGui() {
		removeAllComponents();
		BlockPos position = te.getPosition();
		addComponent(new QADLabel("Camera Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
	}
}