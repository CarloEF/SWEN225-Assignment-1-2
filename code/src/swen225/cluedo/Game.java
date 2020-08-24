package swen225.cluedo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
// Hello, World!
/**
 * Game class that handles all input/output and the game logic
 *
 */
public class Game {
	
	/*************
	 * CONSTANTS *
	 *************/
	public static final int BOARD_WIDTH = 24;
	public static final int BOARD_HEIGHT = 25;
	
	//player order clockwise around the board
	public static final String[] PLAYER_ORDER = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
	
	//i = inaccessible
	//h = hallway
	//k = kitchen
	//b = ball room
	//c = conservatory
	//d = dining room
	//l = library
	//m = billiard room(too many letter conflicts)
	//a = hall
	//s = study
	//o = lounge
	public static final String ROOM_BOARD = "iiiiiiiiihiiiihiiiiiiiii" + 
									 		"kkkkkkihhhbbbbhhhicccccc" + 
									 		"kkkkkkhhbbbbbbbbhhcccccc" + 
									 		"kkkkkkhhbbbbbbbbhhcccccc" + 
											"kkkkkkhhbbbbbbbbhhcccccc" + 
											"kkkkkkhhbbbbbbbbhhhcccci" +
											"ikkkkkhhbbbbbbbbhhhhhhhh" +
											"hhhhhhhhbbbbbbbbhhhhhhhi" +
											"ihhhhhhhhhhhhhhhhhmmmmmm" +
											"dddddhhhhhhhhhhhhhmmmmmm" +
											"ddddddddhhiiiiihhhmmmmmm" +
											"ddddddddhhiiiiihhhmmmmmm" + 
											"ddddddddhhiiiiihhhmmmmmm" + 
											"ddddddddhhiiiiihhhhhhhhi" +
											"ddddddddhhiiiiihhhllllli" +
											"ddddddddhhiiiiihhlllllll" +
											"ihhhhhhhhhiiiiihhlllllll" +
											"hhhhhhhhhhhhhhhhhlllllll" +
											"ihhhhhhhhaaaaaahhhllllli" +
											"ooooooohhaaaaaahhhhhhhhh" +
											"ooooooohhaaaaaahhhhhhhhi" +
											"ooooooohhaaaaaahhsssssss" + 
											"ooooooohhaaaaaahhsssssss" + 
											"ooooooohhaaaaaahhsssssss" + 
											"ooooooihiaaaaaaihissssss";
	
	//bitwise flag for walls with / as delimiter
	//1 for left, 2 for up, 4 for right, 8 for down
	//convention is walls are on both tiles they are connected to
	public static final String WALL_BOARD = "0/0/0/0/0/0/0/0/0/7/0/0/0/0/7/0/0/0/0/0/0/0/0/0/" + 
											"3/2/2/2/2/6/0/3/10/12/3/2/2/6/9/10/6/0/3/2/2/2/2/6/" +
											"1/0/0/0/0/4/3/4/3/2/0/0/0/0/2/6/1/6/1/0/0/0/0/4/" +
											"1/0/0/0/0/4/1/4/1/0/0/0/0/0/0/4/1/4/1/0/0/0/0/4/" +
											"1/0/0/0/0/4/1/4/1/0/0/0/0/0/0/4/1/4/1/0/0/0/0/12/" +
											"9/0/0/0/0/4/1/0/0/0/0/0/0/0/0/0/0/0/4/9/8/8/12/0/" +
											"0/9/8/8/0/12/1/4/1/0/0/0/0/0/0/4/1/0/0/2/2/2/2/14/" +
											"11/2/2/2/0/2/0/4/9/0/8/8/8/8/0/12/1/0/8/8/8/8/12/0/" +
											"0/9/8/8/8/0/0/0/2/0/2/2/2/2/0/2/0/4/3/2/2/2/2/6/" +
											"3/2/2/2/6/9/8/8/0/0/8/8/8/8/8/0/0/0/0/0/0/0/0/4/" +
											"1/0/0/0/0/2/2/6/1/4/3/2/2/2/6/1/0/4/1/0/0/0/0/4/" +
											"1/0/0/0/0/0/0/4/1/4/1/0/0/0/4/1/0/4/1/0/0/0/0/4/" +
											"1/0/0/0/0/0/0/0/0/4/1/0/0/0/4/1/0/4/9/8/8/8/0/12/" +
											"1/0/0/0/0/0/0/4/1/4/1/0/0/0/4/1/0/0/10/10/2/10/12/0/" +
											"1/0/0/0/0/0/0/4/1/4/1/0/0/0/4/1/0/12/3/2/0/2/6/0/" +
											"9/8/8/8/8/8/0/12/1/4/1/0/0/0/4/1/4/3/0/0/0/0/0/6/" +
											"0/3/2/2/2/2/0/2/0/4/9/8/8/8/12/1/0/0/0/0/0/0/0/4/" +
											"11/0/0/0/0/0/0/0/0/8/10/2/2/10/10/0/4/9/0/0/0/0/0/12/" +
											"0/9/8/8/8/8/0/0/4/3/2/0/0/2/6/1/0/6/9/8/8/8/12/0/" +
											"3/2/2/2/2/2/4/1/4/1/0/0/0/0/4/1/0/0/2/2/2/2/2/14/" +
											"1/0/0/0/0/0/4/1/4/1/0/0/0/0/0/0/0/0/8/8/8/8/12/0/" +
											"1/0/0/0/0/0/4/1/4/1/0/0/0/0/4/1/4/1/2/2/2/2/2/6/" +
											"1/0/0/0/0/0/4/1/4/1/0/0/0/0/4/1/4/1/0/0/0/0/0/4/" + 
											"1/0/0/0/0/0/12/1/12/1/0/0/0/0/4/9/4/9/0/0/0/0/0/4/" +
											"9/8/8/8/8/12/0/13/0/9/8/8/8/8/12/0/13/0/9/8/8/8/8/12/";
	
