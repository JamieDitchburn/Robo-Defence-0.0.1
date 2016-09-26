package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	// Handle all key inputs here rather than in main class.
	public InputHandler(GamePanel game) {
		game.addKeyListener(this);	
	}
	
	public class Key {
		private int numTimesPressed = 0;		// Not sure if going to be used. Used for things like double-jumping etc. 
		private boolean pressed = false;
		
		public int getNumTimesPressed() {		// Not used yet.
			return numTimesPressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {	
			pressed = isPressed;
			if(pressed) numTimesPressed++;
		}
	}
	
	// Initialise Key objects for each action available to the player.
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key jump = new Key();
	public Key enter = new Key();
	public Key action = new Key();
	public Key despawn = new Key();
	public Key robot = new Key();
	public Key upgrade = new Key();
	public Key monies = new Key();
	
	// Set different key actions. Possibility for rebindable keys in the future.
	public void toggleKey(int keyCode, boolean isPressed) {	
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) { up.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) { left.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) { down.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) { right.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_SPACE) { jump.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_ENTER) { enter.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_E) { action.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_F) { despawn.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_R) { robot.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_U) { upgrade.toggle(isPressed); }
		if(keyCode == KeyEvent.VK_INSERT) { monies.toggle(isPressed); }
	}
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) { }

}
