package game.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;

import game.GamePanel;
import game.InputHandler;
import game.entity.Enemy;
import game.entity.Player;
import game.entity.enemies.Robot1;
import game.entity.turrets.Turret;
import game.entity.turrets.Turret1;
import game.entity.turrets.Turret2;
import game.gfx.Background;
import game.gfx.TileMap;

public class LevelState extends GameState {

	// GameStateManager
	private GameStateManager gsm;
	private boolean gameOver;
	
	// HUD
	private Font font, font2;
	
	// Player
	private Player player;
	
	// Key press bools
	private boolean actionPressed;		// Boolean to state if the action button is pressed. Used so action only performed once each time the key is pressed. ie Can't hold the button down.
	private boolean despawnPressed;
	private boolean spawnRobotPressed;
	private boolean upgradePressed;
	
	// Enemies
	private ArrayList<Enemy> robots;
	private int robotsKilled;
	private int gameLevel;			// Difficulty level for the amount of robots being spawned. Increases as the game goes on.
	
	// Turrets
	private ArrayList<Turret> turrets;
	private boolean ableToBuild;		// able to build new turret at players position
	
	// Resources
	private double resource;
	private double resourceRate;
	private int baseHealth;
	
	// position
	private double x;
	private double y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;				// Smooth camera movement
	
	// Map
	public static final int MAP_WIDTH = 64;
	public static final int MAP_HEIGHT = 12;
	private int[][] map;			// [rows][cols]
	private TileMap tileMap;
	
	// Background
	private Background bg;
		
	// Input
	private InputHandler input;
	
	// drawing
	private int rowOffset;			// Only want to draw what's on the screen.
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	private Random rand;
	
	
	public LevelState(GameStateManager gsm, InputHandler input) {
		this.gsm = gsm;
		this.input = input;
		init();
	}

	public void init() {
		
		// HUD
		font = new Font("Arial", Font.PLAIN, 12);
		font2 = new Font("Arial", Font.PLAIN, 10);
		
		// Resources
		resource = 40;
		baseHealth = 1000;
		resourceRate = 0.1;
		robotsKilled = 0;
		
		// Tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSet/tileset.gif");
		this.setPosition(0,0);
		tween = 0.07;			// Camera smoothing function.
		
		// Background
		bg = new Background("/Backgrounds/menubg.gif", 0.2);
		
		// Player
		player = new Player(tileMap, this);
		player.setPosition(100, 100);
		
		// Map
		map = new int[MAP_HEIGHT][MAP_WIDTH];
		for(int i = 0; i < MAP_HEIGHT; i++) {
			for(int j = 0; j < MAP_WIDTH; j++) {
				map[i][j] = -1;
			}
		}
		generateLevel();
		
		// Spawn turrets
		turrets = new ArrayList<>();
		
		// Spawn robots
		robots = new ArrayList<>();
		robots.add(new Robot1(tileMap, this));
		robots.get(0).setPosition(800, 100);
		gameLevel = 0;
		
		// Set min and max coords
		xmin = GamePanel.WIDTH - (MAP_WIDTH * tileMap.getTileSize());
		xmax = 0;
		ymin = GamePanel.HEIGHT - (MAP_HEIGHT * tileMap.getTileSize());
		ymax = 0;
		
		// Drawing
		numRowsToDraw = GamePanel.HEIGHT / tileMap.getTileSize() + 2;		// Extra 2 just for padding.
		numColsToDraw = GamePanel.WIDTH / tileMap.getTileSize() + 2;
		
	}

	public void generateLevel() {
		rand = new Random();
		
		// Populate floor tiles.
		int j;		// Used to return the value from rand.nextInt
		for (int i = 0; i < MAP_WIDTH; i++) {
			map[MAP_HEIGHT - 1][i] = tileMap.getNumTilesAcross();			// Populate bottom of map with the first grass block.
			map[MAP_HEIGHT - 2][i] = (j = rand.nextInt(7)) < 3? j: -1;		// Randomly populate second to bottom of map with greenery.
		}
		
		// Draw base on left.
		map[MAP_HEIGHT - 2][0] = tileMap.getNumTilesAcross() + 2;		// Door
		map[MAP_HEIGHT - 3][0] = tileMap.getNumTilesAcross() + 2;	
		map[MAP_HEIGHT - 4][0] = tileMap.getNumTilesAcross() + 2;
		map[MAP_HEIGHT - 5][0] = tileMap.getNumTilesAcross() + 3;		// Top of door
		map[MAP_HEIGHT - 5][1] = 3;
		map[MAP_HEIGHT - 5][2] = 6;
		map[MAP_HEIGHT - 6][0] = 7;		
		map[MAP_HEIGHT - 6][1] = 4;		// Roof
		map[MAP_HEIGHT - 6][2] = 5;
		
	}
	