	//stores the number of players
	int playerNum;
	
	List<Card> cards;
	
	Board board;
	
	Map<String, Player> players;
	Map<String, Weapon> weapons;
	Map<String, Room> rooms;
	
	Player murderer = null;
	Weapon murderWeapon = null;
	Room murderRoom = null;
	
	//list of only players that are playing in order 
	List<Player> playersOrdered;
	
	boolean isRunning = true;
	
	//get all input from this scanner
	Scanner input;
	
	// Constructs the game
	public Game() {
		cards = new ArrayList<Card>();
		
		players = new HashMap<String, Player>();
		weapons = new HashMap<String, Weapon>();
		rooms = new HashMap<String, Room>();
		
		playersOrdered = new ArrayList<Player>();
		
		input = new Scanner(System.in);
	}

	/**
	 * Main method, initiates the game and starts it
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.startGame();
		
	}
	
	/**
	 * Initiates the board
	 */
	private void initBoard() {
		board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
		
		board.setBoard(ROOM_BOARD, WALL_BOARD, rooms);
	}
	
	/**
	 * Initiates the cards
	 */
	private void initCards() {
		
		addPlayer(new Player("Miss Scarlett", "S"));
		addPlayer(new Player("Col. Mustard", "M"));
		addPlayer(new Player("Mrs. White", "W"));
		addPlayer(new Player("Mr. Green", "G"));
		addPlayer(new Player("Mrs. Peacock", "P"));
		addPlayer(new Player("Prof. Plum", "p"));
		
		addWeapon(new Weapon("Candlestick", "C"));
		addWeapon(new Weapon("Dagger", "D"));
		addWeapon(new Weapon("Lead Pipe", "L"));
		addWeapon(new Weapon("Revolver", "R"));
		addWeapon(new Weapon("Rope", "r"));
		addWeapon(new Weapon("Spanner", "s"));
		
		addRoom(new Room("Kitchen", "K"));
		addRoom(new Room("Ball Room", "B"));
		addRoom(new Room("Conservatory", "C"));
		addRoom(new Room("Billiard Room", "b"));
		addRoom(new Room("Library", "L"));
		addRoom(new Room("Study", "S"));
		addRoom(new Room("Hall", "H"));
		addRoom(new Room("Lounge", "l"));
		addRoom(new Room("Dining Room", "D"));
		
	}
	
	/**
	 * Adds a player to collections
	 * @param player
	 */
	private void addPlayer(Player player) {
		cards.add(player);
		players.put(player.getName(), player);
	}
	
	/**
	 * Adds a weapon to collections
	 * @param weapon
	 */
	private void addWeapon(Weapon weapon) {
		cards.add(weapon);
		weapons.put(weapon.getName(), weapon);
	}
	
	/**
	 * Adds a room to collections
	 * @param room
	 */
	private void addRoom(Room room) {
		cards.add(room);
		rooms.put(room.getName(), room);
	}
	
