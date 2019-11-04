package de.ryuum3gum1n.adventurecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.network.packets.PlayerNBTDataMergePacket;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public class PlayerNBTDataMergePacketHandler {

	public static void handle(PlayerNBTDataMergePacket message) {
		final ClientProxy cproxy = AdventureCraft.proxy.asClient();
		final PlayerNBTDataMergePacket mpakDataMerge = message;
		cproxy.sheduleClientTickTask(new Runnable() {
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