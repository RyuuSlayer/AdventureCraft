package de.ryuu.adventurecraft.versionchecker;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.Reference;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class SendMessage {

    @SubscribeEvent
    public void onClientConnection(ClientConnectedToServerEvent event) {
        try {
            final ACVersion latest = VersionParser.getLatestVersion();
            final String current = Reference.MOD_VERSION; // to be easily changeable for debugging
            if (latest.isGreaterVersion(current)) {
                final ClientProxy cproxy = AdventureCraft.proxy.asClient();
                cproxy.scheduleClientTickTask(new Runnable() {
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