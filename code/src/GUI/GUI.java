package GUI;
import swen225.cluedo.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GUI {

    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 720;
    public static int CURRENT_WINDOW_WIDTH = 960;
    public static int CURRENT_WINDOW_HEIGHT = 720;
    
    public static String[] PLAYERS = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
    public static String[] WEAPONS = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
    public static String[] ROOMS = {"Kitchen", "Ball Room", "Conservatory", "Billiard Room", "Library", "Study", "Hall", "Lounge", "Dining Room"};

    Set<Tile> validTiles = new HashSet<Tile>();
    Set<Room> validRooms = new HashSet<Room>();

    private Player currentPlayer;
    private Queue<Player> playerQueue = new ArrayDeque<Player>();

    public static Image WEAPON_IMG;
    public static Image PLAYER_IMG;
    public static Image ROOM_IMG;

    /*
     * The window
     */
    private JFrame frame;
    
    /*
     * Panel 
     */
    private JPanel panel;
    
    /*
     * Drawing components
     */
    private static JComponent component;

    private Game game = new Game();

    public GUI() {

        // load images for cards, there's a better way of doing this but I forgot what it is - Ollie
        try {
            PLAYER_IMG = ImageIO.read(new File("images/player-icon.png"));
            WEAPON_IMG = ImageIO.read(new File("images/weapon-icon.png"));
            ROOM_IMG = ImageIO.read(new File("images/room-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // setup JComponent 
        component  = new GameBoardComponent();
        component.setVisible(true);
        
        component.addMouseListener(new MouseAdapter() {	
            public void mouseReleased(MouseEvent mouseEvent) {	//redraw after mouseRelease
                redraw();
            }
        });

        // setup JPanel (the canvas, except canvas is an old awt thing, this is better)
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // the flow layout removes padding        
        panel.add(component);
        
        // set up the window
        frame = new JFrame("Cluedo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        
        setMenuBar(frame);
        chooseCharacters(frame);    // let the player choose the characters, initialise the Game
        
        frame.getContentPane().addComponentListener(new ComponentAdapter(){ //Current Window Size
			public void componentResized(ComponentEvent e) {
				Component c = (Component)e.getSource();
				CURRENT_WINDOW_WIDTH = c.getWidth();
				CURRENT_WINDOW_HEIGHT = c.getHeight();

				// TODO: This resizes the pane for the board. Breaks sometimes? Look at it. - Elias
				component.setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
			}
		});

        // Initializes Queue of players for turns
        for (Player player : game.getHumanPlayers()) {
            playerQueue.add(player);
        }
        // Sets up the first player's turn.
        // Something similar to this will be called only whenever a turn is ended.
        currentPlayer = playerQueue.poll();
        initializeTurn();

        // these methods have to be called at the end, otherwise horrific things happen - Ollie
        redraw();
        frame.pack();   // make the frame take on the size of the panel
        frame.setVisible(true);
    }

    class GameBoardComponent extends JComponent {

        GameBoardComponent() {
            setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            game.draw((Graphics2D) g);
            // TODO: reinstate this when drawCards displays properly
            // drawCards((Graphics2D) g);
        }
    }

    // TODO: should probably move this stuff to GUI class (is it not already in the GUI class?)
    //  also get this to scale properly with the display, so it doesn't overlap with the board
//    public void drawCards(Graphics2D g) {
//
//        List<Card> playersCards = currentPlayer.getCards();
//
//        for (int i=0; i<6; i++)
//            drawACard(i < playersCards.size() ? playersCards.get(i) : null, i, g);
//    }
//
//    public void drawACard(Card card, int index, Graphics2D g) {
//
//        int CARDS_LEFT = 600;
//        int CARDS_TOP = 360;
//        int CARD_WIDTH = 110;
//        int CARD_HEIGHT = 160;
//        int CARD_PADDING = 10;
//        int INNER_PADDING = 10;
//
//        int x = CARDS_LEFT + (index % 3) * (CARD_WIDTH + CARD_PADDING);
//        int y = CARDS_TOP + (index < 3 ? 0 : 1) * (CARD_HEIGHT + CARD_PADDING);
//        Rectangle iconArea = new Rectangle(x + INNER_PADDING, y + INNER_PADDING, CARD_WIDTH - 2 * INNER_PADDING, CARD_HEIGHT - 4*INNER_PADDING);
//
//        // if there's no card to be drawn here, draw outline and return
//        if (card == null) {
//            g.setColor(Color.LIGHT_GRAY);
//            g.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);
//            return;
//        }
//
//        g.setColor(new Color(0xFF01579B));
//        g.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);
//
//        g.setColor(Color.WHITE);
//        g.fillRect(iconArea.x, iconArea.y, iconArea.width, iconArea.height);
//
//        Image icon = card instanceof Weapon ? WEAPON_IMG : card instanceof Player ? PLAYER_IMG : ROOM_IMG;
//        int iconXOffset = (CARD_WIDTH - icon.getWidth(null)) / 2;
//        int iconYOffset = INNER_PADDING + (iconArea.height - icon.getHeight(null)) / 2;
//        g.drawImage(icon, x + iconXOffset, y + iconYOffset,null);
//
//        String cardName = card.getName();
//        Font font = new Font("SansSerif", Font.BOLD, 13);
//        FontMetrics fontMetrics = g.getFontMetrics(font);
//        int textXOffset = (CARD_WIDTH - fontMetrics.stringWidth(cardName)) / 2;
//        g.setFont(font);
//        g.drawString(cardName, x + textXOffset, y + CARD_HEIGHT-CARD_PADDING);
//    }
    
    /*
     * return dimension of component
     */
    public static Dimension getComponentDimension() {
    	return component.getSize();
    	}

    
    public void redraw() {
    	frame.repaint();
    	panel.revalidate();
    	panel.repaint();
    }

    /**
     * Example implementation of a MenuBar implementation (easily adapted to include actual functions)
     *
     * @param parentFrame - The frame we'll put the MenuBar in
     */
    public void setMenuBar(JFrame parentFrame) {

        // creates the JMenuBar object (the bar itself)
        JMenuBar menuBar = new JMenuBar();

        // creates drop-down menus
        JMenu gameMenu = new JMenu("Game");
        JMenu actionMenu = new JMenu("Actions");
        JMenu helpMenu = new JMenu("Help");
        JMenu debugMenu = new JMenu("Debug");

        // adds menus to the bar
        menuBar.add(gameMenu);
        menuBar.add(actionMenu);
        menuBar.add(helpMenu);
        menuBar.add(debugMenu);

        // creates JMenuItems to add to the JMenu objects (creating buttons in a drop-down menu)
        JMenuItem startGame = new JMenuItem("Start Game");
        JMenuItem showMurder = new JMenuItem("View Murder");
        JMenuItem makeAccusation = new JMenuItem("Make Accusation");
        JMenuItem makeSuggestion = new JMenuItem("Make Suggestion");
        JMenuItem endTurn = new JMenuItem("End turn");
        JMenuItem rulesButton = new JMenuItem("Rules");
        JMenuItem readmeButton = new JMenuItem("ReadMe");
        JMenuItem redrawButton = new JMenuItem("Redraw");
        gameMenu.add(startGame);
        actionMenu.add(makeAccusation);
        actionMenu.add(makeSuggestion);
        actionMenu.add(endTurn);
        helpMenu.add(rulesButton);
        helpMenu.add(readmeButton);
        debugMenu.add(showMurder);
        debugMenu.add(redrawButton);

        // sets accelerator keystrokes to JMenuItems (performs the action without the button being visible)
        // ALT + item number will activate that button
        startGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        makeAccusation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        makeSuggestion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        endTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        rulesButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        readmeButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        showMurder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        redrawButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));

        // adds an action listener to a button (starts the game when first button is pressed
        startGame.addActionListener(e -> chooseCharacters(parentFrame));
        // For debugging: Returns Murder circumstances
        showMurder.addActionListener(e -> getMurderCircumstances(parentFrame));
        // For accusing
        makeAccusation.addActionListener(e -> getAccusationCircumstances(parentFrame));
        // For suggesting
        makeSuggestion.addActionListener(e -> doGUISuggestion(parentFrame));
        // For redrawing
        redrawButton.addActionListener(e -> redraw());
        // For ending turn
        endTurn.addActionListener(e -> endTurn());
        // For showing rules
        rulesButton.addActionListener(e -> displayRules(parentFrame));
        // For showing readme
        readmeButton.addActionListener(e -> displayReadme(parentFrame));

        // adds the finalised JMenuBar to the overall frame
        parentFrame.setJMenuBar(menuBar);

    }

    public void chooseCharacters(JFrame parentFrame) {

        Map<String, String> players = new HashMap<>();

        int numPlayers = getIntegerInput(parentFrame);
        if (numPlayers == -1) return; //User closed dialog

        // set up the frame (basically the window?)
        JDialog dialog = new JDialog(parentFrame, "Choose Characters", true);
        dialog.setSize(400, 400);
        dialog.setLayout(null);

        AtomicReference<Integer> playerCount = new AtomicReference<>(1);

        // text at the top
        JLabel title = new JLabel("Choose character for player " + playerCount + ":");
        title.setBounds(30, 10, 250, 25);
        dialog.add(title);

        // text field for inputting player's name
        JLabel chooseNameLabel = new JLabel("Player's name:");
        chooseNameLabel.setBounds(30, 190, 250, 25);
        dialog.add(chooseNameLabel);
        JTextField textField = new JTextField();
        textField.setBounds(30, 220, 100, 25);
        dialog.add(textField);

        // a continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(30, 260, 100, 25);
        continueButton.setEnabled(false);
        dialog.add(continueButton);

        // create the radio buttons, based on player name Strings
        int y = 10;
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String name : PLAYERS) {
            JRadioButton radioButton = new JRadioButton(name);
            radioButton.addActionListener(e -> continueButton.setEnabled(true));
            radioButton.setActionCommand(name);  // it will return this String
            radioButton.setBounds(30, y += 25, 120, 25);
            buttonGroup.add(radioButton);
            dialog.add(radioButton);
        }

        // add the selected player when button is pressed
        continueButton.addActionListener(e -> {
            Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
            while (radioButtons.hasMoreElements()) {
                AbstractButton button = radioButtons.nextElement();
                if (button.isSelected()) {
                    buttonGroup.clearSelection();
                    button.setEnabled(false);
                    continueButton.setEnabled(false);
                    String playerName = textField.getText().equals("") ? "Player " + playerCount : textField.getText();
                    textField.setText("");

                    players.put(button.getActionCommand(), playerName);
                    System.out.printf("Player %d (%s) selected %s%n", playerCount.get(), playerName, button.getActionCommand());

                    if (playerCount.getAndSet(playerCount.get() + 1) >= numPlayers) {
                        game.startGame(players);
                        dialog.setVisible(false);
                    }
                    title.setText("Choose character for player " + playerCount + ":");
                }
            }
        });

        // NOTE: it seems to work better putting this at the end
        // otherwise some things aren't visible
        dialog.setVisible(true);
    }

    /*
     * Construct a dialog to ask for the number of players.
     */
    public int getIntegerInput(Component parentComponent) {
        String title = "Welcome to Cludeo!";
        String question = "How many players do you have?";
        Object[] fixed_option = {"3", "4", "5", "6"};

        String input = (String) JOptionPane.showInputDialog(parentComponent, question, title,
                JOptionPane.INFORMATION_MESSAGE, null, fixed_option, fixed_option[0]);

        //when user close dialog
        if (input == null)
            return -1;

        return Integer.parseInt(input);
    }

    private void getMurderCircumstances(JFrame parentframe) {
        String murdererString = "EMPTY";
        for (String key : game.getPlayerMap().keySet()) {
            if (game.getPlayerMap().get(key).equals(game.getMurderer())) {
                murdererString = key;
            }
        }
        String murderWeaponString = "EMPTY";
        for (String key : game.getWeaponMap().keySet()) {
            if (game.getWeaponMap().get(key).equals(game.getMurderWeapon())) {
                murderWeaponString = key;
            }
        }
        String murderRoomString = "EMPTY";
        for (String key : game.getRoomMap().keySet()) {
            if (game.getRoomMap().get(key).equals(game.getMurderRoom())) {
                murderRoomString = key;
            }
        }
        System.out.println("\nMurderer: " + murdererString);
        System.out.println("Weapon: " + murderWeaponString);
        System.out.println("Room: " + murderRoomString);
    }

    public void getAccusationCircumstances(JFrame parentFrame) {

        JDialog dialog = new JDialog(parentFrame, "Make an Accusation", true);
        dialog.setSize(400, 400);
        dialog.setLayout(null);

        // text at the top
        JLabel title = new JLabel("Make an accusation - select the murder circumstances");
        title.setBounds(30, 10, 350, 25);
        dialog.add(title);

        // combo box to choose a player
        JLabel suspectLabel = new JLabel("Suspect:");
        suspectLabel.setBounds(30, 40, 250, 25);
        dialog.add(suspectLabel);
        JComboBox<String> suspectComboBox = new JComboBox<>(PLAYERS);
        suspectComboBox.setBounds(30, 70, 100, 25);
        dialog.add(suspectComboBox);

        // combo box to choose a weapon
        JLabel weaponLabel = new JLabel("Weapon:");
        weaponLabel.setBounds(30, 100, 250, 25);
        dialog.add(weaponLabel);
        JComboBox<String> weaponComboBox = new JComboBox<>(WEAPONS);
        weaponComboBox.setBounds(30, 130, 100, 25);
        dialog.add(weaponComboBox);

        // combo box to choose a room
        JLabel roomLabel = new JLabel("Crime scene:");
        roomLabel.setBounds(30, 160, 250, 25);
        dialog.add(roomLabel);
        JComboBox<String> roomComboBox = new JComboBox<>(ROOMS);
        roomComboBox.setBounds(30, 190, 100, 25);
        dialog.add(roomComboBox);

        // an accuse button
        JButton accuseButton = new JButton("Accuse");
        accuseButton.setBounds(30, 250, 100, 25);
        dialog.add(accuseButton);

        // add the selected player when button is pressed
        accuseButton.addActionListener(e -> {

            System.out.println("You chose:");
            String accSuspect = (String) suspectComboBox.getSelectedItem();
            String accWeapon = (String) weaponComboBox.getSelectedItem();
            String accRoom = (String) roomComboBox.getSelectedItem();
            System.out.println(accSuspect);
            System.out.println(accWeapon);
            System.out.println(accRoom);

            if (game.getPlayerMap().get(accSuspect) == game.getMurderer() &&
                    game.getWeaponMap().get(accWeapon) == game.getMurderWeapon() &&
                    game.getRoomMap().get(accRoom) == game.getMurderRoom()) {
                System.out.printf("Congratulations to player %s, you won!\n", currentPlayer.toString());
                game.setEnd();
                return;
            } else {
                System.out.println("Oops, that was not correct, you can no longer suggest/accuse");
                // TODO: Remove player from turn list, without removing them from Queue
                currentPlayer.setCanAccuse(false);
                endTurn();
            }

            dialog.setVisible(false);
        });

        // NOTE: it seems to work better putting this at the end
        // otherwise some things aren't visible
        dialog.setVisible(true);
    }

    private void doGUISuggestion(JFrame parentFrame) {

        JDialog dialog = new JDialog(parentFrame, "Make a Suggestion", true);
        dialog.setSize(400, 400);
        dialog.setLayout(null);

        if (currentPlayer.getTile() instanceof RoomTile) {
            Room sugRoom = ((RoomTile) currentPlayer.getTile()).getRoom();


            JLabel title = new JLabel("Make a suggestion within the current room: " + sugRoom.getName());
            title.setBounds(30, 10, 350, 25);
            dialog.add(title);

            // combo box to choose a player
            JLabel suspectLabel = new JLabel("Suspect:");
            suspectLabel.setBounds(30, 40, 250, 25);
            dialog.add(suspectLabel);
            JComboBox<String> suspectComboBox = new JComboBox<>(PLAYERS);
            suspectComboBox.setBounds(30, 70, 100, 25);
            dialog.add(suspectComboBox);

            // combo box to choose a weapon
            JLabel weaponLabel = new JLabel("Weapon:");
            weaponLabel.setBounds(30, 100, 250, 25);
            dialog.add(weaponLabel);
            JComboBox<String> weaponComboBox = new JComboBox<>(WEAPONS);
            weaponComboBox.setBounds(30, 130, 100, 25);
            dialog.add(weaponComboBox);

            // a Suggest button
            JButton suggestButton = new JButton("Suggest");
            suggestButton.setBounds(30, 250, 100, 25);
            dialog.add(suggestButton);

            suggestButton.addActionListener(e -> {
                System.out.println("You chose:");
                String susSuspect = (String) suspectComboBox.getSelectedItem();
                String susWeapon = (String) weaponComboBox.getSelectedItem();
                String susRoom = sugRoom.getName();

                System.out.println(susSuspect);
                System.out.println(susWeapon);
                System.out.println(susRoom);

                Player suggestedPlayer = game.getPlayerMap().get(susSuspect);
                Weapon suggestedWeapon = game.getWeaponMap().get(susWeapon);
                Room suggestedRoom = game.getRoomMap().get(susRoom);

                game.getBoard().movePlayer(suggestedPlayer, sugRoom);
                game.getBoard().moveWeapon(suggestedWeapon, sugRoom);
                // TODO: Update the board

                boolean refuted = false;
                Queue<Player> refuterQueue = new ArrayDeque<>();
                Queue<Player> tempQueue = new ArrayDeque<>(playerQueue);

                // For loop to only include players that are not the currentPlayer
                for (int i = 0; i < game.getHumanPlayers().size()-1; i++) {
                    refuterQueue.add(tempQueue.poll());
                }

                for (int i = 0; i < refuterQueue.size(); i++) {
                    Player currentRefuter = refuterQueue.poll();
                    List<Card> refuteCards = currentRefuter.getRefutes(suggestedPlayer, suggestedWeapon, suggestedRoom);

                    if (refuteCards.size() == 0) {
                        System.out.printf("%s cannot refute the murder suggestion.\n", currentRefuter.getName());
                    } else {
                        refute(currentRefuter, susSuspect, susWeapon, susRoom, parentFrame);
                        refuted = true;
                        break;
                    }

                }

                if (!refuted) {
                    System.out.println("The Suggestion was unable to be refuted by the other players.");
                    System.out.println("Would you like to make an accusation?");
                    // TODO: Instead of ending the turn, allow the player to make an accusation.

                }
                dialog.setVisible(false);
            });

            // NOTE: it seems to work better putting this at the end
            // otherwise some things aren't visible
            dialog.setVisible(true);
        }
        else {      // Player is not in a room
            System.out.println("You are not in a room!");
        }

    }

    // TODO: Add a JRadioButton for all 3 cards. If the player selects a card they don't have, ask again.
    public void refute(Player refuter, String suggestedPlayer, String suggestedWeapon, String suggestedRoom, JFrame parentFrame) {

    }

    public void initializeTurn() {
        System.out.printf("\n%s's turn:\n", currentPlayer.getName()); // Should be changed to UI name
        int step1 = game.diceRoll();
        int step2 = game.diceRoll();
        int stepNum = step1 + step2;       // Should be shown in UI dice
        int playerX = currentPlayer.getTile().getX() + 1;
        int playerY = game.BOARD_HEIGHT - currentPlayer.getTile().getY();

        // Gets all valid tiles and rooms the player can go to and puts them into the sets
        game.getBoard().getValidMoves(stepNum, currentPlayer, validTiles, validRooms);

        System.out.printf("You rolled a %d and a %d.\n", step1, step2);
        System.out.printf("Your possible moves have been highlighted as green tiles, or orange tiles for rooms.\n");
        // TODO: Update the board

    }
    private void endTurn() {
        System.out.println(currentPlayer.getName() + " has ended their turn.");
        Player lastPlayer = currentPlayer;
        currentPlayer = playerQueue.poll();
        playerQueue.add(lastPlayer);
        initializeTurn();
    }

    public void displayRules(JFrame parentFrame) {
        //TODO: Bother writing HTML to make this nicer formatted, or otherwise cleaning it up, adapt to use JEditorPane?
        String text = "Object:\nWelcome to Tudor Mansion. Your host, Mr. John Boddy, has met and untimely end - he's " +
                "the victim of foul play. To win this game, you must determine the answer to these three questions: "  +
                "Who done it? Where? And with what Weapon?\n\nGameplay:\nYour turn consists of up to three actions: "  +
                "Moving Your Character Pawn, Making a Suggestion, and Making an Accusation.\n\nMoving Your Character"  +
                " Pawn:\nRoll the dice and move your character pawn the number of squares you rolled. You may not move"+
                " diagonally. You may change directions as many times as you like, but may not enter the same square " +
                "twice on the same turn. You cannot land in a square that's occupied by another suspect. Your valid "  +
                "moves will be highlighted in green on the board, and you can move to a square by clicking on it.\n\n" +
                "Making a Suggestion:\nAs soon as you enter a Room, you are prompted to make a Suggestion. Suggestions"+
                " allow you to determine which three cards are the murder circumstances. To make a Suggestion, pick a "+
                "Suspect and a Weapon. The Room you are currently in will be the Room in your suggestion, and the "    +
                "Suspect and Weapon will be moved into that Room with you.\n\nRefuting a Suggestion:\nAs soon as a "   +
                "Suggestion is made, opposing players have the opportunity to refute the Suggestion, in turn order. "  +
                "If the first player in turn order has any of the cards named in the Suggestion, they must show "      +
                "the card to the suggesting player. If the refuting player has more than one of the cards, they get "  +
                "to choose which card is shown to the suggesting player. If the refuting player has none of the "      +
                "suggested cards, the opportunity to refute is passed on to the next player in turn order. As "        +
                "soon as one player refutes the suggestion, it is proof that the card cannot be in the envelope, and " +
                "the presented murder is incorrect. Other players can no longer refute once the Suggestion has been "  +
                "successfully Refuted. If no one can successfully refute the Suggestion, the suggesting player may "   +
                "either end their turn or make an Accusation.\n\nMaking an Accusation:\nOnce you've gathered enough "  +
                "information to know the murder circumstances, make an Accusation. Select any Suspect, Weapon, and "   +
                "Room combination. This will be compared to the true murder circumstances, and if all three match, "   +
                "you win the game. If any don't match, you are considered out of the game. You can only make one "     +
                "Accusation per game of Cluedo.\n\nWinning the Game:\nWinning the game is done by successfully making" +
                " an Accusation that matches all three of the pre-selected murder circumstance cards. This can be"     +
                "done through process of elimination, figuring out what cards other players have that cannot be the"   +
                "murder circumstances.";

        displayTextDialog(text, "Rules", parentFrame);
    }

    public void displayReadme(JFrame parentFrame) {
        //TODO: Add text to readme
        String text = "Readme:";

        displayTextDialog(text, "Rules", parentFrame);
    }

    /**
     * A generic method to make a dialog box filled with scrollable text.
     * @param text - Text to put in text box
     * @param title - Title for dialog window
     * @param parentFrame - Main frame of the program
     */
    public void displayTextDialog(String text, String title, JFrame parentFrame) {
        // Sets up dialog box
        JDialog rules = new JDialog(parentFrame, title, true);
        rules.setSize(400, 400);

        // Formatting for Text Box
        JTextArea textBox = new JTextArea(text);
        textBox.setLineWrap(true);
        textBox.setWrapStyleWord(true);
        textBox.setEditable(false);

        // Scroll Bar Implementation & Formatting
        JScrollPane scroll = new JScrollPane(textBox);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add scrolling pane to dialog box & display box
        rules.add(scroll);
        rules.setVisible(true);
    }

    public void doMouse() {

    }

    public static void main(String[] args) {
        new GUI();
    }
}
