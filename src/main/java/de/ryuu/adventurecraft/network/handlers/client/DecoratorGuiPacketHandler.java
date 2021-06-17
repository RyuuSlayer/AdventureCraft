package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.client.gui.items.GuiDecorator;
import de.ryuu.adventurecraft.network.packets.DecoratorGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class DecoratorGuiPacketHandler {
    public static void handle(DecoratorGuiPacket message) {
        List<String> list = new ArrayList<String>();
        NBTTagList tl = message.tag.getTagList("decorations_list", 8);
        for (int i = 0; i < tl.tagCount(); i++) {
            list.add(tl.getStringTagAt(i));
        }
        Minecraft.getMinecraft().displayGuiScreen(new GuiDecorator(list, message.tag));
    }
}