package game.entity.turrets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.entity.Animation;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Turret1 extends Turret{

	// Animations
	private final int[] numFrames = {				// How many frames each animation has
			1, 5, 5, 1, 5
	};
	
	public Turret1(TileMap tm, LevelState ls) {
		super(tm, ls);
		
		spriteWidth = 60;		// Just for reading in spritesheet.
		spriteHeight = 60;
		width = 24;				// Actual width and height.
		height = 14;
		
		health = maxHealth = 100;
		damage = 1;
		turretRange = 350;			// Range in pixels.
		fireRate = 2;				// How many times it fires per second.
		fireAnimRate = 2;			// Same as fireRate
		idleAnimDelay = 400;		// How long to wait in ms between frames of the idle animation.
		buildAnimDelay = 400;
		upgradeCost = 50;			// Just the build cost for the basic turret.
		
		facingRight = true;
		
		// Load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Turrets/turret1.gif"));

			sprites = new ArrayList<>();
			for (int i = 0; i < numFrames.length; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
				}

				sprites.add(bi);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = BUILDING;
		animation.setFrames(sprites.get(BUILDING), true);
		animation.setDelay(buildAnimDelay);
	}

	public void tick() {
		
		super.tick();
		animation.tick();
		
	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		super.render(g);
		
	}
	
}
