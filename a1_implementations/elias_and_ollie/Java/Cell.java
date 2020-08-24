package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/

public class Cell {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    private boolean containsItem = false;
    private int row, col;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    //------------------------
    // INTERFACE
    //------------------------

    /**
     * @return the row the cell is in
     */
    public int getRow() {return row;}

    /**
     * @return the column the cell is in
     */
    public int getCol() {return col;}

    /**
     * @return whether this cell contains an item
     */
    public boolean doesContainItem() {
        return containsItem;
    }

    /**
     * Sets the room's containsItem field.
     * @param containsItem contains the boolean to set the field to.
     */
    public void setContainsItem(boolean containsItem) {
        this.containsItem = containsItem;
    }

}