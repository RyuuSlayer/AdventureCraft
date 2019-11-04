package de.ryuum3gum1n.adventurecraft.script;

import java.util.List;

import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class ConsoleOutput implements IObjectWrapper {

	public ConsoleOutput() {/*empty stub*/}

	public void println(String string) {
		AdventureCraft.logger.info(string);
	}

	public void println(Object object) {
		AdventureCraft.logger.info(object);
	}

	public void println(Throwable e) {
		AdventureCraft.logger.info(e);
	}

	public void println(IObjectWrapper object) {
		AdventureCraft.logger.info(object.getClass().getSimpleName() + "@" + object.hashCode() + " : " + object.getOwnPropertyNames());
	}

	@Override
	public Object internal() {
		return null;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

}