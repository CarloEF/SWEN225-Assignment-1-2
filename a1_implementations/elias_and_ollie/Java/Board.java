package Java;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5074.a43557235 modeling language!*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Java.Card.*;

public class Board {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    public static String[] printedBoard;

    //Board Associations
    private Cell[][] cells;
    private Map<Card, Item> items;
    private Map<RoomCard, Set<RoomCell>> cellsPerRoom;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Board(File dataFile, File printedBoardFile) {

        // creates room objects and associates cell sets with them
        initCells(dataFile, printedBoardFile);

        items = new HashMap<>();

        // create player objects in their starting positions
        items.put(PlayerCard.COLONEL_MUSTARD, new Player(cells[17][0], PlayerCard.COLONEL_MUSTARD, "CM"));
        items.put(PlayerCard.PROFESSOR_PLUM, new Player(cells[19][23], PlayerCard.PROFESSOR_PLUM, "PP"));
        items.put(PlayerCard.REVEREND_GREEN, new Player(cells[0][14], PlayerCard.REVEREND_GREEN, "RG"));
        items.put(PlayerCard.MRS_PEACOCK, new Player(cells[6][23], PlayerCard.MRS_PEACOCK, "MP"));
        items.put(PlayerCard.MISS_SCARLET, new Player(cells[24][7], PlayerCard.MISS_SCARLET, "MS"));
        items.put(PlayerCard.MRS_WHITE, new Player(cells[0][9], PlayerCard.MRS_WHITE, "MW"));

        // shuffles rooms so weapon placement is randomised each game
        List<RoomCard> shuffledRooms = new ArrayList<>(Arrays.asList(RoomCard.values()));
        Collections.shuffle(shuffledRooms);

        // add the weapons at null cells
        for (WeaponCard weaponCard : WeaponCard.values()) {
            String printString = weaponCard.name().substring(0, 2);
            items.put(weaponCard, new Weapon(cells[0][0], weaponCard, printString));
        }

        // then give the weapons cells in random rooms
        for (int i=0; i<WeaponCard.values().length; i++) {
            moveItemToRoom(WeaponCard.values()[i], shuffledRooms.get(i));
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    /**
     * Moves an item into a random cell in a given room.
     *
     * @param itemName contains the item card of the item we want to move into a room.
     * @param roomName contains the RoomCard of the room we want the item moved to.
     */
    public void moveItemToRoom(Card itemName, RoomCard roomName) {

        if (itemName.getClass() == RoomCard.class)
            return;

        List<RoomCell> cellsInRoom = new ArrayList<>(cellsPerRoom.get(roomName));
        Collections.shuffle(cellsInRoom);

        for (RoomCell cell : cellsInRoom) {
            if (!cell.doesContainItem() && !cell.getIsDoor()) {
                items.get(itemName).moveToCell(cell);
                break;
            }
        }
    }

    /**
     * Reads two files and initialises a 2D Array of Cells based on the files.
     * This 2D Array of Cells becomes the "Board" the players move on.
     *
     * @param dataFile contains a text file of the board with more information for forming rooms.
     * @param printedBoardFile contains a text file of the board as printed to the players, for creating doors/entrances
     */
    public void initCells(File dataFile, File printedBoardFile) {
        try {
            cellsPerRoom = new HashMap<>();
            Scanner sc = new Scanner(dataFile);
            cells = new Cell[25][24];

            int row = 0;

            while (sc.hasNext()) {
                int col = 0;
                String line = sc.nextLine();
                for (int i = 0; i < 48; i += 2) {
                    char c1 = line.charAt(i);
                    char c2 = line.charAt(i + 1);
                    if (c1 == 'R' || c1 == 'D') {
                        RoomCard roomType = RoomCard.values()[Character.getNumericValue(c2)];
                        RoomCell roomCell = new RoomCell(row, col,c1 == 'D', roomType);
                        cells[row][col] = roomCell;

                        // also add the RoomCell to our map of room names to room cells
                        if (!cellsPerRoom.containsKey(roomType))
                            cellsPerRoom.put(roomType, new LinkedHashSet<>());
                        cellsPerRoom.get(roomType).add(roomCell);

                    } else if (c1 == '░' || c1 == '▒') {
                        cells[row][col] = new HallCell(row, col, c1 == '▒');
                    } else {
                        cells[row][col] = new EmptyCell(row, col);
                    }
                    col++;
                }
                row++;
            }

            sc = new Scanner(printedBoardFile);
            printedBoard = new String[25];
            row = 0;
            while (sc.hasNext()) {
                printedBoard[row++] = sc.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("No valid file found");
            e.printStackTrace();
        }
    }

    /**
     * Print the board and notebook of a given player
     * @param player The player whose notebook we're printing
     */
    public void printBoardAndNotebook(Player player) {

        // construct a StringBuilder array from String literal array
        StringBuilder[] output = new StringBuilder[printedBoard.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = new StringBuilder(printedBoard[i]);
        }

        // replace player/weapon cells with their Strings
        try {
            for (Item w : items.values())
                replaceCell(w.getCell().getRow(), w.getCell().getCol(), w.getPrintString(), output);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // draw X or ? on the player's notebook
        Card[] cardNames = Card.values();
        int offset = 2;
        for (int i = 0; i < cardNames.length; i++) {
            output[i + offset].append(player.knowsAboutCard(cardNames[i]) ? "X" : "?");

            if (i != cardNames.length - 1 && cardNames[i].getClass() != cardNames[i+1].getClass())
                offset++;
        }

        for (StringBuilder sb : output)
            System.out.println(sb);
    }

    /**
     * Replace a given cell in the StringBuilder array with a new String
     *
     * @param row         The row to make the replacement
     * @param col         The col index of the cell (NOT AN INDEX INTO THE STRING)
     * @param replacement The String to replace (must be 2 characters long)
     * @param text        The StringBuilder array to make the replacement on
     * @throws Exception If the String is not 2 characters long
     */
    private static void replaceCell(int row, int col, String replacement, StringBuilder[] text) throws Exception {
        if (replacement.length() != 2)
            throw new Exception("Wrong sized replacement String");
        text[row].replace(col * 2, col * 2 + replacement.length(), replacement);
    }

    /**
     * Gets a cell from a given row and column
     *
     * @param row the row to get the cell from.
     * @param col the column to get the cell from.
     * @return the cell at (row, col).
     */
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean itemInRoom(Card itemName, RoomCard roomName) {
        return itemName.getClass() == RoomCard.class && items.get(itemName).getRoomName() == roomName;
    }

    /**
     * Gets a Player object from their PlayerCard.
     *
     * @param cardName the PlayerCard of the Player we want.
     * @return the Player associated to that PlayerCard.
     */
    public Player getPlayer(PlayerCard cardName) {
        return (Player)items.get(cardName);
    }

}