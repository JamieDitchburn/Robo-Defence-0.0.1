package game.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.GamePanel;
import game.InputHandler;

public class GameOverState extends GameState {

	// Input
	private InputHandler input;
	
	// Font
	private Font font;
	
	public GameOverState(GameStateManager gsm, InputHandler input) {
		
		this.gsm = gsm;
		this.input = input;
		
		init();
	}
	
	public void init() {
		font = new Font("Century Gothic", Font.PLAIN, 50);
	}

	public void tick() {
		
	}

	public void render(Graphics2D g) {
		
		// Game Over Text
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString("Game Over!", (int)(GamePanel.WIDTH / 2) - 140, (int)(GamePanel.HEIGHT * 0.4));
		
	}

}
