package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.ClientNetworkHandler;
import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADButton;
import de.ryuu.adventurecraft.client.gui.qad.QADFACTORY;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.invoke.IInvoke;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.tileentity.RelayBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.Map.Entry;

public class GuiRelayBlock extends QADGuiScreen {
    RelayBlockTileEntity tileEntity;

    public GuiRelayBlock(RelayBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(
                new QADLabel("Relay Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

        addComponent(new QADLabel("Invokes: " + tileEntity.getInvokes().size(), 2 + 24, 16 + 6));

        addComponent(QADFACTORY.createButton("+", 2, 16, 20, new Runnable() {
            @Override
            public void run() {
                String commandString = ClientNetworkHandler.makeBlockCommand(position);

                NBTTagCompound commandData = new NBTTagCompound();
                commandData.setString("command", "invoke_add");

                // Send command
                AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));

                // close whatever gui is open
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }).setTooltip("Adds a new invoke.", "", TextFormatting.RED + "WARNING:", "The current screen will close",
                "by pressing this button.", "All changes will be lost!"));

        int yOff = 16 + 22;
        int allow = InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL;
        for (Entry<String, IInvoke> entry : tileEntity.getInvokes().entrySet()) {

            final String id = entry.getKey();
            final IInvoke invoke = entry.getValue();
            InvokePanelBuilder.build(this, this, 2 + 20 + 2, yOff, invoke, new BlockInvokeHolder(position, id), allow);

            addComponent(QADFACTORY.createButton(QADButton.ICON_DELETE, 2, yOff, 20, new Runnable() {
                @Override
                public void run() {
                    String commandString = ClientNetworkHandler.makeBlockCommand(position);

                    NBTTagCompound commandData = new NBTTagCompound();
                    commandData.setString("command", "invoke_remove");
                    commandData.setString("invokeToRemove", id);

                    // Send command
                    AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));

                    // close whatever gui is open
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
            }).setTooltip("Remove invoke " + id));

            yOff += 22;
        }

    }

}