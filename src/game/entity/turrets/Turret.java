package game.entity.turrets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Enemy;
import game.entity.Entity;
import game.gamestate.LevelState;
import game.gfx.TileMap;

public class Turret extends Entity {

	// Attributes
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int turretRange;		// In pixels
	protected double fireRate;			// How many times it fires per second.
	protected double fireAnimRate;		// How many times to loop the firing animation per second.
	protected int upgradeCost;

	// Animations
	protected boolean firing;
	protected boolean dying;
	protected boolean building;
	protected int idleAnimDelay;
	protected int buildAnimDelay;
	protected ArrayList<BufferedImage[]> sprites;	
	
	// Animations actions
	protected static final int IDLE = 0;
	protected static final int FIRING = 1;
	protected static final int DESPAWNING = 2;
	protected static final int DEAD = 3;
	protected static final int BUILDING = 4;
	
	// Fire rate
	protected long fireTimer;
	protected long elapsed;
	
	// Firing
	protected int closestEnemy;
	
	public Turret(TileMap tm, LevelState ls) {
		super(tm, ls);
	}

	public boolean isDead() { return dead; }
	public int getDamage() { return damage; }
	public double getFireRate() { return fireRate; }
	public int getCurrentAction() { return currentAction; }
	public int getBuildAnimDelay() { return buildAnimDelay; }
	public int getUpgradeCost() { return upgradeCost; }

	public void hit(double damage) {
		if (dead) return;			// Do something.
		health -= damage;
		if (health < 0) health = 0;
		if (health == 0) dead = true;
	}
	
	// Whilst being called every tick, only applies damage based on fireRate.
	public void attack(Enemy e) {		
		elapsed = System.currentTimeMillis() - fireTimer;
		
		if (elapsed >= (1000 / fireRate) ) {			// Only apply damage at fireRate. fireRate is in milliseconds.
			e.hit(damage);
			fireTimer = System.currentTimeMillis();
		}
		
	}
	
	public void setDead() {
		dead = true;
	}
	
	public void setIdle() {
		if (currentAction == FIRING) {
			currentAction = IDLE;
			animation.setFrames(sprites.get(IDLE), false);
			animation.setDelay(idleAnimDelay);
		}
	}
	
	public void setFiring() {
		if (currentAction == IDLE) {
			currentAction = FIRING;
			animation.setFrames(sprites.get(FIRING), false);
			animation.setDelay((int)((1000 / fireAnimRate) / sprites.get(FIRING).length));
			fireTimer = System.currentTimeMillis();			// Start the firing timer.
		}
	}
	
	public void setBuilding() {
		currentAction = BUILDING;
		animation.setFrames(sprites.get(BUILDING), true);
		animation.setDelay(buildAnimDelay);
	}
	
	public void setDespawning(int delay) {
		currentAction = DESPAWNING;
		animation.setFrames(sprites.get(DESPAWNING), true);
		animation.setDelay(delay);
	}
	
	public boolean checkRange(Enemy enemy) {		// Check to see if the enemy is in range.
		return (enemy.getx() - x) <= turretRange && enemy.getx() > x;
	}

	public void tick() {
		
		// Check turret build animation has stopped if building
		if (currentAction == BUILDING) {
			if (animation.hasPlayedOnce()) { 
				currentAction = FIRING;			// Fix to glitch where buildings weren't exiting the build animation until targets were available.
				setIdle();
			}
		}
		
		// Check turret despawn animation has stopped if despawning and set to dead if it has.
		if (currentAction == DESPAWNING) {
			if (animation.hasPlayedOnce()) { 
				currentAction = DEAD; 
				dead = true;
			}
		}
		
		// Check if turret should be firing and apply damage to closest enemy.
		closestEnemy = -1;
		for (int i = 0; i < levelState.getRobots().size(); i++) {				
			Enemy e = levelState.getRobot(i);									// For each robot, e in robots.
			
			if (checkRange(e)) {												// If in range.
				
				if (closestEnemy == -1) closestEnemy = i;						// If not the first robot in loop.
				if (e.getx() < levelState.getRobot(closestEnemy).getx()) {		// If closer than other enemies that are in range.
					closestEnemy = i;
				}
				setFiring();							// If any are in range, start firing.
			}
		}
		if (closestEnemy == -1) setIdle();				// If no enemies are in range, set idle.
		else if (currentAction != BUILDING) {			// Don't start atatcking until the building animation has finished.
			attack(levelState.getRobot(closestEnemy));	// Only attack the closest enemy.
		}
		
	}
	
	public void render(Graphics2D g) {
		
		super.render(g);
		
		// Draw health above turret
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.drawString("" + health, (int)(x + xmap) - 10, (int)(y + ymap) + 10);
	}
	
}
