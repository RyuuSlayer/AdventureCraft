package de.ryuu.adventurecraft.commands;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.invoke.CommandSenderInvokeSource;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.invoke.FileScriptInvoke;
import de.ryuu.adventurecraft.invoke.Invoke;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.util.PlayerHelper;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class ScriptCommand extends ACCommandBase {

    @Override
    public String getName() {
        return "ac_script";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "<[?]> (use tab completion)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException("Not enough parameters for meaningful action. (" + args.length + ")");
        }

        if (sender.getCommandSenderEntity() == null) {
            throw new WrongUsageException("ICommandSender does not have a entity assigned! Bug?");
        }
        boolean cmd = sender instanceof BlockCommandBlock;
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerMP || cmd)) {
            throw new WrongUsageException("This command can only be executed by a opped player or command block.");
        }
        EntityPlayerMP player = null;
        if (!cmd) {
            player = (EntityPlayerMP) sender.getCommandSenderEntity();

            if (!PlayerHelper.isOp(player)) {
                throw new WrongUsageException("This command can only be executed by a opped player.");
            }
        }

        if (args[0].equals("run")) {
            if (args.length == 2) {
                // Runs a script
                String script = args[1];

                if (script != null && !script.isEmpty()) {
                    Invoke.invoke(new FileScriptInvoke(script), new CommandSenderInvokeSource(sender), null,
                            EnumTriggerState.ON);
                }
            } else {
                throw new WrongUsageException("Wrong parameter count: /ac_script run <scriptname>");
            }
        }

        if (args[0].equals("edit")) {
            if (cmd)
                throw new WrongUsageException("Edit can only be run by a player.");
            if (args.length == 2) {
                // Get Script name
                String fileName = args[1];

                // Load Script
                String fileContent = AdventureCraft.globalScriptManager.loadScript(sender.getEntityWorld(), fileName);

                // Send Command-Packet with script to sender!
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("scriptname", fileName);
                nbt.setString("script", fileContent);
                AdventureCraft.network.sendTo(new StringNBTCommandPacket("script_edit", nbt), player);
            } else {
                throw new WrongUsageException("Wrong parameter count: /ac_script edit <scriptname>");
            }
        }

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 0) {
            return getListOfStringsMatchingLastWord(args, new String[]{"run", "edit"});
        }

        return Collections.emptyList();
    }

}