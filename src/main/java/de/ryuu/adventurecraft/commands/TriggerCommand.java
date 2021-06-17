package de.ryuu.adventurecraft.commands;

import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.invoke.Invoke;
import de.ryuu.adventurecraft.util.CommandArgumentParser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TriggerCommand extends ACCommandBase {

    @Override
    public String getName() {
        return "ac_triggerblock";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "<x> <y> <z>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 3) {
            throw new CommandException("Wrong number of parameters: " + args.length + " given, 3 needed.");
        }

        CommandArgumentParser parser = new CommandArgumentParser(args);
        parser.commandSenderPosition = sender.getPositionVector();
        BlockPos triggerPos = parser.consume_blockpos("Failed to parse block position: " + Arrays.toString(args));

        World world = sender.getEntityWorld();
        IBlockState state = world.getBlockState(triggerPos);
        Invoke.trigger(world, triggerPos, state, EnumTriggerState.ON);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 0) {
            return getListOfStringsMatchingLastWord(args, new String[]{"~", "0"});
        }
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, new String[]{"~", "0"});
        }
        if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, new String[]{"~", "0"});
        }

        return Collections.emptyList();
    }

}