package game.entity.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.entity.Animation;
import game.entity.Enemy;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Robot1 extends Enemy {
	
	// Animations
	private final int[] numFrames = {				// How many frames each animation has
			4, 4
	};
	
	public Robot1(TileMap tm, LevelState ls) {
		super(tm, ls);

		moveSpeed = 0.2;
		maxSpeed = 0.4;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		spriteWidth = 60;		// For tilesheet
		spriteHeight = 60;
		width = 50;				// Actual width and height
		height = 58;

		health = maxHealth = 30;
		damage = 0.1;

		// Load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/robot1.gif"));

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
		currentAction = WALKING;
		animation.setFrames(sprites.get(WALKING), false);
		animation.setDelay(150);

		right = false;
		left = true;			// When spawned it goes left.
		facingRight = false;

	}
	
	private void getNextPosition() {

		// movement
		dx -= moveSpeed;		// Can only move left.
		if (dx < -maxSpeed) {
			dx = -maxSpeed;
		}
		

		// Falling
		if(falling) {				// Falling not going be used yet but leaves room for future map terrain.
			dy += fallSpeed;
		}

	}
	
	public void tick() {

		// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		super.tick();

	}
	
	public void render(Graphics2D g) {
		
		setMapPosition();
		
		// Draw health above enemies heads.
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString("" + health, (int)(x + xmap) - 5, (int)(y + ymap) - 30);
		
		super.render(g);

	}
	
}
	
	
















