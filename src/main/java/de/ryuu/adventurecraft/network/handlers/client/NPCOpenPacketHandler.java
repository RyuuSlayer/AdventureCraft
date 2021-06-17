package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.client.gui.entity.npc.NPCEditorGui;
import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import de.ryuu.adventurecraft.entity.NPC.NPCData;
import de.ryuu.adventurecraft.network.packets.NPCOpenPacket;
import net.minecraft.client.Minecraft;

public class NPCOpenPacketHandler {

    public static void handle(NPCOpenPacket message) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityNPC npc = (EntityNPC) mc.world.getEntityByID(message.id);
        EntityNPC newNPC = new EntityNPC(npc.world);
        mc.displayGuiScreen(new NPCEditorGui(npc.getUniqueID(), NPCData.fromNBT(newNPC, npc.getNPCData().toNBT())));
    }

}