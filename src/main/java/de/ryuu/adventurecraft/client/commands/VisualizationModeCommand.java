package de.ryuu.adventurecraft.client.commands;

import de.ryuu.adventurecraft.client.ClientRenderer.VisualMode;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class VisualizationModeCommand extends CommandBase {
    @Override
    public String getName() {
        return "acc_vismode";
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
        final VisualMode mode = VisualMode.valueOf(args[0]);

        ClientProxy.schedule(new Runnable() {
            @Override
            public void run() {
                ClientProxy.proxy.getRenderer().setVisualizationMode(mode);
            }
        });
    }
}
