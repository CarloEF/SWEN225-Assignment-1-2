package swen225.cluedo;

/**
 * Useful abstract class representing a tile
 * Stores the x and y coords as well as whether the tile has walls around it
 * Also stores any objects(players/weapons) the tile might have on it
 */
public abstract class Tile {
    private boolean upWall;
    private boolean downWall;
    private boolean leftWall;
    private boolean rightWall;

    private int x;
    private int y;

    private CluedoObject object;

    /**
     * Constructs a tile
     *
     * @param up    True if this Tile has an upper wall
     * @param down  True if this Tile has an bottom wall
     * @param left  True if this Tile has an left wall
     * @param right True if this Tile has an right wall
     * @param x     The column of this Tile
     * @param y     The row of the Tile
     */
    public Tile(boolean up, boolean down, boolean left, boolean right, int x, int y) {
        this.upWall = up;
        this.downWall = down;
        this.leftWall = left;
        this.rightWall = right;

        this.x = x;
        this.y = y;

        this.object = null;
    }

    /**
     * Sets the object on this tile
     *
     * @param newObject The object to move onto this tile
     */
    public void setObject(CluedoObject newObject) {
        object = newObject;
    }

    /**
     * Checks whether this tile has an object on it
     *
     * @return True if the tile has an object
     */
    public boolean hasObject() {
        return object != null;
    }

    /**
     * Checks whether this tile has an up wall
     *
     * @return True if this tile has an upper wall, otherwise false
     */
    public boolean hasUpWall() {
        return upWall;
    }

    /**
     * Checks whether this tile has a down wall
     *
     * @return True if this tile has an lower wall, otherwise false
     */
    public boolean hasDownWall() {
        return downWall;
    }

    /**
     * Checks whether this tile has a left wall
     *
     * @return True if this tile has a left wall, otherwise false
     */
    public boolean hasLeftWall() {
        return leftWall;
    }

    /**
     * Checks whether this tile has a right wall
     *
     * @return True if this tile has a right wall, otherwise false
     */
    public boolean hasRightWall() {
        return rightWall;
    }

    /**
     * Gets the x coord
     *
     * @return The column of this Tile
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coord
     *
     * @return The row of this Tile
     */
    public int getY() {
        return y;
    }

    /**
     * Get whether this tile can be traversed through
     * Can be overriden by sub classes
     *
     * @return True if this Tile is accessible, otherwise false
     */
    public boolean isAccessible() {
        return object == null;
    }

    /**
     * Overridden but used by sub classes if necessary
     */
    public String toString() {
        if (hasObject()) {
            return object.toString();
        }
        return " ";
    }
}
