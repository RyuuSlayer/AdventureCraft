package de.ryuu.adventurecraft.commands;

import de.ryuu.adventurecraft.util.GObjectTypeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;

public class ValidateBlockCommand extends ACCommandBase {

    @Override
    public String getName() {
        return "ac_isblock";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "<block>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("Wrong number of parameters: " + args.length + " given, 1 needed.");
        }

        IBlockState state = GObjectTypeHelper.findBlockState(args[0]);

        if (state == null) {
            throw new CommandException("Block type does not exist: " + args[0]);
        }

        sender.sendMessage(new TextComponentString("Block type exists: " + args[0]));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 0) {
            return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        }

        return Collections.emptyList();
    }

}