package swen225.cluedo;

import java.awt.*;
import java.awt.font.GraphicAttribute;
import java.util.*;
import java.util.List;

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
	public static final String ROOM_BOARD = 
					"iiiiiiiiihiiiihiiiiiiiii" + 
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

	// Bitwise flag for how many walls a Tile contains.
	// 1 for left, 2 for up, 4 for right, 8 for down
	// Convention is walls are on both tiles they are connected to
	public static final String WALL_BOARD = 
					"0/0/0/0/0/0/0/0/0/7/0/0/0/0/7/0/0/0/0/0/0/0/0/0/" + 
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

	// Stores number of players
//	int playerNum;
	Board board;

	// Stores 
	List<Card> cards;

	// Stores a map of every Player, Weapon and Room
	Map<String, Player> players;
	Map<String, Weapon> weapons;
	Map<String, Room> rooms;

	List<Player> playerList;
	List<Weapon> weaponList;
	List<Room> roomList;

	Player murderer = null;
	Weapon murderWeapon = null;
	Room murderRoom = null;

	// Ordered List of all Players
	List<Player> humanPlayers;

	boolean isRunning = true;

	//get all input from this scanner
	Scanner input;

	/**
	 * Constructs the game, sets up stuff
	 */
	public Game() {
		cards = new ArrayList<Card>();

		players = new LinkedHashMap<>();
		playerList = new ArrayList<Player>();
		weapons = new HashMap<String, Weapon>();
		weaponList = new ArrayList<Weapon>();
		rooms = new HashMap<String, Room>();
		roomList = new ArrayList<Room>();

		input = new Scanner(System.in);
	}

	public void draw(Graphics g) {
		g.fillRect(0, 0, 960, 720);
		g.setColor(Color.WHITE);
		g.drawString("Testing drawing to the canvas", 100, 100);
	}

