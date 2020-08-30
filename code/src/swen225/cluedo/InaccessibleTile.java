package swen225.cluedo;

/**
 * Class representing a tile that can't be traversed through
 *
 */
public class InaccessibleTile extends Tile {
	
	/**
	 * Inaccessible tiles always have all 4 walls since you can't get to them
	 * @param x The column of this Tile
	 * @param y The row of this Tile
	 */
	public InaccessibleTile(int x, int y) {
		super(true, true, true, true, x, y);
	}
	
	/**
	 * Overrides super class, so this tile is never accessible
	 */
	public boolean isAccessible() {
		return false;
	}
	
	/**
	 * Symbol representing an inaccessible tile
	 */
	public String toString() {
		return "*";
	}
}
