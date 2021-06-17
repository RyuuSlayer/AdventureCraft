package de.ryuu.adventurecraft.network.handlers.client;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.network.packets.PlayerNBTDataMergePacket;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;

public class PlayerNBTDataMergePacketHandler {

    public static void handle(PlayerNBTDataMergePacket message) {
        final ClientProxy cproxy = AdventureCraft.proxy.asClient();
        final PlayerNBTDataMergePacket mpakDataMerge = message;
        cproxy.scheduleClientTickTask(new Runnable() {
            Minecraft micr = ClientProxy.mc;

            @Override
            public void run() {
                if (micr.player != null) {
                    micr.player.getEntityData().merge((mpakDataMerge.data));
                }
            }
        });
    }
}