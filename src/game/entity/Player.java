package game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Player extends Entity {

	// Player stuff
	private boolean dead;
	
	// Animations
	private ArrayList<BufferedImage[]> sprites;	
	private final int[] numFrames = {				// How many frames each animation has
			2, 2, 1, 1
	};

	// Animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	
	
	public Player(TileMap tm, LevelState ls) {
		super(tm, ls);
		
		spriteWidth = 30;		// Just for reading in spritesheet.
		spriteHeight = 30;
		width = 20;				// Actual width and height.
		height = 28;
		
		moveSpeed = 0.3;
		maxSpeed = 3;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 10;
		jumpStart = -2.8;
		stopJumpSpeed = 0.3;
		
		
		facingRight = true;
		
		// Load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			
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
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE), false);
		animation.setDelay(400);
	}
	
	private void getNextPosition() {
		
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {				// Stopping
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}
		
		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if (falling) {
			dy += fallSpeed; 
			
			if (dy > 0) { jumping = false; }
			if (dy < 0 && !jumping) { dy += stopJumpSpeed; }			// The longer you hold the jump button, the higher you jump.
			if (dy > maxFallSpeed) { dy = maxFallSpeed; }				// Terminal velocity
		}
		
	}
	
	public void tick() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// Fix bounds
		if (x > (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2) x = (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2;			// Strange bug where furthest tile on the right is not BLOCKED (you fall through it)
		if (x < width / 2) x = width / 2;

		// Animation
		if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING), false);
				animation.setDelay(100);
				width = 30;
			}
		}
		else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING), false);
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING), false);
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE), false);
				animation.setDelay(400);
				width = 30;
			}
		}

		animation.tick();

		// set direction
		if (right) facingRight = true;
		if (left) facingRight = false;
	}

	public void render(Graphics2D g) {

		setMapPosition();
		
		super.render(g);

	}
	
}













