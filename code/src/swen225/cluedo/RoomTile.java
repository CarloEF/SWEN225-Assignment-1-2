package swen225.cluedo;

/**
 * Class representing a room tile
 *
 */
public class RoomTile extends Tile {
	
	//the room this belongs to
	private Room room;
	
	/**
	 * Constructs a roomtile
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param room
	 */
	public RoomTile(boolean up, boolean down, boolean left, boolean right, int x, int y, Room room) {
		super(up, down, left, right, x, y);
		
		this.room = room;
		room.addTile(this);
	}
	
	/**
	 * Gets the room this belongs to
	 * @return
	 */
	public Room getRoom() {
		return room;
	}
	
	/**
	 * Can't travel through rooms, so a room is always inaccessible
	 */
	public boolean isAccessible() {
		return false;
	}
	
	/**
	 * Returns the object on this tile if one exists, else the room's value
	 */
	public String toString() {
		String tileValue = super.toString();
		//if tile doesn't have an object on it return room's tile value
		if (tileValue.equals(" ")) {
			return room.toString();
		}
		return tileValue;
	}

}
