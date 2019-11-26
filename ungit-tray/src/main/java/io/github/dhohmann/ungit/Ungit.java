package io.github.dhohmann.ungit;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

/**
 * Ungit wrapper class.
 * 
 * @since 0.0.1
 * @author Daniel Hohmann
 *
 */
public class Ungit implements Runnable {

	private static String ungitCommand = null;
	private Process process;
	private static final File ungitWorkingDir;
	private static final UngitSettings settings;

	static {
		String[] paths = { "/usr/local/lib/node_modules",
				System.getProperty("user.home") + "\\AppData\\npm\\node_modules",
				System.getProperty("user.home") + "\\AppData\\Roaming\\npm\\node_modules" };
		for (String path : paths) {
			File file = new File(path);
			if (file.exists() && file.isDirectory()) {
				ungitCommand = file.getAbsolutePath() + "\\ungit\\bin\\ungit";
				if (new File(ungitCommand).exists()) {
					break;
				} else {
					ungitCommand = null;
				}
			}
		}
		if (ungitCommand == null) {
			System.err.println("ungit not found");
			System.exit(1);
		}
		ungitWorkingDir = new File(System.getProperty("user.dir"), ".ungit");
		if (!ungitWorkingDir.exists()) {
			ungitWorkingDir.mkdirs();
		}

		try {
			settings = new UngitSettings();
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Ungit settings could not be read", ex);
		}
	}

	/**
	 * Starts the ungit service thread. While a thread is running, additional calls
	 * will be ignored and a message is displayed instead.
	 */
	public void start() {
		if (this.isRunning()) {
			JOptionPane.showMessageDialog(null, "Ungit is already running");
			return;
		}
		new Thread(this, "ungit-service").start();
	}

	/**
	 * Process builder process
	 */
	@Override
	public void run() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(getExecutorCommand(), ungitCommand);
			processBuilder.redirectError(Redirect.INHERIT);
			processBuilder.redirectOutput(Redirect.INHERIT);
			this.process = processBuilder.start();
			this.process.waitFor();
			this.process = null;
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Retrieves the running state of ungit.
	 * 
	 * @return <code>true</code>, if an ungit process is already managed by Java
	 */
	public boolean isRunning() {
		return this.process != null && this.process.isAlive();
	}

	/**
	 * Stops the ungit process
	 */
	public void stop() {
		if (isRunning()) {
			this.process.destroyForcibly();
		}
	}

	/**
	 * Opens the provided URL
	 * 
	 * @param url URL to open in the browser
	 * @throws IOException        if no default browser could be found
	 * @throws URISyntaxException if the provided URL is not transferable into an
	 *                            URI
	 */
	public void open(String url) throws IOException, URISyntaxException {
		if (!this.isRunning()) {
			this.start();
		}
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI(url));
		}
	}

	private static String getExecutorCommand() {
		String executor = "node";
		return executor;
	}

	/**
	 * Retrieves the ungit version running on the local machine.
	 * 
	 * @return <code>undefined</code>, if ungit was not found or if the version
	 *         could not be determined, the version String otherwise
	 */
	public static String getVersion() {
		String version = "undefined";
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(getExecutorCommand(), ungitCommand, "--version");
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			version = builder.toString();
			if (process.isAlive()) {
				process.destroyForcibly();
			}
		} catch (Throwable e) {
			System.err.println(e.getLocalizedMessage());
		}
		return version;

	}

	/**
	 * Retrieves the ungit settings for the default location
	 * 
	 * @return Settings object
	 */
	public static UngitSettings getSettings() {
		return settings;
	}
}
