package de.ryuum3gum1n.adventurecraft.script.wrappers.world;

import java.util.List;

import net.minecraft.world.GameRules;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class GameRulesObjectWrapper implements IObjectWrapper {
	private GameRules rules;

	public GameRulesObjectWrapper(GameRules gameRules) {
		this.rules = gameRules;
	}

	@Override
	public GameRules internal() {
		return rules;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public boolean hasRule(String name) {
		return rules.hasRule(name);
	}

	public String getRule(String name) {
		return rules.getString(name);
	}

	public int getRuleInt(String name) {
		return rules.getInt(name);
	}

	public boolean getRuleBool(String name) {
		return rules.getBoolean(name);
	}

	public void setRule(String name, String value) {
		rules.setOrCreateGameRule(name, value);
	}

	public String[] getRules() {
		return rules.getRules();
	}

}