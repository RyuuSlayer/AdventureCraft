package de.ryuum3gum1n.adventurecraft.versionchecker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VersionParser {

	public static ACVersion getLatestVersion() {
		try {
			InputStreamReader r = new InputStreamReader(new URL("").openStream());
			JsonParser jsonParser = new JsonParser();
			JsonObject tc = (JsonObject) jsonParser.parse(r);
			JsonObject versiondetails = tc.getAsJsonObject("download");
			String name = versiondetails.getAsJsonPrimitive("name").getAsString();
			ACVersion tcv = new ACVersion(name);
			return tcv;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}