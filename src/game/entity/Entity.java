package game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.gamestate.LevelState;
import game.gfx.Tile;
import game.gfx.TileMap;

public class Entity {

	// Tile stuff
	protected LevelState levelState;
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;

	// Position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	// Dimensions
	protected int spriteWidth;		// Just for reading in spritesheets
	protected int spriteHeight;

	// Collision box
	protected int width;		// Real width and height 
	protected int height;

	// Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	// Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;		// Might not be needed
	protected boolean facingRight;

	// Movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// Movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;			// If not pressing left or right, this is the value that your speed decrements by.
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;		// Mechanic where the longer you hold jump, the higher you go.

	public Entity(TileMap tm, LevelState ls) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		levelState = ls;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int)x - width, (int)y - height, width, height);
	}
	
	@SuppressWarnings("static-access")
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - width / 2) / tileSize;
		int rightTile = (int)(x + width / 2 - 1) / tileSize;			// -1 so we don't step over into the next column.
		int topTile = (int)(y - height / 2) / tileSize;
		int bottomTile = (int)(y + height / 2 - 1) / tileSize;
		
		if (topTile < 0 || bottomTile >= levelState.MAP_HEIGHT || 		// Fix to an out of bounds exception when going outside the map.
				leftTile < 0 || rightTile >= levelState.MAP_WIDTH ) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		
		int tl = levelState.getType(topTile, leftTile);		// Set corners
		int tr = levelState.getType(topTile, rightTile);
		int bl = levelState.getType(bottomTile, leftTile);
		int br = levelState.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;			// Booleans to say if blocked.
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	// Used for platforming elements. Not in current build but plan to use in the future.
	public void checkTileMapCollision() {			// Works for both x and y directions. Only going to use the x-component for now but leaves room for future expansion.

		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;

		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;

		calculateCorners(x, ydest);
		if (dy < 0) { 			// upwards
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + height / 2;			// Set just below where we bumped our head.
			} else {
				ytemp += dy;		// Otherwise keep going.
			}
		}
		if (dy > 0) { 			// downwards
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - height / 2;
			} else {
				ytemp += dy;
			}
		}

		calculateCorners(xdest, y);
		if (dx < 0) { 			// left
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + width / 2;
			} else {
				xtemp += dx;
			}
		}
		if (dx > 0) { 			// right
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - width / 2;
			} else {
				xtemp += dx;
			}
		}

		if(!falling) {
			calculateCorners(x, ydest + 1);			// Have to check the ground 1 pixel below us and make sure we haven't fallen off a cliff
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}

	}

	public int getx() { return (int) x; }
	public int gety() { return (int) y; }
	public int getSpriteWidth() { return spriteWidth; }
	public int getSpriteHeight() { return spriteHeight; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {		// Every map object has 2 positions: Global pos = regular x and y, Local(Map) pos = x + xmap, y + ymap. Tells where to actually draw the character.
		xmap = levelState.getx();
		ymap = levelState.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public void render(Graphics2D g) {
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (x + xmap - spriteWidth / 2), (int) (y + ymap - spriteHeight / 2), null);
		} else {		// Facing left
			g.drawImage(animation.getImage(), (int) (x + xmap - spriteWidth / 2 + spriteWidth), (int) (y + ymap - spriteHeight / 2), -spriteWidth, spriteHeight , null);
		}
	}
	
}















