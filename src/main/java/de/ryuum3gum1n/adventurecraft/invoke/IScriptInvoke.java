package de.ryuum3gum1n.adventurecraft.invoke;

public interface IScriptInvoke extends IInvoke {
	public String getScriptName();
	public String getScript();
	public void reloadScript();
}