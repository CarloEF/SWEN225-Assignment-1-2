package GUI;

import swen225.cluedo.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GUI {

    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 720;
    public static int CURRENT_WINDOW_WIDTH = 960;
    public static int CURRENT_WINDOW_HEIGHT = 720;

    // the top/left position on screen where cards are drawn
    public static int CARDS_LEFT = 600;
    public static int CARDS_TOP = 360;

    private String gameLog = "";

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

    private Game game = new Game(this);

    public GUI() {

        // setup JComponent 
        component = new GameBoardComponent();
        component.setVisible(true);
        component.addMouseListener(new GameBoardMouseListener());

        component.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent mouseEvent) {    //redraw after mouseRelease
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

        frame.getContentPane().addComponentListener(new ComponentAdapter() { //Current Window Size
            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                CURRENT_WINDOW_WIDTH = c.getWidth();
                CURRENT_WINDOW_HEIGHT = c.getHeight();

                // TODO: This resizes the pane for the board. Breaks sometimes? Look at it. - Elias
                component.setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
            }
        });

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
            game.getBoard().drawValidTiles((Graphics2D) g);
            // TODO: reinstate this when drawCards displays properly
            // drawCards((Graphics2D) g);
        }
    }

    class GameBoardMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {
            game.getBoard().doMouse(game.getCurrentPlayer(), e);

            redraw();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    //  TODO: also get this to scale properly with the display, so it doesn't overlap with the board
    public void drawACard(Card card, int index, Graphics2D g) {

        int x = CARDS_LEFT + (index % 3) * (Card.WIDTH + Card.OUTER_PADDING);
        int y = CARDS_TOP + (index < 3 ? 0 : 1) * (Card.HEIGHT + Card.OUTER_PADDING);
        Rectangle iconArea = new Rectangle(x + Card.INNER_PADDING, y + Card.INNER_PADDING, Card.WIDTH - 2 * Card.INNER_PADDING, Card.HEIGHT - 4*Card.INNER_PADDING);

        // if there's no card to be drawn here, draw outline and return
        if (card == null) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, Card.WIDTH, Card.HEIGHT, 10, 10);
            return;
        }

        g.setColor(new Color(0xFF01579B));
        g.fillRoundRect(x, y, Card.WIDTH, Card.HEIGHT, 10, 10);

        g.setColor(Color.WHITE);
        g.fillRect(iconArea.x, iconArea.y, iconArea.width, iconArea.height);

        Image icon = card.getIcon();
        int iconXOffset = (Card.WIDTH - icon.getWidth(null)) / 2;
        int iconYOffset = Card.INNER_PADDING + (iconArea.height - icon.getHeight(null)) / 2;
        g.drawImage(icon, x + iconXOffset, y + iconYOffset,null);

        String cardName = card.getName();
        Font font = new Font("SansSerif", Font.BOLD, 13);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textXOffset = (Card.WIDTH - fontMetrics.stringWidth(cardName)) / 2;
        g.setFont(font);
        g.drawString(cardName, x + textXOffset, y + Card.HEIGHT-Card.OUTER_PADDING);
    }

    public void redraw() {
        frame.repaint();
        panel.revalidate();
        panel.repaint();
        // TODO: Add board.drawValidTiles();
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
        JMenuItem viewLog = new JMenuItem("View Game Logs");
        JMenuItem showMurder = new JMenuItem("View Murder");
        JMenuItem makeAccusation = new JMenuItem("Make Accusation");
        JMenuItem makeSuggestion = new JMenuItem("Make Suggestion");
        JMenuItem endTurn = new JMenuItem("End turn");
        JMenuItem rulesButton = new JMenuItem("Rules");
        JMenuItem readmeButton = new JMenuItem("ReadMe");
        JMenuItem redrawButton = new JMenuItem("Redraw");
        gameMenu.add(startGame);
        gameMenu.add(viewLog);
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
        viewLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        makeAccusation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        makeSuggestion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        endTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        rulesButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        readmeButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        showMurder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        redrawButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));

        // Starts the game
        startGame.addActionListener(e -> chooseCharacters(parentFrame));
        // View Log of current game
        viewLog.addActionListener(e -> viewLog(parentFrame));
        // For debugging: Returns Murder circumstances
        showMurder.addActionListener(e -> getMurderCircumstances(parentFrame));
        // For accusing
        makeAccusation.addActionListener(e -> getAccusationCircumstances(parentFrame));
        // For suggesting
        makeSuggestion.addActionListener(e -> doGUISuggestion(parentFrame));
        // For redrawing
        redrawButton.addActionListener(e -> redraw());
        // For ending turn
        endTurn.addActionListener(e -> game.endTurn());
        // For showing rules
        rulesButton.addActionListener(e -> displayRules(parentFrame));
        // For showing readme
        readmeButton.addActionListener(e -> displayReadme(parentFrame));

        // adds the finalised JMenuBar to the overall frame
        parentFrame.setJMenuBar(menuBar);

    }

    public void chooseCharacters(JFrame parentFrame) {

        Map<String, String> players = new HashMap<>();

        int numPlayers = getNumPlayers(parentFrame);
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
        for (String name : Card.PLAYERS) {
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
                    gameLog+=("Player "+playerCount.get()+" ("+playerName+") selected "+button.getActionCommand()+"\n");

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
    public int getNumPlayers(Component parentComponent) {
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

    private void viewLog(JFrame parentFrame) {
        displayTextDialog(gameLog, "Game Log", parentFrame);
    }

    private void getMurderCircumstances(JFrame parentFrame) {
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
        gameLog+=("Murderer: "+murdererString+"\n");
        gameLog+=("Weapon: "+murderWeaponString+"\n");
        gameLog+=("Room: "+murderRoomString+"\n");
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
        JComboBox<String> suspectComboBox = new JComboBox<>(Card.PLAYERS);
        suspectComboBox.setBounds(30, 70, 100, 25);
        dialog.add(suspectComboBox);

        // combo box to choose a weapon
        JLabel weaponLabel = new JLabel("Weapon:");
        weaponLabel.setBounds(30, 100, 250, 25);
        dialog.add(weaponLabel);
        JComboBox<String> weaponComboBox = new JComboBox<>(Card.WEAPONS);
        weaponComboBox.setBounds(30, 130, 100, 25);
        dialog.add(weaponComboBox);

        // combo box to choose a room
        JLabel roomLabel = new JLabel("Crime scene:");
        roomLabel.setBounds(30, 160, 250, 25);
        dialog.add(roomLabel);
        JComboBox<String> roomComboBox = new JComboBox<>(Card.ROOMS);
        roomComboBox.setBounds(30, 190, 100, 25);
        dialog.add(roomComboBox);

        // an accuse button
        JButton accuseButton = new JButton("Accuse");
        accuseButton.setBounds(30, 250, 100, 25);
        dialog.add(accuseButton);

        // add the selected player when button is pressed
        accuseButton.addActionListener(e -> {

            gameLog+=(game.getCurrentPlayer().getName()+" chose to accuse: ");
            String accSuspect = (String) suspectComboBox.getSelectedItem();
            String accWeapon = (String) weaponComboBox.getSelectedItem();
            String accRoom = (String) roomComboBox.getSelectedItem();
            gameLog+=(accSuspect+", ");
            gameLog+=(accWeapon+", ");
            gameLog+=(accRoom+"\n");

            if (game.checkAccusationIsTrue(accSuspect, accWeapon, accRoom)) {
                log("Oops, that was not correct, "+game.getCurrentPlayer().getName()+" can no longer suggest/accuse\n");
            } else {
                log("Congratulations to player "+game.getCurrentPlayer().getName()+", you won!\n");
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

        if (game.getCurrentPlayer().getTile() instanceof RoomTile) {
            Room sugRoom = ((RoomTile) game.getCurrentPlayer().getTile()).getRoom();

            JLabel title = new JLabel("Make a suggestion within the current room: " + sugRoom.getName());
            title.setBounds(30, 10, 350, 25);
            dialog.add(title);

            // combo box to choose a player
            JLabel suspectLabel = new JLabel("Suspect:");
            suspectLabel.setBounds(30, 40, 250, 25);
            dialog.add(suspectLabel);
            JComboBox<String> suspectComboBox = new JComboBox<>(Card.PLAYERS);
            suspectComboBox.setBounds(30, 70, 100, 25);
            dialog.add(suspectComboBox);

            // combo box to choose a weapon
            JLabel weaponLabel = new JLabel("Weapon:");
            weaponLabel.setBounds(30, 100, 250, 25);
            dialog.add(weaponLabel);
            JComboBox<String> weaponComboBox = new JComboBox<>(Card.WEAPONS);
            weaponComboBox.setBounds(30, 130, 100, 25);
            dialog.add(weaponComboBox);

            // a Suggest button
            JButton suggestButton = new JButton("Suggest");
            suggestButton.setBounds(30, 250, 100, 25);
            dialog.add(suggestButton);

            suggestButton.addActionListener(e -> {
                gameLog+=("Player "+game.getCurrentPlayer().getName()+" chose to suggest:\n");
                String susSuspect = (String) suspectComboBox.getSelectedItem();
                String susWeapon = (String) weaponComboBox.getSelectedItem();
                String susRoom = sugRoom.getName();

                gameLog+=(susSuspect+", ");
                gameLog+=(susWeapon+", ");
                gameLog+=(susRoom+"\n");

                if (game.canRefuteSuggestion(susSuspect, susWeapon, susRoom)) {
                    log("You may make an accusation before ending your turn.\n");
                } else {
                    log("The Suggestion was unable to be refuted by the other players.\n");
                }

                dialog.setVisible(false);
            });

            // NOTE: it seems to work better putting this at the end
            // otherwise some things aren't visible
            dialog.setVisible(true);
        } else {      // Player is not in a room
            gameLog+=("You are not in a room!\n");
        }

    }

    /**
     * Adds information to game log
     * @param string The info to add
     */
    public void log(String string) {
        gameLog += string;
    }

    public void refute(Player refuter, String suggestedPlayer, String suggestedWeapon, String suggestedRoom, boolean failed) {
        String[] refutableCards = {suggestedPlayer, suggestedWeapon, suggestedRoom};
        List<Card> refuterCards = refuter.getCards();

        if (!failed) {
            gameLog += (refuter.getName()+" is able to refute the suggestion.\n");
        }
        else {
            gameLog += (refuter.getName()+" chose a card they did not hold! Please choose again.\n");
        }

        JDialog dialog = new JDialog(frame, "Choose Card to refute", true);
        dialog.setSize(400, 400);
        dialog.setLayout(null);

        // text at the top
        JLabel title = new JLabel("Choose card to refute for "+game.getCurrentPlayer().getName()+"'s suggestion:");
        title.setBounds(30, 10, 250, 25);
        dialog.add(title);

        // a refute button
        JButton refuteButton = new JButton("Refute");
        refuteButton.setBounds(30, 260, 100, 25);
        refuteButton.setEnabled(false);
        dialog.add(refuteButton);

        int y = 10;
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String card : refutableCards) {
            JRadioButton radioButton = new JRadioButton(card);
            radioButton.addActionListener(e -> refuteButton.setEnabled(true));
            radioButton.setActionCommand(card);  // it will return this String
            radioButton.setBounds(30, y += 25, 120, 25);
            buttonGroup.add(radioButton);
            dialog.add(radioButton);
        }

        // Checks to see if Refuter owns the chosen card
        refuteButton.addActionListener(e -> {
                if (refuteButton.isSelected()) {
                        String refuteChoice = refuteButton.getActionCommand();
                        gameLog += (refuter.getName() + " chose to refute: " + refuteChoice + "\n");
                        // If Refuter owns their refute choice as a Player card.
                        if (refuterCards.contains(game.getPlayerMap().get(refuteChoice))) {
                            gameLog += (refuter.getName() + " refuted the suggestion with Player card: " + refuteChoice);
                            return;
                        }
                        // If Refuter owns their refute choice as a Weapon card.
                        else if (refuterCards.contains(game.getWeaponMap().get(refuteChoice))) {
                            gameLog += (refuter.getName() + " refuted the suggestion with Weapon card: " + refuteChoice);
                            return;
                        }
                        // If Refuter owns their refute choice as a Player card.
                        else if (refuterCards.contains(game.getRoomMap().get(refuteChoice))) {
                            gameLog += (refuter.getName() + " refuted the suggestion with Room card: " + refuteChoice);

                            return;
                        }
                        // Error: Player chose a card they do not hold.
                        else {
                            refute(refuter, suggestedPlayer, suggestedWeapon, suggestedRoom, true);
                        }
                    }
            });
        // NOTE: it seems to work better putting this at the end
        // otherwise some things aren't visible
        dialog.setVisible(true);
    }

    public void displayRules(JFrame parentFrame) {
        //TODO: Bother writing HTML to make this nicer formatted, or otherwise cleaning it up, adapt to use JEditorPane?
        String text = "Object:\nWelcome to Tudor Mansion. Your host, Mr. John Boddy, has met and untimely end - he's " +
                "the victim of foul play. To win this game, you must determine the answer to these three questions: " +
                "Who done it? Where? And with what Weapon?\n\nGameplay:\nYour turn consists of up to three actions: " +
                "Moving Your Character Pawn, Making a Suggestion, and Making an Accusation.\n\nMoving Your Character" +
                " Pawn:\nRoll the dice and move your character pawn the number of squares you rolled. You may not move" +
                " diagonally. You may change directions as many times as you like, but may not enter the same square " +
                "twice on the same turn. You cannot land in a square that's occupied by another suspect. Your valid " +
                "moves will be highlighted in green on the board, and you can move to a square by clicking on it.\n\n" +
                "Making a Suggestion:\nAs soon as you enter a Room, you are prompted to make a Suggestion. Suggestions" +
                " allow you to determine which three cards are the murder circumstances. To make a Suggestion, pick a " +
                "Suspect and a Weapon. The Room you are currently in will be the Room in your suggestion, and the " +
                "Suspect and Weapon will be moved into that Room with you.\n\nRefuting a Suggestion:\nAs soon as a " +
                "Suggestion is made, opposing players have the opportunity to refute the Suggestion, in turn order. " +
                "If the first player in turn order has any of the cards named in the Suggestion, they must show " +
                "the card to the suggesting player. If the refuting player has more than one of the cards, they get " +
                "to choose which card is shown to the suggesting player. If the refuting player has none of the " +
                "suggested cards, the opportunity to refute is passed on to the next player in turn order. As " +
                "soon as one player refutes the suggestion, it is proof that the card cannot be in the envelope, and " +
                "the presented murder is incorrect. Other players can no longer refute once the Suggestion has been " +
                "successfully Refuted. If no one can successfully refute the Suggestion, the suggesting player may " +
                "either end their turn or make an Accusation.\n\nMaking an Accusation:\nOnce you've gathered enough " +
                "information to know the murder circumstances, make an Accusation. Select any Suspect, Weapon, and " +
                "Room combination. This will be compared to the true murder circumstances, and if all three match, " +
                "you win the game. If any don't match, you are considered out of the game. You can only make one " +
                "Accusation per game of Cluedo.\n\nWinning the Game:\nWinning the game is done by successfully making" +
                " an Accusation that matches all three of the pre-selected murder circumstance cards. This can be" +
                "done through process of elimination, figuring out what cards other players have that cannot be the" +
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
     *
     * @param text        - Text to put in text box
     * @param title       - Title for dialog window
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
        // TODO: do validTiles.clear(); and validRooms.clear(), to show that the player has moved.
    }

    public static void main(String[] args) {
        new GUI();
    }
}
