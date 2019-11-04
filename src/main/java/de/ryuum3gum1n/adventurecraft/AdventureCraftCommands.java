package de.ryuum3gum1n.adventurecraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import de.ryuum3gum1n.adventurecraft.commands.AttackCommand;
import de.ryuum3gum1n.adventurecraft.commands.ButcherCommand;
import de.ryuum3gum1n.adventurecraft.commands.EditEntityCommand;
import de.ryuum3gum1n.adventurecraft.commands.FadeCommand;
import de.ryuum3gum1n.adventurecraft.commands.FileCommand;
import de.ryuum3gum1n.adventurecraft.commands.HighlightCommand;
import de.ryuum3gum1n.adventurecraft.commands.MountCommand;
import de.ryuum3gum1n.adventurecraft.commands.PlayerDataCommand;
import de.ryuum3gum1n.adventurecraft.commands.RegionTriggerCommand;
import de.ryuum3gum1n.adventurecraft.commands.RenameCommand;
import de.ryuum3gum1n.adventurecraft.commands.ScriptCommand;
import de.ryuum3gum1n.adventurecraft.commands.TargetedTeleportCommand;
import de.ryuum3gum1n.adventurecraft.commands.TriggerCommand;
import de.ryuum3gum1n.adventurecraft.commands.UndoCommand;
import de.ryuum3gum1n.adventurecraft.commands.ValidateBlockCommand;
import de.ryuum3gum1n.adventurecraft.commands.VelocityCommand;
import de.ryuum3gum1n.adventurecraft.commands.VisualizeCommand;
import de.ryuum3gum1n.adventurecraft.commands.VoxelBrushCommand;
import de.ryuum3gum1n.adventurecraft.commands.WandCommand;

public class AdventureCraftCommands {
	private static final List<CommandBase> commands = Lists.newArrayList();

	public static void init() {
		// Just add commands here and they automatically get registered!
		commands.add(new WandCommand());
		commands.add(new MountCommand());
		commands.add(new TriggerCommand());
		commands.add(new RegionTriggerCommand());
		commands.add(new VelocityCommand());
		// commands.add(new ExplosionCommand());
		// commands.add(new SwitchShaderCommand());
		commands.add(new VoxelBrushCommand());
		commands.add(new ButcherCommand());
		commands.add(new ScriptCommand());
		commands.add(new VisualizeCommand());
		commands.add(new ValidateBlockCommand());
		commands.add(new EditEntityCommand());
		commands.add(new AttackCommand());
		commands.add(new TargetedTeleportCommand());
		commands.add(new HighlightCommand());
		commands.add(new FileCommand());
		commands.add(new FadeCommand());
		commands.add(new RenameCommand());
		commands.add(new PlayerDataCommand());
		commands.add(new UndoCommand());
	}

	public static void register(CommandHandler registry) {
		for (ICommand cmd : commands) {
			registry.registerCommand(cmd);
		}
	}

	public static Collection<? extends CommandBase> getCommandList() {
		return Collections.unmodifiableList(commands);
	}
}
