package swen225.cluedo;

/**
 * Class representing a weapon
 * 
 */
public class Weapon implements CluedoObject {
	private String name;
	private String tileValue;
	//should only ever be a RoomTile
	private Tile tile;
	
	/**
	 * Constructs a weapon
	 * @param name
	 * @param tile
	 */
	public Weapon(String name, String tileValue) {
		this.name = name;
		this.tileValue = tileValue;
		this.tile = null;
	}
	
	/**
	 * Gets the tile a weapon is on
	 * @return tile
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * Gets the name of the weapon
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Moves this weapon to a room
	 * @param room - the room to move to
	 */
	public void moveToRoom(Room room) {
		Tile newTile = room.getFreeTile();
		
		if (tile != null) {
			tile.setObject(null);
		}
		
		newTile.setObject(this);
		tile = newTile;
	}
	
	/**
	 * This weapon's representation on the board
	 */
	public String toString() {
		return tileValue;
	}
}
