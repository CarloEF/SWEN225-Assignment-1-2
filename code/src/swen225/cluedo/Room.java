package swen225.cluedo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a Room
 */
public class Room implements Card {
	//list of all the tiles that make up this room
	private List<RoomTile> tiles;
	//important set of exittiles for movement
	private Set<Tile> exitTiles;
	
	private String name;

	private static Image icon;
	
	/**
	 * Constructs a room
	 * @param name The name of the Room as a String
	 */
	public Room(String name) {
		this.name = name;
		tiles = new ArrayList<RoomTile>();
		exitTiles = new HashSet<Tile>();

		try {
			icon = ImageIO.read(new File("images/room-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a tile from this room that is free
	 * With the current room sizes and CludeoObjects, there should always be a space free
	 * @return The first randomly located Tile
	 */
	public Tile getFreeTile() {
		int index = 0;
		do {
			index = (int)Math.floor(Math.random()*tiles.size());
		} while (tiles.get(index).hasObject());
		return tiles.get(index);
	}
	
	/**
	 * Adds an exit tile to the room
	 * @param tile The Tile to add
	 */
	public void addExitTile(Tile tile) {
		exitTiles.add(tile);
	}
	
	/**
	 * Gets the exit tiles for this room
	 * @return A Set of the exit tiles
	 */
	public Set<Tile> getExitTiles() {
		return exitTiles;
	}
	
	/**
	 * Adds a tile to the room
	 * @param tile The RoomTile to add
	 */
	public void addTile(RoomTile tile) {
		tiles.add(tile);
	}
	
	/**
	 * Gets the room's name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the icon for room cards
	 * @return The icon as an Image object
	 */
	@Override
	public Image getIcon() {
		return icon;
	}

	public List<RoomTile> getTiles() {
		return this.tiles;
	}

	/**
	 * This room's representation on the board
	 * @return tile value
	 */
	public String toString() {
		return name;
	}
}
