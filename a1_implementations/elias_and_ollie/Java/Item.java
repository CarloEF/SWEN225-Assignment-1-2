package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/

public class Item {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Item Associations
    private Cell cell;
    private Card card;
    private String printString;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Item(Cell cell, Card card, String printString) {
        this.cell = cell;
        this.card = card;
        this.printString = printString;
        this.cell.setContainsItem(true);
    }

    //------------------------
    // INTERFACE
    //------------------------

    public String toString() { return printString; }

    public String getPrintString() { return printString; }

    public Cell getCell() {
        return cell;
    }

    public Card getCard() {
        return card;
    }

    /**
     * Moves the item from its current cell to a new given cell.
     * Additionally, updates the cells to correctly know whether they contain an item.
     *
     * @param cell contains the cell to move this item to
     */
    public void moveToCell(Cell cell) {
        this.cell.setContainsItem(false);
        this.cell = cell;
        cell.setContainsItem(true);
    }

    /**
     * Gets the card associated to the room this item is in.
     * This converts the item's cell information into a RoomCard, to be used elsewhere. If the cell of the item is not
     * a RoomCell, it returns null.
     *
     * @return The CardName of the room (or null for a non-room cell, e.g. hall)
     */
    public Card.RoomCard getRoomName() {
        if (cell.getClass() == RoomCell.class) {
            return ((RoomCell) cell).getRoom();
        }
        return null;
    }

}