package GUI;

import swen225.cluedo.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.awt.GridBagConstraints.BOTH;

public class GUI {

    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 720;
    public static int CURRENT_WINDOW_WIDTH = 960;
    public static int CURRENT_WINDOW_HEIGHT = 720;

    /*
     * The window
     */
    private JFrame frame;

    /*
     * Panel
     */
    private JPanel panel;

    /*
     * Text pane
     */
    private JTextPane textOutputPane;

    private JButton suggestionButton;
    private JButton accusationButton;
    private JButton endTurnButton;

    /*
     * Drawing components
     */
    private static JComponent boardComponent, cardsComponent, diceComponent;

    // the top/left position on screen where cards are drawn
    public static int CARD_WIDTH;
    public static int CARD_HEIGHT;
    public static int CARD_PADDING;

    public static int DICE_SIZE;
    public static int DICE_TOP;
    public static int DICE_LEFT;
    public static int DICE_GAP;

    // constants representing the position the board is drawn to
    public static int TILE_SIZE;
    public static final int BOARD_TOP = 0;
    public static final int BOARD_LEFT = 0;

    private String gameLog = "";

    private Game game;

    public GUI() {

        game = new Game(this);

        // setup JPanel (the canvas, except canvas is an old awt thing, this is better)
        panel = new JPanel(new GridBagLayout()); // GBL allows easy scaling

        // constraints for adding to GBL panel
        GridBagConstraints gbc = new GridBagConstraints();

        // setup boardComponent
        boardComponent = new GameBoardComponent();
        boardComponent.setVisible(true);
        boardComponent.setBorder(BorderFactory.createLineBorder(Color.black));
        boardComponent.addMouseListener(new GameBoardMouseListener());

        boardComponent.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent mouseEvent) { // redraw after mouseRelease
                redraw();
            }
        });

        // constraints set for boardComponent
        gbc.fill = BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.weightx = 0.6;
        gbc.weighty = 1;
        // add boardComponent with constraints to panel
        panel.add(boardComponent, gbc);

        // setup diceComponent
        diceComponent = new DiceComponent();
        diceComponent.setVisible(true);
        diceComponent.setBorder(BorderFactory.createLineBorder(Color.black));
        // constraints changed for diceComponent
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        // add diceComponent with constraints to panel
        panel.add(diceComponent, gbc);

        // sets up an End Turn Button
        endTurnButton = new JButton("End Turn");
        endTurnButton.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        endTurnButton.addActionListener(e -> game.endTurn());
        endTurnButton.setBackground(new Color(0xe1f5fe));
        endTurnButton.setBorder(BorderFactory.createLineBorder(Color.black));
        // hover effect
        endTurnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (endTurnButton.isEnabled())
                    endTurnButton.setBorder(BorderFactory.createLineBorder(Color.blue));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                endTurnButton.setBorder(BorderFactory.createLineBorder(Color.black));
            }
        });
        // constraints changed for endTurnButton
        gbc.gridx = 2;
        // add endTurnButton with constraints to panel
        panel.add(endTurnButton, gbc);

        // sets up a suggestionButton
        suggestionButton = new JButton("Make Suggestion");
        suggestionButton.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        suggestionButton.addActionListener(e -> doGUISuggestion(frame));
        suggestionButton.setBackground(new Color(0xe1f5fe));

        suggestionButton.setBorder(BorderFactory.createLineBorder(Color.black));
        // hover effects
        suggestionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (suggestionButton.isEnabled())
                    suggestionButton.setBorder(BorderFactory.createLineBorder(Color.blue));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                suggestionButton.setBorder(BorderFactory.createLineBorder(Color.black));
            }
        });
        // constraints changed for suggestionButton
        gbc.gridx = 1;
        gbc.gridy = 1;
        // add suggestionButton with constraints to panel
        panel.add(suggestionButton, gbc);

        // sets up an accusationButton
        accusationButton = new JButton("Make Accusation");
        accusationButton.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        accusationButton.addActionListener(e -> getAccusationCircumstances(frame));
        accusationButton.setBackground(new Color(0xe1f5fe));
        accusationButton.setBorder(BorderFactory.createLineBorder(Color.black));
        // hover effects
        accusationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (accusationButton.isEnabled())
                    accusationButton.setBorder(BorderFactory.createLineBorder(Color.blue));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                accusationButton.setBorder(BorderFactory.createLineBorder(Color.black));
            }
        });
        // constraints changed for accusationButton
        gbc.gridx = 2;
        // add accusationButton with constraints to panel
        panel.add(accusationButton, gbc);

        // setup text pane
        textOutputPane = new textOutputPane();
        textOutputPane.setVisible(true);
        textOutputPane.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(textOutputPane);
        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setViewportView(textOutputPane);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.4;

        // add textPanel with constraints to panel
        panel.add(scrollPane, gbc);

        // setup cardsComponent
        cardsComponent = new CardsComponent();
        cardsComponent.setVisible(true);
        cardsComponent.setBorder(BorderFactory.createLineBorder(Color.black));
        // constraints changed for logComponent
        gbc.gridy = 3;
        // add cardsComponent with constraints to panel
        panel.add(cardsComponent, gbc);

        // set up the window
        frame = new JFrame("Cluedo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        setMenuBar(frame);
        chooseCharacters(frame); // let the player choose the characters, initialise the Game

        frame.getContentPane().addComponentListener(new ComponentAdapter() { // Current Window Size
            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                CURRENT_WINDOW_WIDTH = c.getWidth();
                CURRENT_WINDOW_HEIGHT = c.getHeight();
            }
        });

        // these methods have to be called at the end, otherwise horrific things happen
        // - Ollie
        redraw();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // make the frame take on the size of the panel
        frame.setVisible(true);
    }

    class textOutputPane extends JTextPane {
        textOutputPane() {
            setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
            setEditable(false);
            setLayout(null);
        }
    }

    class GameBoardComponent extends JComponent {

        GameBoardComponent() {
            setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT);

            int width = GUI.getBoardComponentWidth();
            int height = GUI.getBoardComponentHeight();

            if (width < height)
                TILE_SIZE = width / 24;
            else
                TILE_SIZE = height / 25;

            // fill in behind the board so that cellar looks solid
            g.setColor(Color.GRAY);
            g.fillRect(BOARD_LEFT + 2 * TILE_SIZE, BOARD_TOP + 2 * TILE_SIZE, TILE_SIZE * (Game.BOARD_COLS - 4), TILE_SIZE * (Game.BOARD_ROWS - 4));

            for (int row = 0; row < Game.BOARD_ROWS; row++) {
                for (int col = 0; col < Game.BOARD_COLS; col++) {
                    int x = BOARD_LEFT + col * TILE_SIZE;
                    int y = BOARD_TOP + row * TILE_SIZE;

                    // draw backgrounds of cells
                    Tile tile = game.getBoard().getTile(col, row);
                    if (tile instanceof InaccessibleTile)
                        continue;

                    if (tile instanceof HallwayTile) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else if (tile instanceof RoomTile) {
                        g.setColor(Color.GRAY);
                    }

                    if (game.state == Game.State.MOVING)
                        if (game.getBoard().getValidTiles().contains(tile))
                            g.setColor(Color.GREEN);
                        else
                            for (Room room : game.getBoard().getValidRooms())
                                if (room.getTiles().contains(tile))
                                    g.setColor(Color.ORANGE);

                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                    // do outlines for hallway tiles
                    if (tile instanceof HallwayTile) {
                        g.setColor(Color.GRAY);
                        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                    }

                    // draw walls as thicker lines
                    Stroke initialStroke = g.getStroke();
                    g.setColor(Color.DARK_GRAY);
                    g.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
                    if (tile.hasDownWall())
                        g.drawLine(x, y + TILE_SIZE, x + TILE_SIZE, y + TILE_SIZE);
                    if (tile.hasUpWall())
                        g.drawLine(x, y, x + TILE_SIZE, y);
                    if (tile.hasLeftWall())
                        g.drawLine(x, y, x, y + TILE_SIZE);
                    if (tile.hasRightWall())
                        g.drawLine(x + TILE_SIZE, y, x + TILE_SIZE, y + TILE_SIZE);

                    g.setStroke(initialStroke); // set the stroke back to normal
                }
            }

            for (Player player : game.getPlayerMap().values()) {
                player.draw(g);
            }

            for (Weapon weapon : game.getWeaponMap().values()) {
                weapon.draw(g);
            }
        }
    }

    class DiceComponent extends JComponent {

        DiceComponent() {
            setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(0xe1f5fe));
            g.fillRect(0, 0, CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT);
            DICE_SIZE = (int) (this.getWidth() > this.getHeight() ? this.getHeight() * 0.8 : this.getWidth() * 0.4);
            DICE_TOP = (int) (this.getHeight() * 0.1);
            DICE_LEFT = (int) (this.getWidth() * 0.1);
            DICE_GAP = (int) (this.getWidth() * 0.8 - 2 * DICE_SIZE);
            drawDice((Graphics2D) g);
        }
    }

    class CardsComponent extends JComponent {

        CardsComponent() {
            setPreferredSize(new Dimension(CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(0xe1f5fe));
            g.fillRect(0, 0, CURRENT_WINDOW_WIDTH, CURRENT_WINDOW_HEIGHT);

            int verticalSpace = (int) (this.getHeight() / 34);     // single unit of vertical space for cards
            int horizontalSpace = (int) (this.getWidth() / 36);    // single unit of horizontal space for cards

            // based on 16:11:1 Height:Width:Padding ratio given in original cards
            CARD_HEIGHT = verticalSpace > horizontalSpace ? 16 * horizontalSpace : 16 * verticalSpace;
            CARD_WIDTH = verticalSpace > horizontalSpace ? 11 * horizontalSpace : 11 * verticalSpace;
            CARD_PADDING = Math.min(verticalSpace, horizontalSpace);

            game.drawCards((Graphics2D) g);
        }
    }

    class GameBoardMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            // Only allows movement if Game is not in GAME_OVER State.
            if (game.state != Game.State.GAME_OVER) {
                if (e.getX() < BOARD_LEFT || e.getX() > BOARD_LEFT + Game.BOARD_COLS * TILE_SIZE) return;
                if (e.getY() < BOARD_TOP || e.getY() > BOARD_TOP + Game.BOARD_ROWS * TILE_SIZE) return;

                int col = (e.getX() - BOARD_LEFT) / TILE_SIZE;
                int row = (e.getY() - BOARD_TOP) / TILE_SIZE;

                Tile tile = game.getBoard().getTile(col, row);

                if (game.getBoard().getValidTiles().contains(tile)) {
                    game.getCurrentPlayer().moveToTile(tile);
                    game.goToState(Game.State.ACCUSING);
                    return;
                }
                for (Room room : game.getBoard().getValidRooms()) {
                    if (room.getTiles().contains(tile)) {
                        game.getCurrentPlayer().moveToRoom(room);
                        game.goToState(Game.State.SUGGESTING);
                        return;
                    }
                }
            }

            redraw();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /**
     * Draws an individual player icon onto the board.
     *
     * @param player The player to draw
     * @param g      2dGraphics Object used to draw
     */
    public static void drawPlayer(Player player, Graphics2D g) {
        int tileTop = BOARD_TOP + player.getTile().getY() * TILE_SIZE;
        int tileLeft = BOARD_LEFT + player.getTile().getX() * TILE_SIZE;

        // make the player a circle slightly smaller than the tile it's in
        int innerPadding = TILE_SIZE / 10;
        int diameter = TILE_SIZE - 2 * innerPadding;
        int circleTop = tileTop + innerPadding;
        int circleLeft = tileLeft + innerPadding;

        // draw the player as a circle
        g.setColor(player.getColour());
        g.fillOval(circleLeft, circleTop, diameter, diameter);
        g.setColor(Color.BLACK);
        g.drawOval(circleLeft, circleTop, diameter, diameter);

        // draw the player's username underneath (centred horizontally)
        Font font = new Font("SansSerif", Font.PLAIN, 10);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(player.getUsername());
        int textHeight = fontMetrics.getHeight();
        int textLeft = tileLeft + (TILE_SIZE / 2) - (textWidth / 2);
        int textTop = tileTop + TILE_SIZE;
        g.setFont(font);

        g.setColor(Color.BLACK);
        g.drawString(player.getUsername(), textLeft, textTop + textHeight - 5);
    }

    /**
     * Draws an individual weapon icon onto the board.
     *
     * @param weapon The weapon to draw
     * @param g      2dGraphics Object used to draw
     */
    public static void drawWeapon(Weapon weapon, Graphics2D g) {
        int tileTop = BOARD_TOP + weapon.getTile().getY() * TILE_SIZE;
        int tileLeft = BOARD_LEFT + weapon.getTile().getX() * TILE_SIZE;
        int halfTile = TILE_SIZE / 2;

        int innerPadding = TILE_SIZE / 10;
        int diamondSize = TILE_SIZE - 2 * innerPadding;
        int halfDiamond = diamondSize / 2;
        int diamondTop = tileTop + innerPadding;
        int diamondLeft = tileLeft + innerPadding;

        int[] xPoints = new int[]{diamondLeft + halfDiamond, diamondLeft + diamondSize, diamondLeft + halfDiamond, diamondLeft};
        int[] yPoints = new int[]{diamondTop, diamondTop + halfDiamond, diamondTop + diamondSize, diamondTop + halfDiamond};

        g.setColor(Color.BLACK);
        g.fillPolygon(xPoints, yPoints, 4);

        Font font = new Font("SansSerif", Font.PLAIN, 10);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(weapon.getName());
        int textHeight = fontMetrics.getHeight();
        int textLeft = tileLeft + halfTile - (textWidth / 2);
        int textTop = tileTop + TILE_SIZE;
        g.setFont(font);

        g.setColor(Color.BLACK);
        g.drawString(weapon.getName(), textLeft, textTop + textHeight - 5);
    }

    /**
     * Draws a card onto the player UI
     *
     * @param card  The card to draw
     * @param index The index of the specified card to draw
     * @param g     2dGraphics Object used to draw
     */
    public void drawACard(Card card, int index, Graphics2D g) {

        int x = CARD_PADDING + (index % 3) * (CARD_WIDTH + CARD_PADDING);
        int y = CARD_PADDING + (index < 3 ? 0 : 1) * (CARD_HEIGHT + CARD_PADDING);
        Rectangle iconArea = new Rectangle(x + CARD_PADDING, y + CARD_PADDING,
                CARD_WIDTH - 2 * CARD_PADDING, CARD_HEIGHT - 4 * CARD_PADDING);

        // if there's no card to be drawn here, draw outline and return
        if (card == null) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);
            return;
        }

        g.setColor(new Color(0xFF01579B));
        g.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);

        g.setColor(Color.WHITE);
        g.fillRect(iconArea.x, iconArea.y, iconArea.width, iconArea.height);

        Image icon = card.getIcon();
        int iconScaledWidth = (int) (iconArea.width * 0.7);
        int iconScaledHeight = (int) (((float) icon.getHeight(null) / icon.getWidth(null)) * iconScaledWidth);
        Image iconScaled = icon.getScaledInstance(iconScaledWidth, iconScaledHeight, Image.SCALE_SMOOTH);
        int iconXOffset = (CARD_WIDTH - iconScaled.getWidth(null)) / 2;
        int iconYOffset = CARD_PADDING + (iconArea.height - iconScaled.getHeight(null)) / 2;
        g.drawImage(iconScaled, x + iconXOffset, y + iconYOffset, null);

        String cardName = card.getName();
        Font font = new Font("SansSerif", Font.BOLD, (int) (1.5 * CARD_PADDING));
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textXOffset = (CARD_WIDTH - fontMetrics.stringWidth(cardName)) / 2;
        g.setFont(font);
        g.drawString(cardName, x + textXOffset, y + CARD_HEIGHT - CARD_PADDING);
    }

    /**
     * Redraws the board and UI, depending on game state.
     */
    public void redraw() {
        frame.repaint();

        if (game.state == Game.State.GAME_OVER) {
            suggestionButton.setEnabled(false);
            suggestionButton.setBackground(Color.WHITE);

            accusationButton.setEnabled(false);
            accusationButton.setBackground(Color.WHITE);

            endTurnButton.setBackground(Color.WHITE);
            endTurnButton.setEnabled(false);
        } else {
            if (game.state == Game.State.SUGGESTING) {
                if (game.getCurrentPlayer().getTile() instanceof RoomTile) {
                    suggestionButton.setEnabled(true);
                    suggestionButton.setBackground(new Color(0xe1f5fe));
                } else {
                    suggestionButton.setEnabled(false);
                    suggestionButton.setBackground(Color.WHITE);
                }
            } else {
                suggestionButton.setEnabled(false);
                suggestionButton.setBackground(Color.WHITE);
            }
            accusationButton.setEnabled(true);
            accusationButton.setBackground(new Color(0xe1f5fe));

            endTurnButton.setBackground(new Color(0xe1f5fe));
            endTurnButton.setEnabled(true);
        }

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Example implementation of a MenuBar implementation (easily adapted to include
     * actual functions)
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

        // creates JMenuItems to add to the JMenu objects (creating buttons in a
        // drop-down menu)
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

        // sets accelerator keystrokes to JMenuItems (performs the action without the
        // button being visible)
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

    /**
     * Starts a new game, popups a JDialog and JRadioButtonGroup to choose characters.
     *
     * @param parentFrame - The frame we'll put the creator popup in
     */
    public void chooseCharacters(JFrame parentFrame) {

        game = new Game(this);

        Map<String, String> players = new HashMap<>();

        int numPlayers = getNumPlayers(parentFrame);
        if (numPlayers == -1)
            return; // User closed dialog

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
            radioButton.setActionCommand(name); // it will return this String
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
                    gameLog += ("Player " + playerCount.get() + " (" + playerName + ") selected "
                            + button.getActionCommand() + "\n");

                    if (playerCount.getAndSet(playerCount.get() + 1) >= numPlayers) {
                        game.startGame(players);
                        dialog.setVisible(false);
                    }
                    title.setText("Choose character for player " + playerCount + ":");
                }
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Starts a new game, popups a JDialog and JRadioButtonGroup to choose characters.
     *
     * @param parentComponent - The frame we'll put the creator popup in
     */
    public int getNumPlayers(Component parentComponent) {
        String title = "Welcome to Cludeo!";
        String question = "How many players do you have?";
        Object[] fixed_option = {"3", "4", "5", "6"};

        String input = (String) JOptionPane.showInputDialog(parentComponent, question, title,
                JOptionPane.INFORMATION_MESSAGE, null, fixed_option, fixed_option[0]);

        // when user close dialog
        if (input == null)
            return -1;

        return Integer.parseInt(input);
    }

    /**
     * Displays a JTextField with the game log.
     *
     * @param parentFrame - The frame we'll put the log popup in
     */
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
        log("\nDEBUG: Murder Circumstances:\n");
        log("Murderer: " + murdererString + "\n");
        log("Weapon: " + murderWeaponString + "\n");
        log("Room: " + murderRoomString + "\n");
    }

    public void getAccusationCircumstances(JFrame parentFrame) {

        // Only allows accusations if Game is not in GAME_OVER State.
        if (game.state == Game.State.GAME_OVER) {
            return;
        }

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

            gameLog += ("\n" + game.getCurrentPlayer().getName() + " chose to accuse: ");
            String accSuspect = (String) suspectComboBox.getSelectedItem();
            String accWeapon = (String) weaponComboBox.getSelectedItem();
            String accRoom = (String) roomComboBox.getSelectedItem();
            gameLog += (accSuspect + ", ");
            gameLog += (accWeapon + ", ");
            gameLog += (accRoom + "\n");

            if (!game.checkAccusationIsTrue(accSuspect, accWeapon, accRoom)) {
                log("\nOops, that was not correct, " + game.getCurrentPlayer().getName()
                        + " can no longer suggest/accuse");
                game.playerLost();
            } else {
                log("Congratulations to player " + game.getCurrentPlayer().getName() + ", you won!\n");
            }

            dialog.setVisible(false);
        });

        dialog.setVisible(true);

    }

    /**
     * Displays a popup, asking for the currentPlayer's suggestion.
     *
     * @param parentFrame - The frame we'll put the suggestion popup in
     */
    private void doGUISuggestion(JFrame parentFrame) {

        if (game.state != Game.State.SUGGESTING) {
            if (game.getCurrentPlayer().getTile() instanceof RoomTile) {
                log("\nPlayers must move before suggesting.");
            } else {
                log("\nYou must be in a room to suggest.");
            }
            return;
        }

        // Only allows suggestions if Game is not in GAME_OVER State.
        if (game.state != Game.State.GAME_OVER) {
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
                    gameLog += ("\n" + game.getCurrentPlayer().getName() + " suggests:\n");
                    String susSuspect = (String) suspectComboBox.getSelectedItem();
                    String susWeapon = (String) weaponComboBox.getSelectedItem();
                    String susRoom = sugRoom.getName();

                    gameLog += (susSuspect + ", ");
                    gameLog += (susWeapon + ", ");
                    gameLog += (susRoom + "\n\n");

                    if (!game.refuteSuggestion(susSuspect, susWeapon, susRoom)) {
                        log("No one could refute your suggestion!\n");
                    }

                    game.goToState(Game.State.ACCUSING);
                    dialog.setVisible(false);
                });

                dialog.setVisible(true);
            } else { // Player is not in a room
                log("You are not in a room!\n");
            }
        }
    }

    /**
     * Displays a popup, asking for the currentPlayer's suggestion.
     *
     * @param refuter - The current player refuting the suggestion
     * @param cards   - The list of cards to be refuted.
     */
    public void chooseRefutingCard(Player refuter, List<Card> cards) {

        if (cards.size() == 1) {
            String message = String.format("%s showed %s the %s card", refuter.getName(), game.getCurrentPlayer().getName(), cards.get(0).getName());
            log(message);
            JOptionPane.showMessageDialog(frame, message);
            return;
        }

        // if there are multiple cards to choose from...
        JDialog dialog = new JDialog(frame, "Choose Card to Refute", true);
        dialog.setSize(400, 200);
        dialog.setLayout(null);

        // text at the top
        JLabel title = new JLabel(refuter.getName() + ", please choose which card to show " + game.getCurrentPlayer().getName());
        title.setBounds(30, 10, 350, 25);
        dialog.add(title);

        // a confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(30, 110, 100, 25);
        confirmButton.setEnabled(true);
        dialog.add(confirmButton);

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            JRadioButton radioButton = new JRadioButton(card.getName());
            radioButton.setBounds(30, 35 + i * 30, 200, 25);
            radioButton.setMnemonic(KeyEvent.VK_1 + i);
            radioButton.setActionCommand(card.getName());
            radioButton.setSelected(i == 0);
            dialog.add(radioButton);
            group.add(radioButton);
        }

        // tells the user which was selected
        confirmButton.addActionListener(e -> {
            String message = String.format("%s showed %s the %s card", refuter.getName(), game.getCurrentPlayer().getName(), group.getSelection().getActionCommand());
            log(message);
            JOptionPane.showMessageDialog(frame, message);
            dialog.setVisible(false);
            dialog.dispose();
            return;
        });

        dialog.setVisible(true);
    }

    /**
     * Adds information to game log
     *
     * @param string The info to add
     */
    public void log(String string) {
        gameLog += string;
        textOutputPane.setText(gameLog);
    }

    /**
     * Displays a JTextDialog of the rules of Cluedo.
     *
     * @param parentFrame - The frame we'll put the JTextDialog popup in
     */
    public void displayRules(JFrame parentFrame) {
        String text = "Object:\nWelcome to Tudor Mansion. Your host, Mr. John Boddy, has met and untimely end - he's "
                + "the victim of foul play. To win this game, you must determine the answer to these three questions: "
                + "Who done it? Where? And with what Weapon?\n\nGameplay:\nYour turn consists of up to three actions: "
                + "Moving Your Character Pawn, Making a Suggestion, and Making an Accusation.\n\nMoving Your Character"
                + " Pawn:\nRoll the dice and move your character pawn the number of squares you rolled. You may not move"
                + " diagonally. You may change directions as many times as you like, but may not enter the same square "
                + "twice on the same turn. You cannot land in a square that's occupied by another suspect. Your valid "
                + "moves will be highlighted in green on the board, and you can move to a square by clicking on it.\n\n"
                + "Making a Suggestion:\nAs soon as you enter a Room, you are prompted to make a Suggestion. Suggestions"
                + " allow you to determine which three cards are the murder circumstances. To make a Suggestion, pick a "
                + "Suspect and a Weapon. The Room you are currently in will be the Room in your suggestion, and the "
                + "Suspect and Weapon will be moved into that Room with you.\n\nRefuting a Suggestion:\nAs soon as a "
                + "Suggestion is made, opposing players have the opportunity to refute the Suggestion, in turn order. "
                + "If the first player in turn order has any of the cards named in the Suggestion, they must show "
                + "the card to the suggesting player. If the refuting player has more than one of the cards, they get "
                + "to choose which card is shown to the suggesting player. If the refuting player has none of the "
                + "suggested cards, the opportunity to refute is passed on to the next player in turn order. As "
                + "soon as one player refutes the suggestion, it is proof that the card cannot be in the envelope, and "
                + "the presented murder is incorrect. Other players can no longer refute once the Suggestion has been "
                + "successfully Refuted. If no one can successfully refute the Suggestion, the suggesting player may "
                + "either end their turn or make an Accusation.\n\nMaking an Accusation:\nOnce you've gathered enough "
                + "information to know the murder circumstances, make an Accusation. Select any Suspect, Weapon, and "
                + "Room combination. This will be compared to the true murder circumstances, and if all three match, "
                + "you win the game. If any don't match, you are considered out of the game. You can only make one "
                + "Accusation per game of Cluedo.\n\nWinning the Game:\nWinning the game is done by successfully making"
                + " an Accusation that matches all three of the pre-selected murder circumstance cards. This can be"
                + "done through process of elimination, figuring out what cards other players have that cannot be the"
                + "murder circumstances.";

        displayTextDialog(text, "Rules", parentFrame);
    }

    /**
     * Displays ReadMe of project
     *
     * @param parentFrame The info to draw the ReadMe into.
     */
    public void displayReadme(JFrame parentFrame) {
        String text = "Readme:\n" +
                "Assignment 2 Implementation by:\n" +
                "Carlo Flores, 300439425\n" +
                "Elias Wood, 300497766\n" +
                "Francis Nguyen-Ly, 300399215\n" +
                "Ollie Franklin, 300497201\n";
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

    /**
     * Displays current player's dice rolls.
     *
     * @param g - 2dGraphics Object used to draw
     */
    public void drawDice(Graphics2D g) {
        drawDie(DICE_TOP, DICE_LEFT, game.getDice()[0], g);
        drawDie(DICE_TOP, DICE_LEFT + DICE_SIZE + DICE_GAP, game.getDice()[1], g);
    }

    /**
     * Displays a die onto the UI.
     *
     * @param top   - Starting TOP position of die draw.
     * @param left  - Starting LEFT position of die draw.
     * @param value - Value of die on roll.
     * @param g     - 2dGraphics Object used to draw
     */
    public void drawDie(int top, int left, int value, Graphics2D g) {
        int INNER_PADDING = DICE_SIZE / 4;
        int DOT_RADIUS = DICE_SIZE / 12;
        int step = (DICE_SIZE - 2 * INNER_PADDING) / 2;

        g.setColor(Color.WHITE);
        g.fillRect(left, top, DICE_SIZE, DICE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(left, top, DICE_SIZE, DICE_SIZE);

        boolean[] isDot = game.getDots(value);
        int x = left + INNER_PADDING;
        int y = top + INNER_PADDING;
        for (int i = 0; i < 9; i++) {
            if (i != 0 && i % 3 == 0) {
                y += step;
                x = left + INNER_PADDING;
            }

            if (isDot[i]) {
                g.fillOval(x - DOT_RADIUS, y - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
            }

            x += step;
        }
    }

    /**
     * Getters
     */
    public static int getBoardComponentWidth() {
        return boardComponent.getWidth();
    }

    public static int getBoardComponentHeight() {
        return boardComponent.getHeight();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
