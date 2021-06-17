package de.ryuu.adventurecraft.client.gui.replaced_guis.map.download;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DownloadableMod {
    @SuppressWarnings("UnusedAssignment")
    public String name = "Unknown";
    public String description = "Unknown";
    public List<String> authors = new ArrayList<String>() {
        {
            add("Unknown");
        }
    };
    public URL dlURL = null;
    public URL modURL = null;

    public DownloadableMod(Entry<String, JsonElement> modJson) {
        // TODO Auto-generated constructor stub
        this.name = modJson.getKey();
        final JsonObject modobj = modJson.getValue().getAsJsonObject();
        if (modobj.has("description"))
            this.description = modobj.get("description").getAsString();
        if (modobj.has("authors")) {
            this.authors = new ArrayList<>();
            modobj.get("authors").getAsJsonArray().forEach(t -> DownloadableMod.this.authors.add(t.getAsString()));
        }
        if (modobj.has("url"))
            try {
                this.modURL = new URL(modobj.get("url").getAsString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        if (modobj.has("dlurl"))
            try {
                this.dlURL = new URL(modobj.get("dlurl").getAsString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
