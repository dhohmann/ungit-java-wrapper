package io.github.dhohmann.unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Settings for ungit wrapped inside a Java class
 * 
 * @since 0.0.1
 * @author Daniel Hohmann
 */
public class UngitSettings {

	private static final String PORT = "port";
	private static final String URLBASE = "urlBase";

	private final JSONObject settings;

	/**
	 * Constructs a new settings object using the ungit configuration file.
	 * 
	 * @throws FileNotFoundException if the configuration file is not available at
	 *                               the user home directory.
	 * @throws JSONException         if the file does not contain valid JSON
	 */
	public UngitSettings() throws FileNotFoundException, JSONException {
		this(new File(System.getProperty("user.home"), ".ungitrc"));
	}

	/**
	 * Constructs a new settings object using the specified configuration file.
	 * 
	 * @param file File to read the settings from
	 * @throws FileNotFoundException if the file is not available or a directory
	 * @throws JSONException         if the file does not contain valid JSON
	 */
	public UngitSettings(File file) throws FileNotFoundException, JSONException {
		if (file.isDirectory()) {
			throw new FileNotFoundException("File is a directory");
		}
		if (!file.exists()) {
			throw new FileNotFoundException("Ungit configuration file " + file.getAbsolutePath() + " does not exist");
		}
		settings = new JSONObject(new JSONTokener(new FileInputStream(file)));
		applyDefaults();
	}

	private void applyDefaults() {
		InputStream stream = UngitSettings.class.getResourceAsStream("ungit-defaults.json");
		JSONObject defaults = new JSONObject(new JSONTokener(stream));
		for (String key : defaults.keySet()) {
			if (!settings.has(key)) {
				settings.put(key, defaults.get(key));
			}
		}
	}

	/**
	 * Retrieves the port specified in the settings
	 * 
	 * @return Port the ungit server is running at
	 */
	public int getPort() {
		return settings.getInt(PORT);
	}

	/**
	 * Retrieves the url base string
	 * 
	 * @return Url base setting
	 */
	public String getURLBase() {
		return settings.getString(URLBASE);
	}

}
