package game.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import game.GamePanel;

public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	
	public Background(String s, double ms) {
		
		try {			// Import resource files in to the game
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;			// Mod so if the image goes past the screen we want it to reset
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {			// If we want the background to automatically scroll.
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {				// Necessary if we were to scroll the background.
		x += dx;
		y += dy;
		x %= GamePanel.WIDTH;			// Mod so that we only need the two bg images being drawn and just swap between them.
		y %= GamePanel.HEIGHT;
	}
	
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		if (x < 0) {
			g.drawImage(image,  (int) x + GamePanel.WIDTH, (int) y, null);			// As the bg image is scrolling we need to draw an extra image as it starts to go off the screen.
		}
		if (x > 0) {
			g.drawImage(image,  (int) x - GamePanel.WIDTH, (int) y, null);
		}
	}
	
	
}