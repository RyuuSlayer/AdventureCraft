package de.ryuum3gum1n.adventurecraft.script.wrappers.scoreboard;

import java.util.List;

import net.minecraft.scoreboard.Score;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class ScoreObjectWrapper implements IObjectWrapper {
	private Score score;

	public ScoreObjectWrapper(Score score) {
		this.score = score;
	}

	@Override
	public Score internal() {
		return score;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getHolderName() {
		return score.getPlayerName();
	}

	public int getScore() {
		return score.getScorePoints();
	}

	public void setScore(int value) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.setScorePoints(value);
	}

	public void increaseScore(int amount) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.increaseScore(amount);
	}

	public void decreaseScore(int amount) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.decreaseScore(amount);
	}
	
	public boolean isReadOnly(){
		return score.getObjective().getCriteria().isReadOnly();
	}
}