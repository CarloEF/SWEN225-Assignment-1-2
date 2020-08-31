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
     *
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
     *
     * @param g The graphics object to draw on
     */
    public void draw(Graphics2D g) {
        GUI.GUI.drawWeapon(this, g);
    }

    /**
     * Gets the tile a weapon is on
     *
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
     *
     * @return The icon as an Image
     */
    @Override
    public Image getIcon() {
        return icon;
    }

    /**
     * Moves this weapon to a room
     *
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
