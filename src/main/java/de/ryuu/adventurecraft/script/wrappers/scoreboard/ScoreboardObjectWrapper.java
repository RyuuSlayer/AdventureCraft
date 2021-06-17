package de.ryuu.adventurecraft.script.wrappers.scoreboard;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import java.util.List;

public class ScoreboardObjectWrapper implements IObjectWrapper {
    private Scoreboard scoreboard;

    public ScoreboardObjectWrapper(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;

    }

    @Override
    public Scoreboard internal() {
        return scoreboard;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public void addPlayerToTeam(String entityName, String teamName) {
        scoreboard.addPlayerToTeam(entityName, teamName);
    }

    public ScoreObjectWrapper getScore(String entityName, String objectiveName) {
        ScoreObjective objective = scoreboard.getObjective(objectiveName);
        return new ScoreObjectWrapper(scoreboard.getOrCreateScore(entityName, objective));
    }

    public ScoreObjectiveObjectWrapper getObjective(String objectiveName) {
        return new ScoreObjectiveObjectWrapper(scoreboard.getObjective(objectiveName));
    }

    // TODO: Add a way to create new objectives.
    // TODO: Add a way to remove objectives.

}