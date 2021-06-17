package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.network.packets.GameruleSyncPacket;

public class MusicDisableGamerulePacketHandler {

    public static void handle(GameruleSyncPacket message) {
        AdventureCraft.proxy.asClient().gamerules.readFromNBT(message.gr);
    }

}