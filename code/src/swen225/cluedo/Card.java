/**
 * 
 */
package swen225.cluedo;

import java.awt.*;

/**
 * Interface that represents a card
 */
public interface Card {
	String[] PLAYERS = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
	String[] WEAPONS = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
	String[] ROOMS = {"Kitchen", "Ball Room", "Conservatory", "Billiard Room", "Library", "Study", "Hall", "Lounge", "Dining Room"};

	// constants for drawing
	static final int WIDTH = 110;
	static final int HEIGHT = 160;
	static final int OUTER_PADDING = 10;
	static final int INNER_PADDING = 10;

	public String getName();
	public Image getIcon();
}
