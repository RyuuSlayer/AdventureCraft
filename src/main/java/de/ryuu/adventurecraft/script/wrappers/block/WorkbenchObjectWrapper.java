package de.ryuu.adventurecraft.script.wrappers.block;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import de.ryuu.adventurecraft.util.WorkbenchManager;

import java.util.List;

public class WorkbenchObjectWrapper implements IObjectWrapper {

    private WorkbenchManager manager;

    public WorkbenchObjectWrapper(WorkbenchManager manager) {
        this.manager = manager;
    }

    @Override
    public Object internal() {
        return manager;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public void removeRecipe(int index) {
        manager.remove(index);
    }

}