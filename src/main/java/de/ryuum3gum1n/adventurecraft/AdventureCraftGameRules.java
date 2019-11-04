package de.ryuum3gum1n.adventurecraft;

import net.minecraft.world.GameRules;

public class AdventureCraftGameRules {
	public static void registerGameRules(GameRules rules){
		rules.addGameRule("ac_playDefaultMusic", "true", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disableInvokeSystem", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_visualEventDebugging", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disableTeleporter", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disableWeather", "false", GameRules.ValueType.BOOLEAN_VALUE);
		
		rules.addGameRule("ac_disable.damage.*", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.fall", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.drown", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.lava", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.magic", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.fire", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("ac_disable.damage.suffocate", "false", GameRules.ValueType.BOOLEAN_VALUE);
	}
}
