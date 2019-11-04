package de.ryuum3gum1n.adventurecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import de.ryuum3gum1n.adventurecraft.client.gui.misc.GuiUndo;
import de.ryuum3gum1n.adventurecraft.network.packets.UndoGuiPacket;

public class UndoGuiPacketHandler{
	
	public static void handle(UndoGuiPacket message){
		Minecraft.getMinecraft().displayGuiScreen(new GuiUndo(message.tag));
	}
}