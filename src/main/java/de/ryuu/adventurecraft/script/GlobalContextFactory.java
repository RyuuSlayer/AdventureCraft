package de.ryuu.adventurecraft.script;

import de.ryuu.adventurecraft.AdventureCraft;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class GlobalContextFactory extends ContextFactory {
    @Override
    protected Context makeContext() {
        Context cx = super.makeContext();
        AdventureCraft.globalScriptManager.contextCreation(cx);
        return cx;
    }
}