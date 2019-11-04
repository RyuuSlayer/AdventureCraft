package de.ryuum3gum1n.adventurecraft.script.wrappers.scoreboard;

import java.util.List;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class ScoreboardCriteriaObjectWrapper implements IObjectWrapper {
	private IScoreCriteria criteria;

	public ScoreboardCriteriaObjectWrapper(IScoreCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public IScoreCriteria internal() {
		return criteria;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getName() {
		return criteria.getName();
	}

	public boolean isReadOnly() {
		return criteria.isReadOnly();
	}

	public EnumRenderType getRenderType() {
		return criteria.getRenderType();
	}

}