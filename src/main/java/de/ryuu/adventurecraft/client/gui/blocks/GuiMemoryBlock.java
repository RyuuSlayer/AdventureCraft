package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.ClientNetworkHandler;
import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADButton;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.client.gui.qad.model.AbstractButtonModel;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.tileentity.MemoryBlockTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class GuiMemoryBlock extends QADGuiScreen {
    MemoryBlockTileEntity tileEntity;

    public GuiMemoryBlock(MemoryBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(new QADLabel("Memory Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(),
                2, 2));
        InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getTriggerInvoke(),
                new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

        addComponent(new QADButton(2, 16 + 2 + 20 + 2, 60, new AbstractButtonModel("Reset") {
            @Override
            public void onClick() {
                String commandString = ClientNetworkHandler.makeBlockCommand(position);
                NBTTagCompound commandData = new NBTTagCompound();
                commandData.setString("command", "reset");
                AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
                GuiMemoryBlock.this.mc.displayGuiScreen(null);
            }

        }));
        addComponent(new QADLabel("Triggered: " + tileEntity.getIsTriggered(), 2 + 60 + 2, 16 + 2 + 20 + 2 + 6));

    }

}