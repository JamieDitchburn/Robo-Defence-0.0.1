package game.entity;

import java.awt.Font;

import game.entity.turrets.Turret;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Enemy extends Entity {

	// Attributes
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected double damage;
	
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
	
	public void setx(int x) { this.x = x; }
	
	public void setAttacking(boolean b) { 
		if (b) dx = 0;
		attacking = b; 
	}
	
	public void hit(int damage) {
		if (dead) return;
		health -= damage;
		if (health < 0) health = 0;
		if (health == 0) dead = true;
	}
	
	public void tick() {
		
		// Fix bounds
		if (x > (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2) x = (levelState.MAP_WIDTH * tileMap.getTileSize()) - width / 2;		// Bug where furthest tile on the right is not BLOCKED (you fall through it)
		if (x < width / 2) x = width / 2;
		
		
		
	}
	
}











