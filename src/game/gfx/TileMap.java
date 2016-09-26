package game.gfx;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class TileMap {

	// tileset
	private BufferedImage tileset;
	private int tileSize;
	private int numTilesAcross;
	private Tile[][] tiles;			// Represents the tileset in an array [rows][cols]

	public TileMap(int tileSize) {
		this.tileSize = tileSize;		// Allow for automatic adjustment to different tile sizes.
	}
	
	public void loadTiles(String s) {			// Loads the tileset into memory
		
		try {
			
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];			//  How many tiles we have.

			BufferedImage subimage;
			for (int col = 0; col < numTilesAcross; col++) {								// Everthing in the first row will not collide with player. Everything in the second row will.
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);		// First row of tileset has no collision with player
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);		// Second row collides with player
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public int getNumTilesAcross() { return numTilesAcross; }
	
	public Tile getTile(int rc) {
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c];
	}
	
	public int getType(int rc) {		// Get tile type.
		if (rc < 0) return Tile.NORMAL;
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	
}
















