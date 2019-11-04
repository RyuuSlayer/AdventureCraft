package de.ryuum3gum1n.adventurecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuum3gum1n.adventurecraft.server.ServerFileSystem;
import de.ryuum3gum1n.adventurecraft.server.ServerMirror;
import de.ryuum3gum1n.adventurecraft.util.Either;

public class FileCommand extends ACCommandBase {

	@Override
	public String getName() {
		return "ac_file";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<action> <directory|   >";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!(sender instanceof EntityPlayerMP)) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED+"This command can only be used by players."));
			return;
		}

		ServerFileSystem fileSystem = ServerMirror.instance().getFileSystem();

		if(args.length == 0) {
			throw new SyntaxErrorException("No arguments given; /tc_file <action> <path>");
		}

		String action = args[0];

		if(action.equalsIgnoreCase("open")) {
			String path = args.length > 1 ? args[1] : "/";
			path = path.replace("%20", " ");

			NBTTagCompound data = null;
			Either<NBTTagCompound, String> either = fileSystem.getFileData(path, true);
			if(either.issetA()) {
				data = either.getA();
			} else {
				data = new NBTTagCompound();
				data.setString("error", either.getB());
			}

			String type = data.getString("type");

			if(type.equalsIgnoreCase("file")) {
				StringNBTCommandPacket command = new StringNBTCommandPacket("client.gui.file.edit", data);
				AdventureCraft.network.sendTo(command, (EntityPlayerMP) sender);
			} else if(type.equalsIgnoreCase("dir")) {
				StringNBTCommandPacket command = new StringNBTCommandPacket("client.gui.file.browse", data);
				AdventureCraft.network.sendTo(command, (EntityPlayerMP) sender);
			} else {
				// ...?
			}
		} else {
			// ...?
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return Collections.emptyList();
	}

}