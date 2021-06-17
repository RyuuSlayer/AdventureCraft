package de.ryuu.adventurecraft.client.gui.replaced_guis.map.download;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DownloadableMap {
    public final boolean hasScripts;
    public final List<DownloadableMod> additionalMods = new ArrayList<>();
    public final String mapUUIDs = "-";
    @SuppressWarnings("UnusedAssignment")
    public String name = "Unknown";
    public String description = "Unknown";
    public String author = "Unknown";
    public URL dlURL = null;
    public boolean additionalModsRequired = false;
    public String mapURL = "";
    public String mapVersion = "";

    public DownloadableMap(Entry<String, JsonElement> mapJson) {
        // TODO Auto-generated constructor stub
        this.name = mapJson.getKey();
        final JsonObject mapobj = mapJson.getValue().getAsJsonObject();
        if (mapobj.has("desc"))
            this.description = mapobj.get("desc").getAsString();
        if (mapobj.has("author")) {
            this.author = mapobj.get("author").getAsString();

        }
        if (mapobj.has("mapurl"))
            this.mapURL = mapobj.get("mapurl").getAsString();
        this.hasScripts = (mapobj.has("scripts") && mapobj.get("scripts").getAsBoolean());
        if (mapobj.has("ac-version"))
            this.mapVersion = mapobj.get("ac-version").getAsString();
        if (mapobj.has("dlurl"))
            try {
                this.dlURL = new URL(mapobj.get("dlurl").getAsString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        if (mapobj.has("additionalMods") && !mapobj.get("additionalMods").isJsonNull()) {
            mapobj.get("additionalMods").getAsJsonObject().entrySet().forEach(mod -> {
                // TODO Auto-generated method stub
                DownloadableMap.this.additionalMods.add(new DownloadableMod(mod));
            });
            this.additionalModsRequired = true;
        }
    }
}
