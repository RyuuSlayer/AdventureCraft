package de.ryuum3gum1n.adventurecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import de.ryuum3gum1n.adventurecraft.client.gui.items.voxelator.GuiVoxelator;
import de.ryuum3gum1n.adventurecraft.network.packets.VoxelatorGuiPacket;

public class VoxelatorGuiPacketHandler{
	
	public static void handle(VoxelatorGuiPacket message){
		Minecraft.getMinecraft().displayGuiScreen(new GuiVoxelator(message.tag));
	}
}