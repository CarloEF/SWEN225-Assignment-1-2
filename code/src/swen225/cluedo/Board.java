package swen225.cluedo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

import GUI.GUI;

/**
 * Class representing the board
 */
public class Board {

    Tile[][] board;
    int width;
    int height;
    Game game;

    // tiles and rooms that the current player can move to
    Set<Tile> validTiles = new HashSet<Tile>();
    Set<Room> validRooms = new HashSet<Room>();

    /**
     * Constructs the board
     *
     * @param width  of the board
     * @param height of the board
     */
    public Board(Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;

        this.board = new Tile[width][height];
    }

    /**
     * Clear valid rooms and tiles once player has moved
     */
    public void clearValidRoomsAndTiles() {
        validTiles.clear();
        validRooms.clear();
    }

    /**
     * Moves a player to a room
     *
     * @param player  The player to move
     * @param newRoom The room to move to
     */
    public void movePlayer(Player player, Room newRoom) {
        player.moveToRoom(newRoom);
    }

    /**
     * Moves a weapon to a room
     *
     * @param weapon  The weapon to move
     * @param newRoom The room to move to
     */
    public void moveWeapon(Weapon weapon, Room newRoom) {
        weapon.moveToRoom(newRoom);
    }

    /**
     * Sets the board's rooms and walls from strings
     *
     * @param roomText - text representing the tiles
     * @param wallText - bitfield text representing the walls on the tiles
     * @param rooms    - a map containing the rooms
     */
    public void setBoard(String roomText, String wallText, Map<String, Room> rooms) {
        Scanner scan = new Scanner(wallText);
        scan.useDelimiter("/");

        for (int i = 0, len = roomText.length(); i < len; i++) {
            int wallNum = scan.nextInt();

            boolean left = false;
            boolean up = false;
            boolean right = false;
            boolean down = false;

            if ((wallNum & 1) == 1) {
                left = true;
            }
            if ((wallNum & 2) == 2) {
                up = true;
            }
            if ((wallNum & 4) == 4) {
                right = true;
            }
            if ((wallNum & 8) == 8) {
                down = true;
            }

            char room = roomText.charAt(i);

            int x = i % width;
            int y = Math.floorDiv(i, width);

            Tile tile = null;

            switch (room) {
                case 'i':
                    tile = new InaccessibleTile(x, y);
                    break;
                case 'h':
                    tile = new HallwayTile(up, down, left, right, x, y);
                    break;
                case 'k':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Kitchen"));
                    break;
                case 'b':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Ball Room"));
                    break;
                case 'c':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Conservatory"));
                    break;
                case 'd':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Dining Room"));
                    break;
                case 'l':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Library"));
                    break;
                case 'm':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Billiard Room"));
                    break;
                case 'a':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Hall"));
                    break;
                case 's':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Study"));
                    break;
                case 'o':
                    tile = new RoomTile(up, down, left, right, x, y, rooms.get("Lounge"));
                    break;
                default:
                    System.err.println("Invalid tile - " + room);
            }

            board[x][y] = tile;
        }
        scan.close();
    }

    /**
     * Gets all valid moves and adds them to sets
     *
     * @param diceRoll - the number of moves to use, determined by a dice roll
     * @param player   - the player to move
     */
    public void getValidMoves(int diceRoll, Player player) {

        Tile playerTile = player.getTile();

        Stack<Tile> visitedTiles = new Stack<Tile>();

        // if player is in a room, they can leave from any of the exits
        if (playerTile instanceof RoomTile) {

            Room room = ((RoomTile) playerTile).getRoom();

            visitedTiles.addAll(room.getExitTiles());
        } else {
            visitedTiles.add(playerTile);
        }

        validMove(0, diceRoll, visitedTiles);
    }

    /**
     * Recursive method to determine whether move is valid
     *
     * @param moveNum    - number of moves used so far
     * @param diceRoll   - the number of moves to use, determined by a dice roll
     * @param visited    - a stack of tiles already visited
     * @param validTiles - the set which all the valid tiles are added to
     * @param validRooms - the set which all the valid rooms are added to
     */
    private void validMove(int moveNum, int diceRoll, Stack<Tile> visited) {
        Tile lastTile = visited.peek();

        // if used up all moves, current tile is a valid tile so add to set and stop
        if (moveNum == diceRoll) {
            validTiles.add(lastTile);
            return;
        }

        // see if we can go to upper tile
        // can't go to invalid tiles or through walls
        if (lastTile.getY() > 0 && !lastTile.hasUpWall()) {
            Tile upperTile = getTile(lastTile.getX(), lastTile.getY() - 1);

            if (upperTile instanceof RoomTile) {
                validRooms.add(((RoomTile) upperTile).getRoom());
            }

            // can't access inaccessible tiles or already visited tiles
            // also can't go through room tiles
            if (upperTile.isAccessible() && !visited.contains(upperTile)) {
                visited.add(upperTile);

                validMove(moveNum + 1, diceRoll, visited);
                visited.pop();
            }
        }

        // see if we can go to lower tile
        // can't go to invalid tiles or through walls
        if (lastTile.getY() < height - 1 && !lastTile.hasDownWall()) {
            Tile lowerTile = getTile(lastTile.getX(), lastTile.getY() + 1);

            if (lowerTile instanceof RoomTile) {
                validRooms.add(((RoomTile) lowerTile).getRoom());
            }

            // can't access inaccessible tiles or already visited tiles
            if (lowerTile.isAccessible() && !visited.contains(lowerTile)) {
                visited.add(lowerTile);

                validMove(moveNum + 1, diceRoll, visited);
                visited.pop();
            }
        }

        // see if we can go to left tile
        // can't go to invalid tiles or through walls
        if (lastTile.getX() > 0 && !lastTile.hasLeftWall()) {
            Tile leftTile = getTile(lastTile.getX() - 1, lastTile.getY());

            if (leftTile instanceof RoomTile) {
                validRooms.add(((RoomTile) leftTile).getRoom());
            }

            // can't access inaccessible tiles or already visited tiles
            if (leftTile.isAccessible() && !visited.contains(leftTile)) {
                visited.add(leftTile);

                validMove(moveNum + 1, diceRoll, visited);
                visited.pop();
            }
        }

        // see if we can go to right tile
        // can't go to invalid tiles or through walls
        if (lastTile.getX() < width - 1 && !lastTile.hasRightWall()) {
            Tile rightTile = getTile(lastTile.getX() + 1, lastTile.getY());

            if (rightTile instanceof RoomTile) {
                validRooms.add(((RoomTile) rightTile).getRoom());
            }

            // can't access inaccessible tiles or already visited tiles
            if (rightTile.isAccessible() && !visited.contains(rightTile)) {
                visited.add(rightTile);

                validMove(moveNum + 1, diceRoll, visited);
                visited.pop();
            }
        }
    }

    /**
     * Returns the tile at a point (x, y) returns null if invalid x or y
     *
     * @param x The x position of the Tile to get
     * @param y The y position of the Tile to get
     * @return The Tile at that position, or null if there isn't one
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return board[x][y];
    }


    public Set<Tile> getValidTiles() {
        return validTiles;
    }

    public Set<Room> getValidRooms() {
        return validRooms;
    }
}
