package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.tileentity.CameraBlockTileEntity;
import net.minecraft.util.math.BlockPos;

public class GuiCameraBlock extends QADGuiScreen {
    private CameraBlockTileEntity te;

    public GuiCameraBlock(CameraBlockTileEntity tileEntity) {
        this.te = tileEntity;
    }

    @Override
    public void buildGui() {
        removeAllComponents();
        BlockPos position = te.getPosition();
        addComponent(new QADLabel("Camera Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(),
                2, 2));
    }
}