package swen225.cluedo;

import java.awt.*;
import java.awt.font.GraphicAttribute;
import java.util.*;
import java.util.List;

import GUI.GUI;

// Hello, World!

/**
 * Game class that handles all input/output and the game logic
 */
public class Game {

    /*************
     * CONSTANTS *
     *************/
    public static final int BOARD_WIDTH = 24;
    public static final int BOARD_HEIGHT = 25;

    public enum State {
        ROLLING_DICE(0),
        MOVING(1),
        SUGGESTING(2),
        ACCUSING(3);

        private int value;

        State(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    };

    public State state = State.ROLLING_DICE;

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
    Board board;

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

    // even more player data structures
    private Player currentPlayer;
    private Queue<Player> playerQueue = new ArrayDeque<Player>();
    private ArrayList<Player> validPlayers = new ArrayList<Player>();

    // the initial values on the dice
    int die1 = diceRoll();
    int die2 = diceRoll();

    boolean isRunning = true;

    //get all input from this scanner
    Scanner input;

    GUI GUI;

    /**
     * Constructs the game, sets up stuff
     */
    public Game(GUI GUI) {
        cards = new ArrayList<Card>();
        board = new Board(this, BOARD_WIDTH, BOARD_HEIGHT);

        players = new LinkedHashMap<>();
        playerList = new ArrayList<Player>();
        weapons = new HashMap<String, Weapon>();
        weaponList = new ArrayList<Weapon>();
        rooms = new HashMap<String, Room>();
        roomList = new ArrayList<Room>();

        input = new Scanner(System.in);

        this.GUI = GUI;
    }

    public void draw(Graphics2D g) {
    	
    	int width = GUI.CURRENT_WINDOW_WIDTH;
    	int height = GUI.CURRENT_WINDOW_HEIGHT;
    	
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        board.draw(g);

        for (Player player : players.values()) {
            player.draw(g);
        }

        GUI.drawDice(g);
        drawCards(g);
    }

    /**
     * Initiates the cards
     */
    private void initCards() {

        addPlayer(new Player("Miss Scarlett", new Color(0xFFB71C1C), null));
        addPlayer(new Player("Col. Mustard", new Color(0xFFFFD600), null));
        addPlayer(new Player("Mrs. White", Color.WHITE, null));
        addPlayer(new Player("Mr. Green", new Color(0xFF33691E),null));
        addPlayer(new Player("Mrs. Peacock", new Color(0xFF0D47A1), null));
        addPlayer(new Player("Prof. Plum", new Color(0xFF311B92), null));

        addWeapon(new Weapon("Candlestick"));
        addWeapon(new Weapon("Dagger"));
        addWeapon(new Weapon("Lead Pipe"));
        addWeapon(new Weapon("Revolver"));
        addWeapon(new Weapon("Rope"));
        addWeapon(new Weapon("Spanner"));

        addRoom(new Room("Kitchen"));
        addRoom(new Room("Ball Room"));
        addRoom(new Room("Conservatory"));
        addRoom(new Room("Billiard Room"));
        addRoom(new Room("Library"));
        addRoom(new Room("Study"));
        addRoom(new Room("Hall"));
        addRoom(new Room("Lounge"));
        addRoom(new Room("Dining Room"));
    }

    /**
     * Adds a player to collections
     *
     * @param player
     */
    private void addPlayer(Player player) {
        cards.add(player);
        players.put(player.getName(), player);
        playerList.add(player);
    }

    /**
     * Adds a weapon to collections
     *
     * @param weapon
     */
    private void addWeapon(Weapon weapon) {
        cards.add(weapon);
        weapons.put(weapon.getName(), weapon);
        weaponList.add(weapon);
    }

