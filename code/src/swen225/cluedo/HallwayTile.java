/**
 * 
 */
package swen225.cluedo;

/**
 * Class representing a hallway tile
 */
public class HallwayTile extends Tile {
	
	/**
	 * Constructs a hallway tile
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param x
	 * @param y
	 */
	public HallwayTile(boolean up, boolean down, boolean left, boolean right, int x, int y) {
		super(up, down, left, right, x, y);
	}
	
}
