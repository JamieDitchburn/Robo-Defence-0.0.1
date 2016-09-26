package game.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.Game;
import game.GamePanel;
import game.InputHandler;
import game.gfx.Background;

public class MenuState extends GameState {

	// Background
	private Background bg;
	
	// Input
	private InputHandler input;
	private boolean buttonPress = false;
	
	// Menu choices
	private int currentChoice = 0;
	private String[] options = { "Start", "Options", "Help", "Quit" };
	
	// Fonts
	private Color titleColor;
	private Font titleFont;
	private Font font;			// Font for the menu options

	
	public MenuState(GameStateManager gsm, InputHandler input) {
		
		this.gsm = gsm;
		this.input = input;
		
		init();
		
	}
	
	public void init() {
		
		// Create the background image
		try {

			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(0.1, 0);			// Set it moving to the right at 0.1 pixels / tick

			titleColor = new Color(200, 20, 20);
			titleFont = new Font("Century Gothic", Font.PLAIN, 40);
			font = new Font("Arial", Font.PLAIN, 20);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void select() {
		if (currentChoice == 0) {
			// Start
			gsm.setState(GameStateManager.LEVELSTATE);
		}
		if (currentChoice == 1) {
			// Options
		}
		if (currentChoice == 2) {
			// Help
		}
		if (currentChoice == 3) {
			// Quit
			System.exit(0);
		}
	}

	public void tick() {
		
		// Update background.
		bg.tick();
		
		// Update menu options.
		if (!input.up.isPressed() && !input.down.isPressed()) buttonPress = false;		// Make sure one key press transfers to one menu movement. ie Can't hold the key down.
		if (input.enter.isPressed() || input.jump.isPressed()) {
			select();
		}
		if (input.up.isPressed() & !buttonPress) {
			currentChoice--;
			buttonPress = true;
			if (currentChoice == -1) {						// If choice has no more options in that direction, go to the other side.
				currentChoice = options.length - 1;
			}
		}
		if (input.down.isPressed() & !buttonPress) {
			currentChoice++;
			buttonPress = true;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		
	}

	public void render(Graphics2D g) {
		
		// Draw background
		bg.render(g);
		
		// Draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString(Game.GAME_NAME, (int)(GamePanel.WIDTH / 2) - 120, (int)(GamePanel.HEIGHT * 0.2));		// Generate the position semi-automatically.
		
		// Draw menu options
		g.setFont(font);
		for (int i = 0; i < options.length;  i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], (GamePanel.WIDTH / 2) - 30, 150 + i * 25);
		}
	}	
	
}
