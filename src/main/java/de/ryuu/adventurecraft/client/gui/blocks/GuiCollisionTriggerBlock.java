package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.tileentity.CollisionTriggerBlockTileEntity;
import net.minecraft.util.math.BlockPos;

public class GuiCollisionTriggerBlock extends QADGuiScreen {
    CollisionTriggerBlockTileEntity tileEntity;

    public GuiCollisionTriggerBlock(CollisionTriggerBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();
        addComponent(new QADLabel(
                "Collision Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
        InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getCollisionStartInvoke(),
                new BlockInvokeHolder(position, "collisionStartTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
        InvokePanelBuilder.build(this, this, 2, 16 + 2 + 20, tileEntity.getCollisionStopInvoke(),
                new BlockInvokeHolder(position, "collisionStopTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

        // TODO: Add entityFilter input+apply

    }

}