	// Spawn a turret where the player is standing.
	public void spawnTurret() {	
		ableToBuild = true;					// Reset from last time this method was called.
		
		for (Turret t: turrets) {			// Check not too close to other turrets.
			if (player.getx() < t.getx() + t.getWidth() && player.getx() > t.getx() - t.getWidth()) {
				ableToBuild = false;
			}
		}
		
		if (ableToBuild && resource > 50) {					// Build a new turret.
			turrets.add(new Turret1(tileMap, this));
			turrets.get(turrets.size() - 1).setPosition(player.getx(), 300);
			resource -= 50;					// Costs 50 resources
		}
		
	}
	
	public void upgradeTurret() {
		int xtemp;
		int ytemp;
		int turretLevel;
		
		for (Turret t: turrets) {			// Check to see if a turret is close enough.
			if (t.getx() - (t.getWidth() / 3) < player.getx() + (player.getWidth() / 3) && t.getx() + (t.getWidth() / 3) > player.getx() - (player.getWidth() / 3) 
					&& t.getCurrentAction() != 4 && t.getCurrentAction() != 2 ) {			// Check if not building or despawning
				
				// Check current turret level, remove it, and create a new turret of the next highest level.
				try {
					turretLevel = Integer.parseInt(t.getClass().getSimpleName().replaceAll("[\\D]", ""));	// Get number from the end of the class name of current turret. 
					turretLevel ++;			// Increase level
					if (turretLevel > 3) return;			// Maximum turret level.
					Turret turret = (Turret) Class.forName("game.entity.turrets.Turret" + turretLevel)
							.getConstructor(TileMap.class, LevelState.class)
							.newInstance(new Object[] {tileMap, this});
					if (turret.getUpgradeCost() > resource) return;			// check got not enough resources.
					turrets.add(turret);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				xtemp = t.getx();		// Get coordinates of turret being upgraded
				ytemp = t.gety();
				t.setDespawning(t.getBuildAnimDelay());			// Despawn at normal building rate
				turrets.get(turrets.size() - 1).setPosition(xtemp, ytemp);		// Spawn at position of previous turret.
				resource -= t.getUpgradeCost();
				
				break;
			}
		}
		
	}
	
	// If a turret is close enough to player, despawn it.
	public void despawnTurret() {
		
		for (Turret t: turrets) {			// Check to see if a turret is close enough.
			if (t.getx() - (t.getWidth() / 3) < player.getx() + (player.getWidth() / 3) && t.getx() + (t.getWidth() / 3) > player.getx() - (player.getWidth() / 3) 
					&& t.getCurrentAction() != 4 && t.getCurrentAction() != 2) {			// and check if not building or despawning
				t.setDespawning(t.getBuildAnimDelay());		// Despawn slowly
				resource += 25;
				break;
			}
		}
		
	}
	
	public void checkCollisionWithTurret(Enemy enemy) {			// Check if the enemy is colliding with a turret and attack.
		
		for (Turret t: turrets) {
			if (enemy.getx() - (enemy.getWidth() / 2) < t.getx() + (t.getWidth() / 2) && enemy.getx() + (enemy.getWidth() / 2) > t.getx() - (t.getWidth() / 2)) {
				enemy.setAttacking(true);
				t.hit(enemy.getDamage());
				enemy.setx((int)(t.getx() + (t.getWidth() / 2) + (enemy.getWidth() / 2)));
				break;
			}
			enemy.setAttacking(false);
		}

	}
	
	// Randomly spawn robots in increasing numbers.
	private void spawnEnemies() {
		gameLevel++;
		
		if (rand.nextInt(600) <= 0 + Math.pow(gameLevel, 0.15)) {
			robots.add(new Robot1(tileMap, this));
			robots.get(robots.size() - 1).setPosition(1000, 100);
		}
		
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	
	public int getType(int r, int c) {
		return tileMap.getType(map[r][c]);
	}
	
	public Enemy getRobot(int i) { return robots.get(i); }
	public ArrayList<Enemy> getRobots() { return robots; }
	public ArrayList<Turret> getTurrets() { return turrets; }
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;			// Smoothing function for the camera movement.
		this.y += (y - this.y) * tween;
		
		fixBounds();			// Ensure coords are within limits
		
		colOffset = (int) -this.x / tileMap.getTileSize();	// Where to start drawing.
		rowOffset = (int) -this.y / tileMap.getTileSize();
		
	}
	
	private void fixBounds() {
		if (x < xmin) x = xmin;
		if (y < ymin) y = ymin;
		if (x > xmax) x = xmax;
		if (y > ymax) y = ymax;
	}
	
	public void tick() {
		
		// Player key presses
		if (input.left.isPressed()) {			// Move left
			player.setLeft(true);
		} else { player.setLeft(false); }
		
		if (input.right.isPressed()) {			// Move right
			player.setRight(true);
		} else { player.setRight(false); }
		
		if (input.up.isPressed() || input.jump.isPressed()) {		// Jump
			player.setJumping(true);
		}  else { player.setJumping(false); }
		
		if (!input.action.isPressed()) actionPressed = false;
		if (input.action.isPressed() & !actionPressed) {			// Action: Place turret
			spawnTurret();
			actionPressed = true;
		}
		
		if (!input.despawn.isPressed()) despawnPressed = false;
		if (input.despawn.isPressed() & !despawnPressed) {			// Despawn turret
			despawnTurret();
			despawnPressed = true;
		}
		
		if (!input.robot.isPressed()) spawnRobotPressed = false;
		if (input.robot.isPressed() & !spawnRobotPressed) {			// Spawn robot
			robots.add(new Robot1(tileMap, this));
			robots.get(robots.size() - 1).setPosition(1000, 100);
			spawnRobotPressed = true;
		}
		
		if (!input.upgrade.isPressed()) upgradePressed = false;
		if (input.upgrade.isPressed() & !upgradePressed) {			// Upgrade turret
			upgradeTurret();
			upgradePressed = true;
		}
		
		if (input.monies.isPressed()) {
			resource += 100;
		}
		
		
		// Update player
		player.tick();
		
		// Update level
		this.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		// Update bg.
		bg.setPosition(x, y);
		
		// Update turrets
		for (int i = 0; i < turrets.size(); i++) {
			Turret t = turrets.get(i);
			t.tick();
			if (t.isDead()) {
//				t.setDespawning(t.getBuildAnimDelay() / 5);
				turrets.remove(i);
				i--;
			}
		}
		
		// Keep randomly spawning enemies in increasing numbers
		spawnEnemies();
		
		// Update enemies
		for (int i = 0; i < robots.size(); i++) {
			Enemy robot = robots.get(i);
			robot.tick();
			if (robot.getx() == robot.getWidth() / 2 + tileMap.getTileSize()) baseHealth -= robot.getDamage();
				
			if (robot.isDead()) {				// Check if robot is dead.
				robots.remove(i);
				i--;
				resource += 20;
				robotsKilled++;
			}
			
			checkCollisionWithTurret(robot);		// If colliding with a turret, stop and attack.
			
		}
		
		// Update resources
		if (gameOver) {
			gsm.setState(GameStateManager.GAMEOVERSTATE);			// If base gets to 0, game over.
		}
		resource += resourceRate;
		if (baseHealth < 0) baseHealth = 0;
		if (baseHealth == 0) {
			gameOver = true;		// Loop through the render function one more time before starting gameOverState so that baseHealth is drawn at 0.
		}
		
	}
	

	public void render(Graphics2D g) {
		
		// Render bg
		bg.render(g);
		
		// Render level.
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= MAP_HEIGHT) break;			// Nothing else to draw.

			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= MAP_WIDTH) break;		// Nothing else to draw.

