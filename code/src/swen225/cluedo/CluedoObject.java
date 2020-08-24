/**
 * 
 */
package swen225.cluedo;

/**
 * Interface representing a cluedo object that can be on a tile or moved to a room
 * Extends card since cluedo objects are also cards
 */
public interface CluedoObject extends Card {
	public Tile getTile();
	public void moveToRoom(Room room);
}
