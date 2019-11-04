package de.ryuum3gum1n.adventurecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import de.ryuum3gum1n.adventurecraft.client.gui.entity.npc.NPCEditorGui;
import de.ryuum3gum1n.adventurecraft.entity.NPC.EntityNPC;
import de.ryuum3gum1n.adventurecraft.entity.NPC.NPCData;
import de.ryuum3gum1n.adventurecraft.network.packets.NPCOpenPacket;

public class NPCOpenPacketHandler {

	public static void handle(NPCOpenPacket message) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityNPC npc = (EntityNPC) mc.world.getEntityByID(message.id);
		EntityNPC newNPC = new EntityNPC(npc.world);
		mc.displayGuiScreen(new NPCEditorGui(npc.getUniqueID(), NPCData.fromNBT(newNPC, npc.getNPCData().toNBT())));
	}

}