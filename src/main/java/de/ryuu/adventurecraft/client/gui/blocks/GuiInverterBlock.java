package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.tileentity.InverterBlockTileEntity;
import net.minecraft.util.math.BlockPos;

public class GuiInverterBlock extends QADGuiScreen {
    InverterBlockTileEntity tileEntity;

    public GuiInverterBlock(InverterBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(new QADLabel("Inverter Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(),
                2, 2));
        InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getTriggerInvoke(),
                new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
    }

}