	/**
	 * Adds the room exits
	 */
	private void addRoomExits() {
		rooms.get("Kitchen").addExitTile(board.getTile(4, 6));
		
		rooms.get("Ball Room").addExitTile(board.getTile(8, 5));
		rooms.get("Ball Room").addExitTile(board.getTile(9, 7));
		rooms.get("Ball Room").addExitTile(board.getTile(14, 7));
		rooms.get("Ball Room").addExitTile(board.getTile(15, 5));
		
		rooms.get("Conservatory").addExitTile(board.getTile(18, 4));
		
		rooms.get("Billiard Room").addExitTile(board.getTile(18, 9));
		rooms.get("Billiard Room").addExitTile(board.getTile(22, 12));
		
		rooms.get("Library").addExitTile(board.getTile(20, 14));
		rooms.get("Library").addExitTile(board.getTile(17, 16));
		
		rooms.get("Study").addExitTile(board.getTile(17, 21));

		rooms.get("Hall").addExitTile(board.getTile(14, 20));
		rooms.get("Hall").addExitTile(board.getTile(12, 18));
		rooms.get("Hall").addExitTile(board.getTile(11, 18));
		
		rooms.get("Lounge").addExitTile(board.getTile(6, 19));

		rooms.get("Dining Room").addExitTile(board.getTile(6, 15));
		rooms.get("Dining Room").addExitTile(board.getTile(7, 12));
	}
	
	/**
	 * Sets the players to their starting positions
	 */
	private void setPlayerTiles() {
		players.get("Miss Scarlett").moveToTile(board.getTile(7, 24));
		players.get("Col. Mustard").moveToTile(board.getTile(0, 17));
		players.get("Mrs. White").moveToTile(board.getTile(9, 0));
		players.get("Mr. Green").moveToTile(board.getTile(14, 0));
		players.get("Mrs. Peacock").moveToTile(board.getTile(23, 6));
		players.get("Prof. Plum").moveToTile(board.getTile(23, 19));
	}
	
	/**
	 * Sets weapons to random rooms
	 */
	private void setWeaponTiles() {
		//copy to new list
		List<Room> roomsLeft = new ArrayList<Room>(rooms.values());
		List<Weapon> weaponsLeft = new ArrayList<Weapon>(weapons.values());
		
		while (weaponsLeft.size() > 0) {
			int random = (int)Math.floor(Math.random()*roomsLeft.size());
			
			board.moveWeapon(weaponsLeft.get(0), roomsLeft.get(random));
			
			roomsLeft.remove(random);
			weaponsLeft.remove(0);
		}
		
	}
	
	/**
	 * Handles all the logic at the start of the game
	 */
	public void startGame() {
		initCards();
		initBoard();
		addRoomExits();
		setPlayerTiles();
		setWeaponTiles();
		
		playerNum = getPlayerNum();
		
		orderPlayers();
		dealCards();
		
		//could be random
		int currentPlayerIndex = 0;
		
		while(isRunning) {
			Player currentPlayer = playersOrdered.get(currentPlayerIndex);
			startPlayerTurn(currentPlayer);
			
			currentPlayerIndex = (currentPlayerIndex + 1) % playerNum;
		}
	}
	
	/**
	 * Orders the players depending on how many players there are
	 */
	private void orderPlayers() {
		for (int i=0;i<playerNum;i++) {
			playersOrdered.add(players.get(PLAYER_ORDER[i]));
		}
	}
	
	/**
	 * Deals cards out, including picking the murder cards
	 */
	private void dealCards() {
		Collections.shuffle(cards);
		
		//take out 1 player, 1 weapon, 1 room cards for the murder circumstance
		int index = 0;
		while(murderer == null || murderWeapon == null || murderRoom == null) {
			Card card = cards.get(index);
			if (murderer == null && card instanceof Player) {
				murderer = (Player)card;
				cards.remove(index);
				continue;
			}
			if (murderWeapon == null && card instanceof Weapon) {
				murderWeapon = (Weapon)card;
				cards.remove(index);
				continue;
			}
			if (murderRoom == null && card instanceof Room) {
				murderRoom = (Room)card;
				cards.remove(index);
				continue;
			}
			index++;
		}
		
		//deal the other cards out
		for (int i=0, len=cards.size();i<len;i++) {
			Player player = playersOrdered.get(i % playerNum);
			
			player.addCard(cards.get(i));
		}
	}
	
