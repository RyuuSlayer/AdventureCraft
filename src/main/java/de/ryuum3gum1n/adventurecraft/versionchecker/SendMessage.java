package de.ryuum3gum1n.adventurecraft.versionchecker;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import de.ryuum3gum1n.adventurecraft.Reference;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public class SendMessage {

	@SubscribeEvent
	public void onClientConnection(ClientConnectedToServerEvent event) {
		try {
			final ACVersion latest = VersionParser.getLatestVersion();
			final String current = Reference.MOD_VERSION; // to be easily changeable for debugging
			if (latest.isGreaterVersion(current)) {
				final ClientProxy cproxy = AdventureCraft.proxy.asClient();
				cproxy.sheduleClientTickTask(new Runnable() {
					Minecraft mc = ClientProxy.mc;

					@Override
					public void run() {
						while (mc.player == null) {
						}
						String message = TextFormatting.YELLOW + "AdventureCraft version is outdated! Your version is "
								+ TextFormatting.GOLD + current + TextFormatting.YELLOW + ". The latest is "
								+ TextFormatting.GOLD + latest.getVersion() + TextFormatting.YELLOW + ".";
						mc.player.sendMessage(new TextComponentString(message));
						AdventureCraft.logger.warn(TextFormatting.getTextWithoutFormattingCodes(message));
					}
				});
			}
		} catch (Exception e) {

		}
	}

}