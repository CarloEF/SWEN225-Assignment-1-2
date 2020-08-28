package swen225.cluedo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Player implements CluedoObject {
	private List<Card> hand;
	private String name;
	private String username;	// the name of the person controlling this player (or null)
	private Tile tile;
	private Color colour;
	
	private boolean allowedAccuse = true;

	private static Image icon;
	
	/**
	 * Constructs the player
	 * @param name
	 * @param tile
	 */
	public Player(String name, Color colour, String username) {
		this.name = name;
		this.colour = colour;
		this.username = username;
		this.hand = new ArrayList<Card>();
		this.tile = null;

		try {
			icon = ImageIO.read(new File("images/player-icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g) {
		int tileTop = Board.TOP + tile.getY() * Board.TILE_SIZE;
		int tileLeft = Board.LEFT + tile.getX() * Board.TILE_SIZE;

		g.setColor(colour);
		g.fillOval(tileLeft, tileTop, Board.TILE_SIZE, Board.TILE_SIZE);

		g.setColor(Color.BLACK);
		g.drawOval(tileLeft, tileTop, Board.TILE_SIZE, Board.TILE_SIZE);
	}
	
	/**
	 * Adds a card to the player's hand
	 * @param card
	 */
	public void addCard(Card card) {
		this.hand.add(card);
	}
	
	/**
	 * Gets the cards a player can use to refute a murder suggestion
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
	 * @param newTile
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
	 * @return
	 */
	public List<Card> getCards() {
		return hand;
	}
	
	/**
	 * Gets whether the player is allowed to suggest/accuse
	 * @return
	 */
	public boolean canAccuse() {
		return allowedAccuse;
	}
	
	/**
	 * Sets whether the player is allowed to suggest/accuse
	 * @param allowed
	 */
	public void setCanAccuse(boolean allowed) {
		allowedAccuse = allowed;
	}
	
	/**
	 * Gets the player's name
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
	 * @param username The name of the human controlling this player
	 */
	public void setUsername(String username) { this.username = username; }
	
	/**
	 * Moves the player to the room
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
}