	/**
	 * Gets input about the number of players.
	 * @return the number of players
	 */
	private int getPlayerNum() {
		
		System.out.println("How many players are playing?");
		
		//TODO catch potential errors on input
		int num = input.nextInt();
		
		while (num < 3 || num > 6) {
			System.out.println("Players(must be between 3 and 6):");
			
			num = input.nextInt();
		}
		
		return num;
	}
	
	/**
	 * Starts a player's turn, taking input
	 * @param player
	 */
	public void startPlayerTurn(Player player) {
		
		System.out.println("Board:");
		
		System.out.println(board + "\n");
		
		System.out.printf("%s's turn:\n", player.getName());
		
		int step1 = diceRoll();
		int step2 = diceRoll();
		
		int stepNum = step1 + step2;
		
		System.out.printf("You rolled a %d and a %d.\n", step1, step2);
		//player MUST make a move
		doMove(player, stepNum);
		
		Tile tile = player.getTile();
		if (tile instanceof RoomTile && player.canAccuse()) {
			//can do suggestion
			doSuggestion(player);
		}
	}
	
	/**
	 * Gets input and does the move
	 * Need to add more outputs
	 */
	private void doMove(Player player, int diceRoll) {
		//converts player coords from array indices to board coords
		int playerX = player.getTile().getX()+1;
		int playerY = BOARD_HEIGHT - player.getTile().getY();
		
		Set<Tile> validTiles = new HashSet<Tile>();
		Set<Room> validRooms = new HashSet<Room>();
		
		//gets all valid tiles and rooms the player can go to and puts them into the sets
		board.getValidMoves(diceRoll, player, validTiles, validRooms);
		
		if (validTiles.size() == 0 && validRooms.size() == 0) {
			System.out.println("You are blocked and cannot move!");
			return;
		}
		
		try {
			
			System.out.printf("You are at (%d %d) and have %d moves to use.\n", playerX, playerY, diceRoll);
			System.out.println("Where do you want to move (give in pairs of coords or room name):");
			
			String textInput;
			
			//catch and ignore nothing lines
			do {
				textInput = input.nextLine();
			} while (textInput.equals(""));
			
			String[] inputs = textInput.split(" ");
			
			if (inputs.length == 0) {
				System.out.println("Invalid input, please try again");
				doMove(player, diceRoll);
				//regex to check whether input is a number
			} else if (inputs.length == 2 && inputs[0].matches("\\d+")) {
				//coords
				int newX = Integer.parseInt(inputs[0]);
				int newY = Integer.parseInt(inputs[1]);
				
				//convert to board array indices
				newX--;
				newY = BOARD_HEIGHT - newY;
				
				Tile newTile = board.getTile(newX, newY);
				
				//check if tile is invalid
				if (newTile == null) {
					System.out.println("Invalid coordinates, please try again.");
					doMove(player, diceRoll);
					//check if they want to move to a room
				} else if (newTile instanceof RoomTile) {
					Room newRoom = ((RoomTile) newTile).getRoom();
					
					if (validRooms.contains(newRoom)) {
						board.movePlayer(player, newRoom);
					} else {
						System.out.println("Can't get to that room, please try again.");
						doMove(player, diceRoll);
					}
					//coords are at a valid hallway tile
				} else {
					if (validTiles.contains(newTile)) {
						board.movePlayer(player, newX, newY);
					} else {
						System.out.println("Can't get to that tile, please try again");
						doMove(player, diceRoll);
					}
				}
			} else {
				//must be a room
				
				//join all of line together, rooms are only 2 words max so this is sufficient
				if (inputs.length == 2) {
					textInput = String.join(" ", inputs[0], inputs[1]);
				} else {
					textInput = inputs[0];
				}
				
				//input is text, so check if it's a valid room
				if (rooms.containsKey(textInput)) {
					Room room = rooms.get(textInput);
					
					if (validRooms.contains(room)) {
						board.movePlayer(player, room);
					} else {
						System.out.println("Can't get to that room, please try again");
						doMove(player, diceRoll);
					}
				} else {
					System.out.println("Invalid room, please try again");
					doMove(player, diceRoll);
				}
			}
		} catch(InputMismatchException e) {
			System.out.println("Invalid input, please try again");
			doMove(player, diceRoll);
		}
	}
	
