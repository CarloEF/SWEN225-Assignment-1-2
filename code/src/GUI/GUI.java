package GUI;
import swen225.cluedo.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        frame.pack();   // make the frame take on the size of the panel
        
        setMenuBar(frame);
        chooseCharacters(frame);    // let the player choose the characters, initialise the Game

        frame.setVisible(true);
        
        frame.getContentPane().addComponentListener(new ComponentAdapter(){ //Current Window Size
			public void componentResized(ComponentEvent e) {
				Component c = (Component)e.getSource();
				CURRENT_WINDOW_WIDTH = c.getWidth();
				CURRENT_WINDOW_HEIGHT = c.getHeight();
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
    }

    class GameBoardComponent extends JComponent {

        GameBoardComponent() {
            setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            game.draw((Graphics2D) g);
        }
    }
    
    /*
     * return dimension of component
     */
    public static Dimension getComponentDimension() {
    	return component.getSize();
    	}

    
    public void redraw() {
    	frame.repaint();
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
        JMenu menu1 = new JMenu("Drop-Down Menu 1");
        JMenu menu2 = new JMenu("Drop-Down Menu 2");

        // adds menus to the bar
        menuBar.add(menu1);
        menuBar.add(menu2);

        // creates JMenuItems to add to the JMenu objects (creating buttons in a drop-down menu)
        JMenuItem item1 = new JMenuItem("Start Game");
        JMenuItem item2 = new JMenuItem("DEBUG: View Murder");
        JMenuItem item3 = new JMenuItem("Make Accusation");
        JMenuItem item4 = new JMenuItem("Make Suggestion");
        JMenuItem item5 = new JMenuItem("Redraw(Testing)");
        JMenuItem item6 = new JMenuItem("End Turn");
        menu1.add(item1);
        menu1.add(item2);
        menu2.add(item3);
        menu2.add(item4);
        menu2.add(item5);
        menu2.add(item6);
        // TODO: Add another MenuItem to end turn. Simply turns turnEnded to true.

        // sets accelerator keystrokes to JMenuItems (performs the action without the button being visible)
        // ALT + item number will activate that button
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
        item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
        item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));

        // adds an action listener to a button (starts the game when first button is pressed
        item1.addActionListener(e -> chooseCharacters(parentFrame));
        // For debugging: Returns Murder circumstances
        item2.addActionListener(e -> getMurderCircumstances(parentFrame));
        // For accusing
        item3.addActionListener(e -> getAccusationCircumstances(parentFrame));
        // For suggesting
        item4.addActionListener(e -> doGUISuggestion(parentFrame));
        // For redrawing
        item5.addActionListener(e -> redraw());
        // For ending turn
        item6.addActionListener(e -> endTurn());
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
        System.out.println("Murderer: " + murdererString);
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
                // player.setCanAccuse(false);
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

        Room sugRoom = ((RoomTile) currentPlayer.getTile()).getRoom();
        // text at the top
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
            Iterator<Player> playerIterator = game.getHumanPlayers().iterator();
            while (playerIterator.next() != currentPlayer) {
            }     // start iterator at currentPlayer

            // for each player clockwise of currentPlayer, check if they can refute the suggestion
            for (int i = 0; i < game.getHumanPlayers().size() - 1; i++) {
                if (!playerIterator.hasNext())
                    playerIterator = game.getHumanPlayers().iterator();

                Player currentRefuter = playerIterator.next();
                List<Card> refuteCards = currentRefuter.getRefutes(suggestedPlayer, suggestedWeapon, suggestedRoom);

                if (refuteCards.size() == 0) {
                    System.out.printf("%s cannot refute the murder suggestion.\n", currentRefuter.getName());
                } else {
                    refute(currentRefuter, susSuspect, susWeapon, susRoom, parentFrame);
                    refuted = true;
                    return;
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

    // TODO: Add a JRadioButton for all 3 cards. If the player selects a card they don't have, ask again.
    public void refute(Player refuter, String suggestedPlayer, String suggestedWeapon, String suggestedRoom, JFrame parentFrame) {


    }

    public void initializeTurn() {
        System.out.printf("%s's turn:\n", currentPlayer.getName()); // Should be changed to UI name
        int step1 = game.diceRoll();
        int step2 = game.diceRoll();
        int stepNum = step1 + step2;       // Should be shown in UI name
        int playerX = currentPlayer.getTile().getX() + 1;
        int playerY = game.BOARD_HEIGHT - currentPlayer.getTile().getY();

        // Gets all valid tiles and rooms the player can go to and puts them into the sets
        game.getBoard().getValidMoves(stepNum, currentPlayer, validTiles, validRooms);

        System.out.printf("You rolled a %d and a %d.\n", step1, step2);
        System.out.printf("Your possible moves have been highlighted as green tiles, or orange tiles for rooms.\n");
        // TODO: Update the board

    }
    private void endTurn() {
        Player lastPlayer = currentPlayer;
        currentPlayer = playerQueue.poll();
        playerQueue.add(lastPlayer);
        initializeTurn();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
