package game.entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean hasPlayedOnce;		// The animation has played once.
	private boolean shouldPlayOnce;		// The animation should play once. Stop when hasPlayedOnce is true.
	
	public void setFrames(BufferedImage[] frames, boolean shouldPlayOnce) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		this.shouldPlayOnce = shouldPlayOnce;
		hasPlayedOnce = false;
	}
	
	public void setDelay(long d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }
	
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	
	public boolean hasPlayedOnce() { return hasPlayedOnce; }
	
	public void tick() {
		if (delay == -1	) return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;			// in milliseconds
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.length) {
			if (shouldPlayOnce) currentFrame--;		// Once animation has completed don't render the first frame again.
			else currentFrame = 0;
			hasPlayedOnce = true;
		}
	}
	
}
