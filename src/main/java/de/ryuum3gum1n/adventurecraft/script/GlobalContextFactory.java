package de.ryuum3gum1n.adventurecraft.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import de.ryuum3gum1n.adventurecraft.AdventureCraft;

public class GlobalContextFactory extends ContextFactory {
	@Override
	protected Context makeContext() {
		Context cx = super.makeContext();
		AdventureCraft.globalScriptManager.contextCreation(cx);
		return cx;
	}
}