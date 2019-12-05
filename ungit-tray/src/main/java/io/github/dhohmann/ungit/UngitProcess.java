package io.github.dhohmann.ungit;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Main class. Creates the UI part and prepares the ungit processes.
 * 
 * @since 0.0.1
 * @author Daniel Hohmann
 *
 */
public class UngitProcess {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	private static void createAndShowGUI() {
		final Ungit ungit = new Ungit();
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("ungit.png", "tray icon"));
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem aboutItem = new MenuItem("About");
		MenuItem openItem = new MenuItem("Open in browser");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem settingsItem = new MenuItem("Settings");

		// Add components to popup menu
		popup.add(aboutItem);
		popup.add(settingsItem);
		popup.addSeparator();
		popup.add(openItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		trayIcon.setImageAutoSize(true);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		aboutItem.addActionListener(new ActionListener() {
			private JDialog dialog;

			public void actionPerformed(ActionEvent e) {
				if (dialog != null) {
					java.awt.EventQueue.invokeLater(() -> {
						dialog.toFront();
						dialog.repaint();
					});
					return;
				}
				dialog = new JDialog((JFrame) null, "About");
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						dialog = null;
					}
				});
				
				JPanel processIndicator = new JPanel();
				processIndicator.setLayout(new GridBagLayout());
				JProgressBar processbar = new JProgressBar();
				processbar.setIndeterminate(true);
				processIndicator.add(processbar, new GridBagConstraints());
				dialog.getContentPane().add(processIndicator);
				dialog.setSize(new Dimension(300, 200));
				dialog.setLocationRelativeTo(null);
				
				java.awt.EventQueue.invokeLater(()->{
					dialog.setVisible(true);
				});
				
				

				ExecutorService service = Executors.newSingleThreadExecutor();
				service.execute(() -> {
					JEditorPane jEditorPane = new JEditorPane();
					jEditorPane.setEditable(false);
					JScrollPane scrollPane = new JScrollPane(jEditorPane);
					HTMLEditorKit kit = new HTMLEditorKit();
					jEditorPane.setEditorKit(kit);
					StyleSheet styleSheet = kit.getStyleSheet();
					styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
					styleSheet.addRule("h1 {color: blue;}");
					styleSheet.addRule("h2 {color: #ff0000;}");
					styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

					StringBuilder html = new StringBuilder("<html>");
					html.append("<div>Ungit is running on version ").append(Ungit.getVersion()).append("</div");
					html.append("<div>").append("<h2>License Information</h2>");
					html.append("<table>");
					html.append("<tr><th>Library/Framework</th><th>Licence</th></tr>");
					html.append(
							"<tr><td>org.json</td><td><a href=\"https://github.com/stleary/JSON-java/blob/master/LICENSE\">See here</a></td></tr>");
					html.append("</table>");
					html.append("</div>");
					html.append("</html>");

					Document doc = kit.createDefaultDocument();
					jEditorPane.setDocument(doc);
					jEditorPane.setText(html.toString());
					jEditorPane.addHyperlinkListener(new HyperlinkListener() {

						@Override
						public void hyperlinkUpdate(HyperlinkEvent e) {
							try {
								if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
									Desktop.getDesktop().browse(e.getURL().toURI());
								}
							} catch (IOException | URISyntaxException e1) {
								e1.printStackTrace();
							}

						}
					});
					dialog.getContentPane().remove(processIndicator);
					dialog.getContentPane().add(scrollPane);
					dialog.getContentPane().repaint();
					dialog.revalidate();
				});
				
			}
		});

		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UngitSettings settings = Ungit.getSettings();
					ungit.open(settings.getURLBase() + ":" + settings.getPort());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				ungit.stop();
				System.exit(0);
			}
		});

		settingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO show settings in editor mode
				JOptionPane.showMessageDialog(null, "Settings manipulation is currently not supported");
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				ungit.stop();
			}
		}));
	}

	/**
	 * Retrieves an image in the class path
	 * 
	 * @param path        Path including the file name
	 * @param description Description for the image
	 * @return Image found in the class path or <code>null</code>, if the file was
	 *         not found
	 */
	protected static Image createImage(String path, String description) {
		URL imageURL = UngitProcess.class.getResource(path);
		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}

}
