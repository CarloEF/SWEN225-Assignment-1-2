package swen225.cluedo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * An object to represent a player (both the piece on the Board, and potentially a human)
 */
public class Player implements CluedoObject {
    private static Image icon; // the icon drawn on Player cards

    private List<Card> hand;
    private String name;
    private String username;    // the name of the person controlling this player
    private Tile tile;
    private Color colour;

    private boolean allowedAccuse = true;

    /**
     * Constructs the player
     *
     * @param name     The player's name (e.g. Col. Mustard)
     * @param colour   The colour that this player is drawn as
     * @param username The username of the person controlling this player, or empty String if no player
     */
    public Player(String name, Color colour, String username) {
        this.name = name;
        this.colour = colour;
        this.username = username;
        this.hand = new ArrayList<Card>();
        this.tile = null;

        // try to load the image
        try {
            icon = ImageIO.read(new File("images/player-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw this player on the Board
     *
     * @param g The graphics object to draw to
     */
    public void draw(Graphics2D g) {
        GUI.GUI.drawPlayer(this, g);
    }

    /**
     * Adds a card to the player's hand
     *
     * @param card The Card to add
     */
    public void addCard(Card card) {
        this.hand.add(card);
    }

    /**
     * Gets the cards a player can use to refute a murder suggestion
     *
     * @param murdererSugg The suggested Player
     * @param weaponSugg   The suggested Weapon
     * @param roomSugg     The suggested Room
     * @return The cards this player can use to refute this suggestion
     */
    public List<Card> getRefutes(Player murdererSugg, Weapon weaponSugg, Room roomSugg) {
        List<Card> refuteCards = new ArrayList<Card>();

        for (Card card : hand) {
            if (card == murdererSugg || card == weaponSugg || card == roomSugg) {
                refuteCards.add(card);
            }
        }

        return refuteCards;
    }

    /**
     * Moves the player to a tile
     *
     * @param newTile The tile to move to
     */
    public void moveToTile(Tile newTile) {
        if (tile != null) {
            tile.setObject(null);
        }
        newTile.setObject(this);
        tile = newTile;
    }

    /**
     * Gets the tile the player is currently on
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Gets a list of the cards in the player's hand
     *
     * @return The Cards
     */
    public List<Card> getCards() {
        return hand;
    }

    /**
     * Gets whether the player is allowed to suggest/accuse
     *
     * @return True if player is allowed (hasn't made an incorrect accusation)
     */
    public boolean canAccuse() {
        return allowedAccuse;
    }

    /**
     * Sets whether the player is allowed to suggest/accuse
     *
     * @param allowed The value to change to
     */
    public void setCanAccuse(boolean allowed) {
        allowedAccuse = allowed;
    }

    /**
     * Gets the player's name
     *
     * @return player's name
     */
    public String getName() {
        return name;
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    /**
     * Setter for username
     *
     * @param username The name of the human controlling this player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Moves the player to the room
     *
     * @param room - the room to move to
     */
    public void moveToRoom(Room room) {
        Tile newTile = room.getFreeTile();
        moveToTile(newTile);
    }

    /**
     * Returns the tile value
     */
    public String toString() {
        return name;
    }

    /**
     * Returns the username of the player controlling this character
     *
     * @return The username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the colour that this player is drawn as
     *
     * @return A Color object
     */
    public Color getColour() {
        return colour;
    }
}
