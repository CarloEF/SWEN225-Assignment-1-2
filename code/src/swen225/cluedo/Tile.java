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
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param x
	 * @param y
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
	 * @param newObject
	 */
	public void setObject(CluedoObject newObject) {
		object = newObject;
	}
	
	/**
	 * Checks whether this tile has an object on it
	 * @return
	 */
	public boolean hasObject() {
		return object != null;
	}
	
	/**
	 * Checks whether this tile has an up wall
	 * @return
	 */
	public boolean hasUpWall() {
		return upWall;
	}
	
	/**
	 * Checks whether this tile has a down wall
	 * @return
	 */
	public boolean hasDownWall() {
		return downWall;
	}
	
	/**
	 * Checks whether this tile has a left wall
	 * @return
	 */
	public boolean hasLeftWall() {
		return leftWall;
	}
	
	/**
	 * Checks whether this tile has a right wall
	 * @return
	 */
	public boolean hasRightWall() {
		return rightWall;
	}
	
	/**
	 * Gets the x coord
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y coord
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get whether this tile can be traversed through
	 * Can be overriden by sub classes
	 * @return
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
