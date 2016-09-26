package game.entity.enemies;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import game.entity.Animation;
import game.entity.Enemy;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Robot1 extends Enemy {

	private BufferedImage[] sprites;
	
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

		// Load Sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/robot1.gif"));

			sprites = new BufferedImage[3];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
			}


		} catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites, false);
		animation.setDelay(100);

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

		// Update animation.
		animation.tick();
		
		// Fix bounds
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
	
	
















