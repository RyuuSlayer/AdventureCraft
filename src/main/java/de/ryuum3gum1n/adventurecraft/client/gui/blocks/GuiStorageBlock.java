package de.ryuum3gum1n.adventurecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.client.ClientNetworkHandler;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADFACTORY;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADLabel;
import de.ryuum3gum1n.adventurecraft.items.WandItem;
import de.ryuum3gum1n.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuum3gum1n.adventurecraft.tileentity.StorageBlockTileEntity;

public class GuiStorageBlock extends QADGuiScreen {
	private StorageBlockTileEntity tileEntity;

	public GuiStorageBlock(StorageBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Storage Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(),
				2, 2));

		addComponent(QADFACTORY.createButton("Set Region & Store", 2, 16 + (22 * 0), 100, new Runnable() {
			@Override
			public void run() {
				int[] bounds = WandItem.getBoundsFromPlayerOrNull(mc.player);

				if (bounds == null)
					return;

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setIntArray("bounds", bounds);
				AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

		addComponent(QADFACTORY.createButton("Store", 2, 16 + (22 * 1), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "store");
				AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

		addComponent(QADFACTORY.createButton("Trigger (Paste)", 2, 16 + (22 * 2), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "trigger");
				AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

	}

}