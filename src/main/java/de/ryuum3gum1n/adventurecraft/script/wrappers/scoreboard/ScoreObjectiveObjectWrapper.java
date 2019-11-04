package de.ryuum3gum1n.adventurecraft.script.wrappers.scoreboard;

import java.util.List;

import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.ScoreObjective;

public class ScoreObjectiveObjectWrapper implements IObjectWrapper {
	private ScoreObjective objective;

	public ScoreObjectiveObjectWrapper(ScoreObjective objective) {
		this.objective = objective;
	}

	@Override
	public ScoreObjective internal() {
		return objective;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getName() {
		return objective.getName();
	}

	public String getDisplayName() {
		return objective.getName();
	}

	public EnumRenderType getRenderType() {
		return objective.getRenderType();
	}

	public ScoreObjectWrapper getValue(String name) {
		return new ScoreObjectWrapper(objective.getScoreboard().getOrCreateScore(name, objective));
	}

	public ScoreboardCriteriaObjectWrapper getCriteria() {
		return new ScoreboardCriteriaObjectWrapper(objective.getCriteria());
	}

	public ScoreboardObjectWrapper getScoreboard() {
		return new ScoreboardObjectWrapper(objective.getScoreboard());
	}

}