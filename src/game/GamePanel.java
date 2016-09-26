package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import game.gamestate.GameStateManager;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// Dimensions
	public static final int WIDTH = 640;
	public static final int HEIGHT = 360;
	public static final int SCALE = 2;				// Scale the window up in visual size without increasing the number of pixels being processed.

	// Game thread
	private Thread thread;
	private boolean running;
	
	// Image
	private BufferedImage image;
	private Graphics2D g;
		
	// Game State Manager
	private GameStateManager gsm;
	
	// InputHandler
	public InputHandler input;
	
	// Ticks
	private long lastTime;
	private double nsPerTick;
	
	// Constructor
	public GamePanel() {
		super();						// Not sure if super() is necessary.
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));			// Multiply by SCALE to increase window size without increasing the number of pixels being drawn.
		setFocusable(true);
		requestFocusInWindow();			// Recommended over requestFocus() because it's platform independent.
	}
	
	public void addNotify() {			// Recommended to never call this method directly. Low level stuff.
		super.addNotify();
		if(thread == null) {					// Only called once.
			thread = new Thread(this);
			thread.start();
		}
	}

	private void init() {
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();				// Using a Graphics2D object instead of a Graphics object. (Graphics2D extends Graphics)
		running = true;
		
		input = new InputHandler(this);
		gsm = new GameStateManager(input);
		
	}
	
	public void run() {
		lastTime = System.nanoTime();					// Gets current time in nanoseconds.
		nsPerTick = 1000000000D / 60D;				// Gets how many nanoseconds in one tickk.
		
		int ticks = 0;										// Updates per second = ticks.
		int frames = 0;										// Used for current fps.
		
		long lastTimer = System.currentTimeMillis();		// Time to reset the data.
		double delta = 0;									// How many unprocessed nanoseconds have gone by?
		
		init();												// Initialise screen.
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;									// Updates lastTime ready for the next tick.
			boolean shouldRender = true;					// Limit the frames that can actually render.
			
			while(delta >= 1) {								// After 1/60 of a second has gone by.
				ticks++;
				tick();										// Update the tick.
				delta -= 1;									// Reset delta.
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);							// Wait so fps doesn't go too high.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(shouldRender) {
				frames++;
				render();									// Update the render and draw to screen.
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000) {		// Reset after every second
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps.");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private void tick() {
		gsm.tick();
	}
	
	private void render() {
		gsm.render(g);
		drawToScreen();
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();				// Graphics2D object of the frame/screen/panel rather than the Image object.
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);			// Stretch the image to fit the screen.
		g2.dispose();
	}

}