				int rc = map[row][col];

				if (rc >= 0) {			// Only draw if there's a tile there.
					g.drawImage(tileMap.getTile(rc).getImage(), (int)x + col * tileMap.getTileSize(), (int)y + row * tileMap.getTileSize(), null);					
				}

			}
		}
		
		// Render enemies
		for (int i = 0; i < robots.size(); i++) {
			robots.get(i).render(g);
		}
		
		// Render turrets
		for (int i = 0; i < turrets.size(); i++) {
			turrets.get(i).render(g);
		}
		
		// Render player
		player.render(g);
		
		// Render HUD
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString("Base Health: " + baseHealth, 10, 15);
		g.drawString("Resource: " + (int)resource, 10, 30);
		g.drawString("Turrets: " + turrets.size(), 10, 45);
		g.drawString("Robots on map: " + robots.size(), 10, 60);
		g.drawString("Robots killed: " + robotsKilled, 10, 75);
		
		g.setColor(Color.WHITE);
		g.setFont(font2);
		g.drawString("E - Place Turret            F - Remove Turret            U - Upgrade Turret            R - Spawn Robot", 170, 12);
		g.drawString("Turrets:    Level 1 - 50 resources           Level 2 - 100 resources           Level 3 - 200 resources", 170, 27);
		
	
	}


}
