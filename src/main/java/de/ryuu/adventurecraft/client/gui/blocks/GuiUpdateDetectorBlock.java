package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.tileentity.BlockUpdateDetectorTileEntity;
import net.minecraft.util.math.BlockPos;

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