package io.github.dhohmann.unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

public class UngitSettings {

	private static final String PORT = "port";
	private static final String URLBASE = "urlBase";

	private final File file;
	private final JSONObject settings;

	public UngitSettings() throws FileNotFoundException {
		file = new File(System.getProperty("user.home"), ".ungitrc");
		if (!file.exists()) {
			throw new FileNotFoundException("Ungit configuration file "+file.getAbsolutePath()+" does not exist");
		}
		settings = new JSONObject(new JSONTokener(new FileInputStream(file)));
		applyDefaults();
	}


	private void applyDefaults() {
		InputStream stream = UngitSettings.class.getResourceAsStream("ungit-defaults.json");
		JSONObject defaults = new JSONObject(new JSONTokener(stream));
		for(String key: defaults.keySet()) {
			if(!settings.has(key)) {
				settings.put(key, defaults.get(key));
			}
		}
	}


	public int getPort() {
		return settings.getInt(PORT);
	}
	public String getURLBase() {
		return settings.getString(URLBASE);
	}

}
