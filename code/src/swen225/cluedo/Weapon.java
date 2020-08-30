package swen225.cluedo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class representing a weapon
 */
public class Weapon implements CluedoObject {
	private String name;
	//should only ever be a RoomTile
	private Tile tile;

	private static Image icon;
	
	/**
	 * Constructs a weapon
	 * @param name The name of this weapon (e.g. Candlestick)
	 */
	public Weapon(String name) {
		this.name = name;
		this.tile = null;

		// try to load the icon image, drawn on cards
		try {
			icon = ImageIO.read(new File("images/weapon-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draw this Weapon on the Board
	 * @param g The graphics object to draw on
	 */
	public void draw(Graphics2D g) {
		int tileTop = Board.TOP + tile.getY() * Board.TILE_SIZE;
		int tileLeft = Board.LEFT + tile.getX() * Board.TILE_SIZE;
		int halfTile = Board.TILE_SIZE / 2;

		int innerPadding = Board.TILE_SIZE / 10;
		int diamondSize = Board.TILE_SIZE - 2*innerPadding;
		int halfDiamond = diamondSize / 2;
		int diamondTop = tileTop + innerPadding;
		int diamondLeft = tileLeft + innerPadding;

		int[] xPoints = new int[] {diamondLeft + halfDiamond, diamondLeft + diamondSize, diamondLeft + halfDiamond, diamondLeft};
		int[] yPoints = new int[] {diamondTop, diamondTop + halfDiamond, diamondTop + diamondSize, diamondTop + halfDiamond};

		g.setColor(Color.BLACK);
		g.fillPolygon(xPoints, yPoints, 4);

		Font font = new Font("SansSerif", Font.PLAIN, 10);
		FontMetrics fontMetrics = g.getFontMetrics(font);
		int textWidth = fontMetrics.stringWidth(name);
		int textHeight = fontMetrics.getHeight();
		int textLeft = tileLeft + halfTile - (textWidth / 2);
		int textTop = tileTop + Board.TILE_SIZE;
		g.setFont(font);

		g.setColor(Color.BLACK);
		g.drawString(name, textLeft, textTop + textHeight - 5);
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
	 * Return the icon to draw on a Weapon card
	 * @return The icon as an Image
	 */
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
		return name;
	}
}
