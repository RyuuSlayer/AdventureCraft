package de.ryuu.adventurecraft.script.wrappers.scoreboard;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;

import java.util.List;

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