package de.ryuu.adventurecraft.script;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;

import java.util.List;

public class ConsoleOutput implements IObjectWrapper {

    public ConsoleOutput() {
        /* empty stub */
    }

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
        AdventureCraft.logger.info(
                object.getClass().getSimpleName() + "@" + object.hashCode() + " : " + object.getOwnPropertyNames());
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