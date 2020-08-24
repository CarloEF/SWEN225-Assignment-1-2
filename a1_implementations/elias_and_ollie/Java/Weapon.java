package Java;
import Java.Card.WeaponCard;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/


import java.util.HashSet;

// line 33 "model.ump"
// line 103 "model.ump"
public class Weapon extends Item {

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Weapon(Cell cell, WeaponCard cardName, String printString) {
        super(cell, cardName, printString);
    }

    //------------------------
    // INTERFACE
    //------------------------

    public String toString() { return super.getCard().toString(); }

    /**
     *  Returns a lowercase string, to represent the weapon on the board with.
     */
    public String getPrintString() { return super.getPrintString().toLowerCase(); }
}