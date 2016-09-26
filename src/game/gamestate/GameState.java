package game.gamestate;

import java.awt.Graphics2D;

import game.gamestate.GameStateManager;

public abstract class GameState {

	protected GameStateManager gsm;			// Needs a reference to the gsm so it can change states
	
	public abstract void init();
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
}