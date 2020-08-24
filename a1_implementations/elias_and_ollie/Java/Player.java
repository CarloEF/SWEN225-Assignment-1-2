package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/

import java.util.*;
import Java.Card.*;

public class Player extends Item {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Player Associations

    private Set<Card> hand;
    private Set<Card> notepad;

    private boolean isOut;          // player is out of the game if they've made an incorrect accusation
    private boolean wasTeleported;  // player was teleported into their current room < 1 turn ago

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Player(Cell cell, PlayerCard cardName, String printString) {
        super(cell, cardName, printString);

        hand = new HashSet<>();
        notepad = new HashSet<>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    /**
     * Deals a card to this player's hand.
     * Also adds it to their notepad, as they know it isn't one of the murder circumstances.
     * @param card contains the card to add to the hand/notepad.
     */
    public void dealCard(Card card) {
        hand.add(card);
        notepad.add(card);
    }

    /**
     * Returns whether this player has a given card in their hand.
     *
     * @param cardName contains the card we're checking against.
     * @return whether the card cardName exists in this player's hand (true or false).
     */
    public boolean holdsCard(Card cardName) {
        return hand.contains(cardName);
    }

    /**
     * Returns whether this player has a given card in their notepad.
     *
     * @param cardName contains the card we're checking against.
     * @return whether the card cardName exists in this player's notepad (true or false).
     */
    public boolean knowsAboutCard(Card cardName) { return notepad.contains(cardName); }

    /**
     * Adds a given card to the notepad.
     *
     * @param card contains the card we want to add to the notepad.
     */
    public void addToNotepad(Card card) {
        notepad.add(card);
    }

    /**
     * @return whether the player is out of the game (true or false)
     */
    public boolean isOut() {
        return isOut;
    }

    /**
     * Sets the player as out of the game
     */
    public void setOut() {
        isOut = true;
    }

    /**
     * @return whether the player was teleported since their last turn (true or false).
     */
    public boolean wasTeleported() {
        return wasTeleported;
    }

    /**
     * Sets the wasTeleported field.
     * @param wasTeleported contains the boolean to set the field to.
     */
    public void setWasTeleported(boolean wasTeleported) {
        this.wasTeleported = wasTeleported;
    }

    public String toString() { return super.getCard().toString(); }

}