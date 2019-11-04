package de.ryuum3gum1n.adventurecraft.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public final class InfobarCommand extends CommandBase {
	@Override
	public String getName() {
		return "acc_infobar";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<true/false>";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			return;
		}

		boolean flag = CommandBase.parseBoolean(args[0]);
		ClientProxy.settings.setBoolean("client.infobar.enabled", flag);
	}
}