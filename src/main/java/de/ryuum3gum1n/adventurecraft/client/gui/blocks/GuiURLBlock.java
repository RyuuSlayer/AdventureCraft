package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.client.ClientNetworkHandler;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADButton;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADFACTORY;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADTextField;
import de.ryuum3gum1n.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuum3gum1n.adventurecraft.tileentity.URLBlockTileEntity;

public class GuiURLBlock extends QADGuiScreen {
	URLBlockTileEntity tileEntity;

	QADTextField textField_url;
	QADTextField textField_selector;

	public GuiURLBlock(URLBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("URL Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

		textField_url = new QADTextField(fontRenderer, 3, 14+20+4, width-6, 20);
		textField_url.setText(tileEntity.getURL());
		textField_url.setTooltip("The URL to open.");
		addComponent(textField_url);

		textField_selector = new QADTextField(fontRenderer, 3, 14+20+4+20+4, width-6, 20);
		textField_selector.setText(tileEntity.getSelector());
		textField_selector.setTooltip("Selector to select players.", "Default: @a");
		addComponent(textField_selector);

		QADButton setDataButton = QADFACTORY.createButton("Apply", 2, 14, 60, null);
		setDataButton.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();

				commandData.setString("selector", textField_selector.getText());
				commandData.setString("url", textField_url.getText());

				AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				displayGuiScreen(null);
			}
		});
		setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
		addComponent(setDataButton);

	}

	@Override
	public void layoutGui() {
		textField_url.setWidth(width-6);
		textField_selector.setWidth(width-6);
	}

}