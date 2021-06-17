package de.ryuu.adventurecraft.script.wrappers;

import java.util.List;

public interface IObjectWrapper {

    public Object internal();

    public List<String> getOwnPropertyNames();

}