	/**
	 * 
	 * @param player
	 * @return - whether the suggestion was refuted
	 */
	private void doSuggestion(Player player) {
		//assumes player is in a roomtile
		Room room = ((RoomTile)player.getTile()).getRoom();
		
		System.out.printf("You(%s) are in room %s.\n", player.getName(), room.getName());
		
		List<Card> playerCards = player.getCards();
		String cardString = "";
		for (int i=0;i<playerCards.size();i++) {
			cardString += playerCards.get(i).getName();
			if (i != playerCards.size()-1) {
				cardString += ", ";
			}
		}
		System.out.printf("You have cards %s.\n", cardString);
				
		System.out.println("You can make a suggestion about the murder circumstances.");
		
		System.out.println("Who do you think the murderer was?");
		
		Player murdererSugg = getSuggestion(players);
		
		System.out.println("What weapon do you think was used to carry out the murder?");
		
		Weapon weaponSugg = getSuggestion(weapons);
		
		//move objects to the room
		board.movePlayer(murdererSugg, room);
		board.moveWeapon(weaponSugg, room);
		
		//go through all the players in order
		int playerIndex = playersOrdered.indexOf(player);
		
		boolean refuted = false;
		
		for (int index = (playerIndex + 1) % playerNum; index != playerIndex; index++, index %= playerNum) {
			Player currPlayer = playersOrdered.get(index);
			List<Card> refuteCards = currPlayer.getRefutes(murdererSugg, weaponSugg, room);
			
			if (refuteCards.size() == 0) {
				System.out.printf("%s cannot refute the murder suggestion.\n", currPlayer.getName());
			} else if (refuteCards.size() == 1) {
				System.out.printf("%s refuted the murder suggestion with %s.\n", currPlayer.getName(), refuteCards.get(0).getName());
				refuted = true;
				break;
			} else {
				
				System.out.printf("%s needs to choose a card to refute.\n", currPlayer.getName());
				System.out.println("Choose a card to use:");
				for (Card c : refuteCards) {
					System.out.printf("%s\n", c.getName());
				}
				
				String cardName;
				Card inputCard = null;
				
				do {
					cardName = input.nextLine();
					for (Card c : refuteCards) {
						if (c.getName().equals(cardName)) {
							inputCard = c;
							break;
						}
					}
				} while (inputCard == null);
				
				System.out.printf("%s refuted the murder suggestion with %s.\n", currPlayer.getName(), inputCard.getName());
				refuted = true;
				break;
			}
		}

		//now do accusation
		if (!refuted) {
			System.out.println("Suggestion was unrefuted, would you like to accuse?");
			
			if (getBooleanInput()) {
				if (murderRoom == room && murderer == murdererSugg && murderWeapon == weaponSugg) {
					System.out.println("Congratulations, you won!");
					input.close();
					isRunning = false;
				} else {
					System.out.println("Oops, that was not correct, you can no longer suggest/accuse");
					player.setCanAccuse(false);
					
					int playersLeft = 0;
					Player last = null;
					
					for (Player p : playersOrdered) {
						if (p.canAccuse()) {
							playersLeft++;
							last = p;
						}
					}
					
					if (playersLeft == 1) {
						//everyone else accused wrongly, so the last player wins
						System.out.printf("Everyone else accused incorrectly, so %s wins!\n", last.getName());
						System.out.printf("The murderer was %s with the %s in the room %s", murderer.getName(), murderWeapon.getName(), murderRoom.getName());
						input.close();
						isRunning = false;
					}
				}
			}
		}
	}
	
	/**
	 * Useful generics method to get input about a suggested murder circumstance
	 * @param <T>
	 * @param map
	 * @return
	 */
	private <T> T getSuggestion(Map<String, T> map) {
		String objName;
		T objSugg = null;
		
		do {
			//catch and ignore nothing lines
			do {
				objName = input.nextLine();
			} while (objName.equals(""));
			
			if (map.containsKey(objName)) {
				objSugg = map.get(objName);
			} else {
				System.out.println("Invalid object, please try again.");
			}
		} while (objSugg == null);
		
		return objSugg;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean getBooleanInput() {
		String[] yesInputs = {"y", "yes", "true", "t"};
		String[] noInputs = {"n", "no", "false", "f"};
		
		
		while(true) {
			String text = input.next();
			
			for (String y : yesInputs) {
				if (text.equalsIgnoreCase(y)) {
					return true;
				}
			}
			for (String n : noInputs) {
				if (text.equalsIgnoreCase(n)) {
					return false;
				}
			}
		}
	}
	
	/**
	 * Simulates a dice roll
	 * @return a random number between 1 and 6
	 */
	private int diceRoll() {
		return (int)Math.floor(Math.random()*6+1);
	}
	

}
