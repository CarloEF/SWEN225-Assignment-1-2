package swen225.cluedo;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * Class representing the board
 * 
 */
public class Board {
	Tile[][] board;
	int width;
	int height;

	/**
	 * Constructs the board
	 * 
	 * @param width of the board
	 * @param height of the board
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;

		this.board = new Tile[width][height];
	}

	/**
	 * Moves a player to a tile, assumes all checks have been done
	 * 
	 * @param player
	 * @param newX
	 * @param newY
	 */
	public void movePlayer(Player player, int newX, int newY) {
		Tile newTile = getTile(newX, newY);

		player.moveToTile(newTile);
	}

	/**
	 * Moves a player to a room
	 * @param player
	 * @param newRoom
	 */
	public void movePlayer(Player player, Room newRoom) {
		player.moveToRoom(newRoom);
	}

	/**
	 * Moves a weapon to a room
	 * @param weapon
	 * @param newRoom
	 */
	public void moveWeapon(Weapon weapon, Room newRoom) {
		weapon.moveToRoom(newRoom);
	}

	/**
	 * Sets the board's rooms and walls from strings
	 * @param roomText - text representing the tiles
	 * @param wallText - bitfield text representing the walls on the tiles
	 * @param rooms - a map containing the rooms
	 */
	public void setBoard(String roomText, String wallText, Map<String, Room> rooms) {
		Scanner scan = new Scanner(wallText);
		scan.useDelimiter("/");

		for (int i=0, len=roomText.length();i<len;i++) {
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

			switch(room) {
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

		//System.out.println(this);
	}

	/**
	 * Gets all valid moves and adds them to sets
	 * @param diceRoll - the number of moves to use, determined by a dice roll
	 * @param player - the player to move
	 * @param validTiles - the set which all the valid tiles are added to
	 * @param validRooms - the set which all the valid rooms are added to
	 * @return
	 */
	public void getValidMoves(int diceRoll, Player player, Set<Tile> validTiles, Set<Room> validRooms) {

		Tile playerTile = player.getTile();

		Stack<Tile> visitedTiles = new Stack<Tile>();

		//if player is in a room, they can leave from any of the exits
		if (playerTile instanceof RoomTile) {

			Room room = ((RoomTile)playerTile).getRoom();

			visitedTiles.addAll(room.getExitTiles());
		} else {
			visitedTiles.add(playerTile);
		}

		validMove(0, diceRoll, visitedTiles, validTiles, validRooms);
	}

	/**
	 * Recursive method to determine whether move is valid
	 * @param moveNum - number of moves used so far
	 * @param diceRoll - the number of moves to use, determined by a dice roll
	 * @param visited - a stack of tiles already visited
	 * @param validTiles - the set which all the valid tiles are added to
	 * @param validRooms - the set which all the valid rooms are added to
	 * @return
	 */
	private void validMove(int moveNum, int diceRoll, Stack<Tile> visited, Set<Tile> validTiles, Set<Room> validRooms) {
		Tile lastTile = visited.peek();

		//if used up all moves, current tile is a valid tile so add to set and stop
		if (moveNum == diceRoll) {
			validTiles.add(lastTile);
			return;
		}

		//see if we can go to upper tile
		//can't go to invalid tiles or through walls
		if (lastTile.getY() > 0 && !lastTile.hasUpWall()) {
			Tile upperTile = getTile(lastTile.getX(), lastTile.getY()-1);

			if (upperTile instanceof RoomTile) {
				validRooms.add(((RoomTile)upperTile).getRoom());
			}

			//can't access inaccessible tiles or already visited tiles
			//also can't go through room tiles
			if (upperTile.isAccessible() && !visited.contains(upperTile)) {
				visited.add(upperTile);

				validMove(moveNum+1, diceRoll, visited, validTiles, validRooms);
				visited.pop();
			}
		}

		//see if we can go to lower tile
		//can't go to invalid tiles or through walls
		if (lastTile.getY() < height-1 && !lastTile.hasDownWall()) {
			Tile lowerTile = getTile(lastTile.getX(), lastTile.getY()+1);

			if (lowerTile instanceof RoomTile) {
				validRooms.add(((RoomTile)lowerTile).getRoom());
			}

			//can't access inaccessible tiles or already visited tiles
			if (lowerTile.isAccessible() && !visited.contains(lowerTile)) {
				visited.add(lowerTile);

				validMove(moveNum+1, diceRoll, visited, validTiles, validRooms);
				visited.pop();
			}
		}

		//see if we can go to left tile
		//can't go to invalid tiles or through walls
		if (lastTile.getX() > 0 && !lastTile.hasLeftWall()) {
			Tile leftTile = getTile(lastTile.getX()-1, lastTile.getY());

			if (leftTile instanceof RoomTile) {
				validRooms.add(((RoomTile)leftTile).getRoom());
			}

			//can't access inaccessible tiles or already visited tiles
			if (leftTile.isAccessible() && !visited.contains(leftTile)) {
				visited.add(leftTile);

				validMove(moveNum+1, diceRoll, visited, validTiles, validRooms);
				visited.pop();
			}
		}

		//see if we can go to right tile
		//can't go to invalid tiles or through walls
		if (lastTile.getX() < width-1 && !lastTile.hasRightWall()) {
			Tile rightTile = getTile(lastTile.getX()+1, lastTile.getY());

			if (rightTile instanceof RoomTile) {
				validRooms.add(((RoomTile)rightTile).getRoom());
			}

			//can't access inaccessible tiles or already visited tiles
			if (rightTile.isAccessible() && !visited.contains(rightTile)) {
				visited.add(rightTile);

				validMove(moveNum+1, diceRoll, visited, validTiles, validRooms);
				visited.pop();
			}
		}
	}

	/**
	 * Returns the tile at a point (x, y)
	 * returns null if invalid x or y
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return null;
		}
		return board[x][y];
	}

	/**
	 * Draws the board
	 */
	public String toString() {
		//string that contains the board
		String text = "";
		for (int y=0;y<height;y++) {
			//adds horizontal wall
			text += "    ";
			for (int x=0;x<width;x++) {
				if (board[x][y].hasUpWall()) {
					text += "-";
				} else {
					text += " ";
				}

				text += " ";
			}
			text += "\n";

			//add y coords on left
			text += (height-y);
			if (height-y < 10) {
				text += " ";
			}
			text += " ";
			//adds walls in between tiles
			for (int x=0;x<width;x++) {
				if (board[x][y].hasLeftWall()) {
					text += "|";
				} else {
					text += " ";
				}
				text += board[x][y];
			}
			//adds the walls on the right if they are there
			if (board[width-1][y].hasRightWall()) {
				text += "|";
			}
			text += "\n";
		}

		//adds the walls on the bottom
		text += "    ";
		for (int x=0;x<width;x++) {
			if (board[x][height-1].hasDownWall()) {
				text += "-";
			} else {
				text += " ";
			}
			text += " ";
		}

		text += "\n";

		text += "    ";
		//add numbers on the bottom
		for (int i=0;i<width;i++) {
			if (i+1 < 10) {
				text += (i+1) + " ";
			} else {
				text += i+1;
			}
		}
		return text;
	}

}
