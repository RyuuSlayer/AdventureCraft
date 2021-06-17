package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.ClientNetworkHandler;
import de.ryuu.adventurecraft.client.gui.qad.*;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.tileentity.URLBlockTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class GuiURLBlock extends QADGuiScreen {
    final URLBlockTileEntity tileEntity;

    QADTextField textField_url;
    QADTextField textField_selector;

    public GuiURLBlock(URLBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(new QADLabel("URL Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

        textField_url = new QADTextField(fontRenderer, 3, 14 + 20 + 4, width - 6, 20);
        textField_url.setText(tileEntity.getURL());
        textField_url.setTooltip("The URL to open.");
        textField_url.setMaxStringLength(2000);
        addComponent(textField_url);

        textField_selector = new QADTextField(fontRenderer, 3, 14 + 20 + 4 + 20 + 4, width - 6, 20);
        textField_selector.setText(tileEntity.getSelector());
        textField_selector.setTooltip("Selector to select players.", "Default: @a");
        addComponent(textField_selector);

        QADButton setDataButton = QADFACTORY.createButton("Apply", 2, 14, 60, null);
        setDataButton.setAction(() -> {
            String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
            NBTTagCompound commandData = new NBTTagCompound();

            commandData.setString("selector", textField_selector.getText());
            commandData.setString("url", textField_url.getText());

            AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
            displayGuiScreen(null);
        });
        setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
        addComponent(setDataButton);

    }

    @Override
    public void layoutGui() {
        textField_url.setWidth(width - 6);
        textField_selector.setWidth(width - 6);
    }


}