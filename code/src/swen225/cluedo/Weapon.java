package swen225.cluedo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class representing a weapon
 * 
 */
public class Weapon implements CluedoObject {
	private String name;
	private String tileValue;
	//should only ever be a RoomTile
	private Tile tile;

	private static Image icon;
	
	/**
	 * Constructs a weapon
	 * @param name
	 * @param tile
	 */
	public Weapon(String name, String tileValue) {
		this.name = name;
		this.tileValue = tileValue;
		this.tile = null;

		try {
			icon = ImageIO.read(new File("images/weapon-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	@Override
	public Image getIcon() {
		return icon;
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
