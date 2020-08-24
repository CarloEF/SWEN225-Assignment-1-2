package swen225.cluedo;

import java.util.List;
import java.util.ArrayList;

public class Player implements CluedoObject {
	private List<Card> hand;
	private String name;
	private String tileValue;
	private Tile tile;
	
	private boolean allowedAccuse = true;
	
	/**
	 * Constructs the player
	 * @param name
	 * @param tile
	 */
	public Player(String name, String tileValue) {
		this.name = name;
		this.tileValue = tileValue;
		this.hand = new ArrayList<Card>();
		this.tile = null;
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
	 * @param murderSugg
	 * @param weaponSugg
	 * @param roomSugg
	 * @return
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
		return tileValue;
	}
}
