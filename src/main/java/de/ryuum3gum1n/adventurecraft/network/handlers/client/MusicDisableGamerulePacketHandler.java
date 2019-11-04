package de.ryuum3gum1n.adventurecraft.network.handlers.client;

import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.network.packets.GameruleSyncPacket;

public class MusicDisableGamerulePacketHandler {

	public static void handle(GameruleSyncPacket message) {
		AdventureCraft.proxy.asClient().gamerules.readFromNBT(message.gr);
	}

}