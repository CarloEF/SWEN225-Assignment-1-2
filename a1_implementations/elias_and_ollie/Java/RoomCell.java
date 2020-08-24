package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/

import Java.Card.RoomCard;

public class RoomCell extends Cell {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //RoomCell Attributes
    private boolean isDoor;
    private RoomCard room;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public RoomCell(int row, int col, boolean aIsDoor, RoomCard aRoom) {
        super(row, col);
        isDoor = aIsDoor;
        room = aRoom;
    }

    //------------------------
    // INTERFACE
    //------------------------

    /**
     * Sets the room as a door. For board reading purposes.
     *
     * @param aIsDoor contains the boolean to set isDoor to.
     * @return whether this was set successfully.
     */
    public boolean setIsDoor(boolean aIsDoor) {
        boolean wasSet = false;
        isDoor = aIsDoor;
        wasSet = true;
        return wasSet;
    }

    /**
     * Sets the room to be associated with this RoomCell. For board reading purposes.
     *
     * @param aRoom contains the room to be set to.
     * @return whether the room was set successfully.
     */
    public boolean setRoom(RoomCard aRoom) {
        boolean wasSet = false;
        room = aRoom;
        wasSet = true;
        return wasSet;
    }

    /**
     * @return whether or not the RoomCell is a door (true or false)
     */
    public boolean getIsDoor() {
        return isDoor;
    }

    /**
     * @return the RoomCard associated with this RoomCell
     */
    public RoomCard getRoom() {
        return room;
    }

    public String toString() {
        return super.toString() + "[" +
                "isDoor" + ":" + getIsDoor() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "room" + "=" + (getRoom() != null ? !getRoom().equals(this) ? getRoom().toString().replaceAll("  ", "    ") : "this" : "null");
    }
}