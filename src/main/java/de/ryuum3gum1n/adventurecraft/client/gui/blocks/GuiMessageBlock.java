package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.client.ClientNetworkHandler;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.*;
import de.ryuum3gum1n.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuum3gum1n.adventurecraft.tileentity.MessageBlockTileEntity;

public class GuiMessageBlock extends QADGuiScreen {
    final MessageBlockTileEntity tileEntity;

    QADColorTextField textField_message;
    QADTextField textField_selector;

    public GuiMessageBlock(MessageBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(new QADLabel("Message Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

        textField_message = new QADColorTextField(fontRenderer, 3, 14 + 20 + 4, width - 6, 20);
        textField_message.setText(tileEntity.getMessage());
        textField_message.setTooltip("The message to send.");
        textField_message.setMaxStringLength(5000);
        addComponent(textField_message);

        textField_selector = new QADTextField(fontRenderer, 3, 14 + 20 + 4 + 20 + 4, width - 6, 20);
        textField_selector.setText(tileEntity.getPlayerSelector());
        textField_selector.setTooltip("Selector to select players to send the message to.", "Default: @a");
        addComponent(textField_selector);

        final QADTickBox tickBox_tellraw = new QADTickBox(2 + 60 + 2, 14, 20, 20);
        tickBox_tellraw.getModel().setState(tileEntity.getTellRaw());
        tickBox_tellraw.setTooltip("Should the message be parsed as raw json?", "Default: Off");
        addComponent(tickBox_tellraw);

        QADButton setDataButton = QADFACTORY.createButton("Apply", 2, 14, 60, null);
        setDataButton.setAction(() -> {
            String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
            NBTTagCompound commandData = new NBTTagCompound();

            // data?
            commandData.setString("playerSelector", textField_selector.getText());
            commandData.setString("message", textField_message.getText());
            commandData.setBoolean("tellraw", tickBox_tellraw.getState());

            AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
            displayGuiScreen(null);
        });
        setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
        addComponent(setDataButton);

    }

    @Override
    public void layoutGui() {
        textField_message.setWidth(width - 6);
        textField_selector.setWidth(width - 6);
    }

}