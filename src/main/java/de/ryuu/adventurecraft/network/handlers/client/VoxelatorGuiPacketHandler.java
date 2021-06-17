package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.client.gui.items.voxelator.GuiVoxelator;
import de.ryuu.adventurecraft.network.packets.VoxelatorGuiPacket;
import net.minecraft.client.Minecraft;

public class VoxelatorGuiPacketHandler {

    public static void handle(VoxelatorGuiPacket message) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiVoxelator(message.tag));
    }
}