//	/**
//	 * Main method, initiates the game and starts it
//	 */
//	public static void main(String[] args) {
//		Game game = new Game();
//		game.startGame();
//	}

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

		addPlayer(new Player("Miss Scarlett", "S", null));
		addPlayer(new Player("Col. Mustard", "M", null));
		addPlayer(new Player("Mrs. White", "W", null));
		addPlayer(new Player("Mr. Green", "G", null));
		addPlayer(new Player("Mrs. Peacock", "P", null));
		addPlayer(new Player("Prof. Plum", "p", null));

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
		playerList.add(player);
	}

	/**
	 * Adds a weapon to collections
	 * @param weapon
	 */
	private void addWeapon(Weapon weapon) {
		cards.add(weapon);
		weapons.put(weapon.getName(), weapon);
		weaponList.add(weapon);
	}

	/**
	 * Adds a room to collections
	 * @param room
	 */
	private void addRoom(Room room) {
		cards.add(room);
		rooms.put(room.getName(), room);
		roomList.add(room);
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
	public void startGame(Map<String, String> playerNames) {
		initCards();
		initBoard();
		addRoomExits();
		setPlayerTiles();
		setWeaponTiles();

		System.out.println("Hello, welcome to Cluedo!");

		initHumanPlayers(playerNames);
		dealCards();

		//todo: I commented out the game loop because waiting for user input stops the GUI from drawing
//		while(isRunning)
//			for (Player currentPlayer : humanPlayers)
//				startPlayerTurn(currentPlayer);
	}

	/**
	 * Puts the human player list in the correct order
	 */
	private void initHumanPlayers(Map<String, String> usernames) {

		humanPlayers = new ArrayList<>();

		for (String name : players.keySet()) {
			if (usernames.containsKey(name)) {
				Player player = players.get(name);
				player.setUsername(usernames.get(name));
				humanPlayers.add(player);
			}
		}
	}

	/**
	 * Deals cards out, including picking the murder cards
	 */
	private void dealCards() {

		Collections.shuffle(cards);

		Iterator<Player> playerIterator = humanPlayers.iterator();

		// start iterator at random position (random "dealer" so Miss Scarlet doesn't get all the cards :P)
		for (int i = 0; i < Math.random() * humanPlayers.size(); i++)
			playerIterator.next();

		for (Card card : cards) {

			if (card.getClass() == Player.class && murderer == null) {
				murderer = (Player)card;
			} else if (card.getClass() == Weapon.class && murderWeapon == null) {
				murderWeapon = (Weapon)card;
			} else if (card.getClass() == Room.class && murderRoom == null) {
				murderRoom = (Room)card;
			} else {
				if (!playerIterator.hasNext())
					playerIterator = humanPlayers.iterator();
				playerIterator.next().addCard(card);
			}
		}
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

		// Allows the player to make a suggestion
		Tile tile = player.getTile();
		if (tile instanceof RoomTile && player.canAccuse()) {
			//can do suggestion
			doSuggestion(player);
		}
		// Allows the player to make an accusation
		System.out.println("Do you want to make an accusation?");
		System.out.println("Warning: if your accusation is wrong, you will lose and be unable to accuse");
		if (getBooleanInput()) {
			doAccusation(player);
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
			} else {		// Must be a room

				//join all of line together, rooms are only 2 words max so this is sufficient
				if (inputs.length == 2) {
					textInput = String.join(" ", inputs[0], inputs[1]);
				} else {
					textInput = inputs[0];
				}
				boolean roomFound = false;

				for (Room room : rooms.values()) {
					if (textInput.equalsIgnoreCase(room.getName()) || textInput.equals(room.toString())) {

						if (validRooms.contains(room)) {
							board.movePlayer(player, room);
						} else {
							System.out.println("Can't get to that room, please try again");
							doMove(player, diceRoll);
						}
						roomFound = true;
						break;
					}
				}

				if (!roomFound) {
					System.out.println("Invalid room, please try again");
					doMove(player, diceRoll);
				}
			}
		} catch(InputMismatchException e) {
			System.out.println("Invalid input, please try again");
			doMove(player, diceRoll);
		} catch(NumberFormatException e) {
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
		// Ask which Character to suggest, print out possible Character options.
		System.out.println("Which Player would you like to suggest?");
		System.out.println(getPlayerList());
		Player murdererSugg = playerList.get(getNum(0, playerList.size()-1));

		// Ask which weapon to suggest, print out possible Weapon options.
		System.out.println("Which Weapon would you like to suggest?");
		System.out.println(getWeaponList());
		Weapon weaponSugg = weaponList.get(getNum(0, weaponList.size()-1));

		// Move objects to the room
		board.movePlayer(murdererSugg, room);
		board.moveWeapon(weaponSugg, room);

		Iterator<Player> playerIterator = humanPlayers.iterator();
		while (playerIterator.next() != player) {
		}     // start iterator at currentPlayer

		// for each player clockwise of currentPlayer, check if they can refute the suggestion
		for (int i = 0; i < humanPlayers.size() - 1; i++) {
			if (!playerIterator.hasNext())
				playerIterator = humanPlayers.iterator();

			Player currPlayer = playerIterator.next();
			List<Card> refuteCards = currPlayer.getRefutes(murdererSugg, weaponSugg, room);

			if (refuteCards.size() == 0) {
				System.out.printf("%s cannot refute the murder suggestion.\n", currPlayer.getName());
			} else if (refuteCards.size() == 1) {
				System.out.printf("%s refuted the murder suggestion with %s.\n", currPlayer.getName(), refuteCards.get(0).getName());
				break;
			} else {
				System.out.printf("%s needs to choose a card to refute.\n", currPlayer.getName());
				System.out.println("Choose a card to use:");
				for (int j=0;j<refuteCards.size();j++) {
					System.out.printf("%d: %s\n", j, refuteCards.get(j).getName());
				}

				Card inputCard = refuteCards.get(getNum(0, refuteCards.size()-1));

				System.out.printf("%s refuted the murder suggestion with %s.\n", currPlayer.getName(), inputCard.getName());
				break;
			}
		}
	}

	private void doAccusation(Player player) {

		// Ask which Player to accuse, print out possible Player options.
		System.out.println("Which Player would you like to accuse?");
		System.out.println(getPlayerList());
		Player murdererAcc = playerList.get(getNum(0, playerList.size()-1));
		// Ask which Weapon to suggest, print out possible Weapon options.
		System.out.println("Which Weapon would you like to accuse?");
		System.out.println(getWeaponList());
		Weapon weaponAcc = weaponList.get(getNum(0, weaponList.size()-1));
		// Ask which Room to suggest, print out possible Room options.
		System.out.println("Which Room would you like to accuse?");
		System.out.println(getRoomList());
		Room roomAcc = roomList.get(getNum(0, roomList.size()-1));

		if (murdererAcc == murderer && weaponAcc == murderWeapon && roomAcc == murderRoom) {
			System.out.println("Congratulations, you won!");
			input.close();
			isRunning = false;
		}
		else {
			System.out.println("Oops, that was not correct, you can no longer suggest/accuse");
			player.setCanAccuse(false);
		}

		// Code to check if 
		int playersLeft = 0;
		Player last = null;

		for (Player p : humanPlayers) {
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

	/**
	 * Gets a number between min and max
	 * @param min
	 * @param max
	 * @return
	 */
	private int getNum(int min, int max) {
		int num = 0;
		int temp;
		boolean validNum = false;

		do {
			if (input.hasNextInt()) {
				temp = input.nextInt();
				if (temp < min || temp > max) {
					System.out.printf("Number must be between %d and %d:\n", min, max);
				} else {
					num = temp;
					validNum = true;
				}
			} else {
				System.out.println("Number must be an integer:");
				input.next();
			}
		} while(!validNum);

		return num;
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

	public String getPlayerList() {
		String temp = "Players: \n";
		for (int i = 0; i < this.playerList.size(); i++) {
			String lineTemp = i+": "+this.playerList.get(i).getName()+"\n";
			temp += lineTemp;
		}
		return temp;
	}


	public String getWeaponList() {
		String temp = "Weapons: "+"\n";
		for (int i = 0; i < this.weaponList.size(); i++) {
			String lineTemp = i+": "+this.weaponList.get(i).getName()+"\n";
			temp += lineTemp;
		}
		return temp;
	}

	public String getRoomList() {
		String temp = "Rooms: "+"\n";
		for (int i = 0; i < this.roomList.size(); i++) {
			String lineTemp = i+": "+this.roomList.get(i).getName()+"\n";
			temp += lineTemp;
		}
		return temp;
	}
}