    /**
     * Adds a room to collections
     *
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
            int random = (int) Math.floor(Math.random() * roomsLeft.size());

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
        board.setBoard(ROOM_BOARD, WALL_BOARD, rooms);
        addRoomExits();
        setPlayerTiles();
        setWeaponTiles();

        System.out.println("Hello, welcome to Cluedo!");

        initHumanPlayers(playerNames);
        dealCards();

        // Initializes Queue of players for turns
        for (Player player : humanPlayers) {
            playerQueue.add(player);
            validPlayers.add(player);
        }

        // Sets up the first player's turn.
        // Something similar to this will be called only whenever a turn is ended.
        currentPlayer = playerQueue.poll();
        initializeTurn();

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
                murderer = (Player) card;
            } else if (card.getClass() == Weapon.class && murderWeapon == null) {
                murderWeapon = (Weapon) card;
            } else if (card.getClass() == Room.class && murderRoom == null) {
                murderRoom = (Room) card;
            } else {
                if (!playerIterator.hasNext())
                    playerIterator = humanPlayers.iterator();
                playerIterator.next().addCard(card);
            }
        }
    }

    public void drawCards(Graphics2D g) {

        List<Card> playersCards = currentPlayer.getCards();

        for (int i=0; i<6; i++)
            GUI.drawACard(i < playersCards.size() ? playersCards.get(i) : null, i, g);
    }

    public void goToState(State newState) {
        state = newState;
        if (newState == State.ROLLING_DICE) {

        } else if (newState == State.MOVING) {

        } else if (newState == State.SUGGESTING) {
            board.clearValidRoomsAndTiles();
            GUI.redraw();
        } else if (newState == State.ACCUSING) {

        }
    }

    public boolean checkAccusationIsTrue(String accSuspect, String accWeapon, String accRoom) {
        if (players.get(accSuspect) == murderer &&
                weapons.get(accWeapon) == murderWeapon &&
                rooms.get(accRoom) == murderRoom) {
            isRunning = false;
            return true;
        }

        validPlayers.remove(currentPlayer);
        currentPlayer.setCanAccuse(false);
        endTurn();
        return false;
    }

    public boolean canRefuteSuggestion(String sugSuspect, String sugWeapon, String sugRoom) {
        Player suggestedPlayer = players.get(sugSuspect);
        Weapon suggestedWeapon = weapons.get(sugWeapon);
        Room suggestedRoom = rooms.get(sugRoom);

        board.movePlayer(suggestedPlayer, suggestedRoom);
        board.moveWeapon(suggestedWeapon, suggestedRoom);
        GUI.redraw();

        Queue<Player> refuterQueue = new ArrayDeque<>();
        Queue<Player> tempQueue = new ArrayDeque<>(playerQueue);

        // For loop to only include players that are not the currentPlayer
        for (int i = 0; i < humanPlayers.size() - 1; i++) {
            refuterQueue.add(tempQueue.poll());
        }

        for (int i = 0; i < refuterQueue.size(); i++) {
            Player currentRefuter = refuterQueue.poll();
            List<Card> refuteCards = currentRefuter.getRefutes(suggestedPlayer, suggestedWeapon, suggestedRoom);

            if (refuteCards.size() == 0) {
                GUI.log("\n"+currentRefuter.getName()+" cannot refute the murder suggestion.\n");
            } else {
                GUI.refute(currentRefuter, sugSuspect, sugWeapon, sugRoom, false);
                return true;
            }
        }

        return false;
    }

    public void initializeTurn() {
        goToState(State.ROLLING_DICE);
        GUI.log(currentPlayer.getName()+"'s turn: ");
        die1 = diceRoll();
        die2 = diceRoll();
        int stepNum = die1 + die2;       // Should be shown in UI dice

        // Gets all valid tiles and rooms the player can go to and puts them into the sets
        getBoard().getValidMoves(stepNum, currentPlayer);

        GUI.log("They rolled a "+die1+" and a "+die2+".\n");
        // Possibly remove:
        // gameLog+=("Your possible moves have been highlighted as green tiles, or orange tiles for rooms.\n");
        GUI.redraw();

        goToState(State.MOVING);
    }

    public void endTurn() {

        // TODO: is this what we agreed on? or do you not have to make a suggestion?
        if (state.value() <= State.SUGGESTING.value()) {
            GUI.log("Cannot end turn without making a suggestion");
            return;
        }

        // If endTurn is called, and 1 player is left.
        if (validPlayers.size() == 1) {
            GUI.log(currentPlayer.toString()+" has won by default, as they are the only player left!\n");
            getBoard().clearValidRoomsAndTiles();
            GUI.redraw();
            isRunning = false;
            return;

        }
        // This if-statement is called most of the time.
        if (currentPlayer.canAccuse()) {
            GUI.log(currentPlayer.getName()+" has ended their turn.\n");
        } else {    // Only called right after when it rolls back around to a player who has lost.
            GUI.log("\n"+currentPlayer.getName()+" cannot play any more, as they have made a failed accusation.\n");
        }

        Player lastPlayer = currentPlayer;
        currentPlayer = playerQueue.poll();
        playerQueue.add(lastPlayer);
        getBoard().clearValidRoomsAndTiles();
        if (currentPlayer.canAccuse()) {
            initializeTurn();
        } else {
            endTurn();
        }
    }

    /**
     * Simulates a dice roll
     *
     * @return a random number between 1 and 6
     */
    public int diceRoll() {
        return (int) Math.floor(Math.random() * 6 + 1);
    }

    public Board getBoard() {
        return board;
    }

    public List<Card> getCards() {
        return cards;
    }
    public Map<String, Player> getPlayerMap() {
        return this.players;
    }

    public Map<String, Weapon> getWeaponMap() {
        return this.weapons;
    }

    public Map<String, Room> getRoomMap() {
        return this.rooms;
    }

    public Player getMurderer() {
        return murderer;
    }
    public Weapon getMurderWeapon() {
        return murderWeapon;
    }
    public Room getMurderRoom() {
        return murderRoom;
    }

    public Player getCurrentPlayer() {return currentPlayer;}

    public int[] getDice() {
        return new int[] {die1, die2};
    }

}
