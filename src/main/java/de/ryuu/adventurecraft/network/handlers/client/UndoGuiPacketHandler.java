package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.client.gui.misc.GuiUndo;
import de.ryuu.adventurecraft.network.packets.UndoGuiPacket;
import net.minecraft.client.Minecraft;

public class UndoGuiPacketHandler {

    public static void handle(UndoGuiPacket message) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiUndo(message.tag));
    }
}