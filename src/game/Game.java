package game;

import javax.swing.JFrame;


/*
 * Resources used:
 * 
 * https://www.youtube.com/playlist?list=PL-2t7SM0vDfcIedoMIghzzgQqZq45jYGv
 * Ludum Dare
 * 
 */

public class Game {

	public static final String GAME_NAME = "Robo-Defence";		// Name might be changed later.
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame(GAME_NAME);
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);		// Center on screen
		frame.setVisible(true);
		
	}
	
	
}
