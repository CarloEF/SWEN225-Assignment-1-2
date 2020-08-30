/**
 * 
 */
package swen225.cluedo;

import java.awt.*;

/**
 * Interface that represents a card
 */
public interface Card {
	/**
	 * Constants for the names of the three types of card
	 */
	String[] PLAYERS = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
	String[] WEAPONS = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
	String[] ROOMS = {"Kitchen", "Ball Room", "Conservatory", "Billiard Room", "Library", "Study", "Hall", "Lounge", "Dining Room"};

	public String getName();
	public Image getIcon();
}
