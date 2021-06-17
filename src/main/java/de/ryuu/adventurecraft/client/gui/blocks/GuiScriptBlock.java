package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.tileentity.ScriptBlockTileEntity;
import net.minecraft.util.math.BlockPos;

public class GuiScriptBlock extends QADGuiScreen {
    ScriptBlockTileEntity tileEntity;

    public GuiScriptBlock(ScriptBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();
        addComponent(new QADLabel("Script Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(),
                2, 2));
        InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(),
                new BlockInvokeHolder(position, "scriptInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_SCRIPTFILE
                        | InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_SCRIPTEMBEDDED);
    }
}