package io.github.dhohmann.ungit.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import io.github.dhohmann.ungit.UngitProcess;

public class AppIcon {

	public static final int NEUTRAL = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	public static final int CRITICAL = 3;

	private BufferedImage image;
	private Graphics graphics;
	private int hOffset;
	private int vOffset;

	public AppIcon(String path) {
		URL imageURL = UngitProcess.class.getResource(path);
		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
		} else {
			try {
				image = ImageIO.read(imageURL);
				hOffset = (int) (image.getHeight() * 0.75);
				vOffset = (int) (image.getWidth() * 0.75);
				graphics = image.getGraphics();
				drawNetral(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setState(int state) {
		setState(state, "");
	}

	public void setState(int state, String message) {
		switch (state) {
		case CRITICAL:
			drawCritical(message);
			break;
		case ERROR:
			drawError(message);
			break;
		case SUCCESS:
			drawSuccess(message);
			break;
		case NEUTRAL:
		default:
			drawNetral(message);
			break;
		}
		drawText(message);
	}

	private void drawNetral(String message) {
		if (graphics == null) {
			return;
		}
		drawIndicator(Color.LIGHT_GRAY);

	}

	private void drawCritical(String message) {
		if (graphics == null) {
			return;
		}
		drawIndicator(Color.YELLOW);
	}

	private void drawSuccess(String message) {
		if (graphics == null) {
			return;
		}
		drawIndicator(Color.GREEN);
	}

	private void drawError(String message) {
		if (graphics == null) {
			return;
		}
		drawIndicator(Color.RED);
	}

	private void drawIndicator(Color color) {
		graphics.setColor(color);
		graphics.fillOval(image.getWidth() - hOffset, image.getHeight() - vOffset, hOffset, vOffset);

	}

	private void drawText(String message) {
		if (message == null) {
			message = "";
		}
		int padding = 10;
		Font font = new Font("Sans Serif", Font.PLAIN, vOffset - (padding * message.length()));
		graphics.setColor(Color.BLACK);
		graphics.setFont(font);
		graphics.drawString(message, image.getWidth() - hOffset, image.getHeight() - 10);
	}

	public Image getImage() {
		return image;
	}

}
