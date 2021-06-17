package de.ryuu.adventurecraft.versionchecker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VersionParser {

    public static ACVersion getLatestVersion() {
        try {
            InputStreamReader r = new InputStreamReader(new URL("").openStream());
            JsonParser jsonParser = new JsonParser();
            JsonObject ac = (JsonObject) jsonParser.parse(r);
            JsonObject versiondetails = ac.getAsJsonObject("download");
            String name = versiondetails.getAsJsonPrimitive("name").getAsString();
            ACVersion acv = new ACVersion(name);
            return acv;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}