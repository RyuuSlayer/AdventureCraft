package de.ryuum3gum1n.adventurecraft.script.wrappers;

import java.util.List;

public interface IObjectWrapper {

	public Object internal();
	public List<String> getOwnPropertyNames();

}