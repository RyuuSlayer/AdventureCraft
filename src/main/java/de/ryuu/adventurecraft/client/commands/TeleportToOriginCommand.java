package de.ryuu.adventurecraft.client.commands;

import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class TeleportToOriginCommand extends CommandBase {
    @Override
    public String getName() {
        return "acc_tpo";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ClientProxy.schedule(new Runnable() {
            @Override
            public void run() {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player != null) {
                    player.sendChatMessage("/tp 0 255 0");
                }
            }
        });
    }

}