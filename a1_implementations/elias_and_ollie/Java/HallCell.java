package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/


// line 11 "model.ump"
// line 80 "model.ump"
public class HallCell extends Cell {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //HallCell Attributes
    private boolean isEntrance;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public HallCell(int row, int col, boolean aIsEntrance) {
        super(row, col);
        isEntrance = aIsEntrance;
    }

    //------------------------
    // INTERFACE
    //------------------------

    /**
     * Sets this HallCell as an entrance. For board reading purposes.
     *
     * @param aIsEntrance contains what to set the isEntrance field to
     * @return whether this was set successfully.
     */
    public boolean setIsEntrance(boolean aIsEntrance) {
        boolean wasSet = false;
        isEntrance = aIsEntrance;
        wasSet = true;
        return wasSet;
    }

    /**
     * @return whether the board is an entrance cell (true/false).
     */
    public boolean getIsEntrance() {
        return isEntrance;
    }

    public String toString() {
        return super.toString() + "[" +
                "isEntrance" + ":" + getIsEntrance() + "]";
    }
}