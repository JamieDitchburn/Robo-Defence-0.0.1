package game.entity;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.turrets.Turret;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Enemy extends Entity {

	// Attributes
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected double damage;
	
	// Animations actions
	protected static final int WALKING = 0;
	protected static final int ATTACKING = 1;
	protected static final int DYING = 2;
	
	// Animations
	protected ArrayList<BufferedImage[]> sprites;
	
	// Attack
	protected boolean attacking;
	
	protected Font font;
	
	public Enemy(TileMap tm, LevelState ls) {
		super(tm, ls);
		font = new Font("Arial", Font.PLAIN, 12);
	}	
	
	public boolean isDead() { return dead; }
	
	public int getWidth() { return width; }
	public int getHealth() { return health; }
	public double getDamage() { return damage; }
	public int getCurrentAction() { return currentAction; }
	
	public void setx(int x) { this.x = x; }
	
	public void setAttacking() { 
		if (currentAction != ATTACKING) {
			currentAction = ATTACKING;
			animation.setFrames(sprites.get(ATTACKING), false);
			animation.setDelay(75);
		}
		
	}
	
	public void setWalking() {
		if (currentAction != WALKING) {
			currentAction = WALKING;
			animation.setFrames(sprites.get(WALKING), false);
			animation.setDelay(150);
		}
	}
	
	public void hit(int damage) {
		if (dead) return;
		health -= damage;
		if (health < 0) health = 0;
		if (health == 0) dead = true;
	}
	
	public void tick() {
		
		// Update animation.
		animation.tick();
		
		// Fix bounds
		if (x > (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2) x = (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2;		// Bug where furthest tile on the right is not BLOCKED (you fall through it)
		if (x < width / 2) x = width / 2;
		
	}
	
}











