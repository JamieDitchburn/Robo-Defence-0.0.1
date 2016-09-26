package game.gamestate;

import java.awt.Graphics2D;

import game.InputHandler;

public class GameStateManager {
	
	// Input
	private InputHandler input;
	
	// States
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES =  3;
	public static final int MENUSTATE = 0;
	public static final int LEVELSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public GameStateManager(InputHandler input) {
		this.input = input;
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
		
	}
	
	private void loadState(int state) {
		if (state == MENUSTATE) {
			gameStates[state] = new MenuState(this, input);
		}
		if (state == LEVELSTATE) {
			gameStates[state] = new LevelState(this, input);
		}
		if (state == GAMEOVERSTATE) {
			gameStates[state] = new GameOverState(this, input);
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
//		gameStates[currentState].init();
	}
	
	public void tick() {
		if (gameStates[currentState] != null) gameStates[currentState].tick();
		
	}
	
	public void render(Graphics2D g) {
		if (gameStates[currentState] != null) gameStates[currentState].render(g);
	}
	
}