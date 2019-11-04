package de.ryuum3gum1n.adventurecraft.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public final class PasteSnapCommand extends CommandBase {
	@Override
	public String getName() {
		return "acc_pastesnap";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<0..64>";
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

		int snap = CommandBase.parseInt(args[0], 0, 64) + 1;
		ClientProxy.settings.setInteger("item.paste.snap", snap);
		ClientProxy.settings.send();
	}
}