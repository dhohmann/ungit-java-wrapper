package io.github.dhohmann.ungit;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import io.github.dhohmann.ungit.app.AboutDialog;
import io.github.dhohmann.ungit.app.AppIcon;

/**
 * Main class. Creates the UI part and prepares the ungit processes.
 * 
 * @since 0.0.1
 * @author Daniel Hohmann
 *
 */
public class UngitProcess {

	private static final String VERSION = "${project.version}";
	
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
		final AppIcon appIcon = new AppIcon("ungit.png");
		final TrayIcon trayIcon = new TrayIcon(appIcon.getImage());
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

		trayIcon.setToolTip("Wrapper v"+VERSION);
		trayIcon.setPopupMenu(popup);
		trayIcon.setImageAutoSize(true);
		
		trayIcon.addMouseListener(new MouseListener() {
			
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			int clicked = 0;
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
//					try {
//						ungit.open(Ungit.getSettings().getURLBase() + ":" + Ungit.getSettings().getPort());
//						
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					} catch (URISyntaxException e1) {
//						e1.printStackTrace();
//					}
					clicked++;
					appIcon.setState(clicked % 4, Integer.toString(clicked));
					trayIcon.setImage(appIcon.getImage());
				}
				
			}
		});

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
				dialog = new AboutDialog();
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						dialog = null;
					}
				});
				
//				JPanel processIndicator = new JPanel();
//				processIndicator.setLayout(new GridBagLayout());
//				JProgressBar processbar = new JProgressBar();
//				processbar.setIndeterminate(true);
//				processIndicator.add(processbar, new GridBagConstraints());
//				dialog.getContentPane().add(processIndicator);
//				dialog.setSize(new Dimension(300, 200));
//				dialog.setLocationRelativeTo(null);
//				
//				java.awt.EventQueue.invokeLater(()->{
//					dialog.setVisible(true);
//				});
//				
//				
//
//				ExecutorService service = Executors.newSingleThreadExecutor();
//				service.execute(() -> {
//					JEditorPane jEditorPane = new JEditorPane();
//					jEditorPane.setEditable(false);
//					JScrollPane scrollPane = new JScrollPane(jEditorPane);
//					HTMLEditorKit kit = new HTMLEditorKit();
//					jEditorPane.setEditorKit(kit);
//					StyleSheet styleSheet = kit.getStyleSheet();
//					styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
//					styleSheet.addRule("h1 {color: blue;}");
//					styleSheet.addRule("h2 {color: #ff0000;}");
//					styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");
//
//					StringBuilder html = new StringBuilder("<html>");
//					html.append("<div>Ungit is running on version ").append(Ungit.getVersion()).append("</div");
//					html.append("<div>").append("<h2>License Information</h2>");
//					html.append("<table>");
//					html.append("<tr><th>Library/Framework</th><th>Licence</th></tr>");
//					html.append(
//							"<tr><td>org.json</td><td><a href=\"https://github.com/stleary/JSON-java/blob/master/LICENSE\">See here</a></td></tr>");
//					html.append("</table>");
//					html.append("</div>");
//					html.append("</html>");
//
//					Document doc = kit.createDefaultDocument();
//					jEditorPane.setDocument(doc);
//					jEditorPane.setText(html.toString());
//					jEditorPane.addHyperlinkListener(new HyperlinkListener() {
//
//						@Override
//						public void hyperlinkUpdate(HyperlinkEvent e) {
//							try {
//								if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
//									Desktop.getDesktop().browse(e.getURL().toURI());
//								}
//							} catch (IOException | URISyntaxException e1) {
//								e1.printStackTrace();
//							}
//
//						}
//					});
//					dialog.getContentPane().remove(processIndicator);
//					dialog.getContentPane().add(scrollPane);
//					dialog.getContentPane().repaint();
//					dialog.revalidate();
//				});
//				
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
			BufferedImage image;
			Graphics graphics;
			try {
				image = ImageIO.read(imageURL);
				graphics = image.getGraphics();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			graphics.setColor(Color.GREEN);
			graphics.fillOval(150, 150, 50, 50);
			return image;
		}
	}

}
