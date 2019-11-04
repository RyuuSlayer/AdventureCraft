package de.ryuum3gum1n.adventurecraft.script.wrappers.block;

import java.util.List;

import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;
import de.ryuum3gum1n.adventurecraft.util.WorkbenchManager;

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