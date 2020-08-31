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
     * @param up True if this Tile has an upper wall
     * @param down True if this Tile has an bottom wall
     * @param left True if this Tile has an left wall
     * @param right True if this Tile has an right wall
     * @param x The column of this Tile
     * @param y The row of the Tile
     */
    public HallwayTile(boolean up, boolean down, boolean left, boolean right, int x, int y) {
        super(up, down, left, right, x, y);
    }

}
