import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SwingTest {

    public static String[] NAMES = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};

    public SwingTest() {

        // set up the frame (basically the window?)
        JFrame frame = new JFrame("Cluedo");
        frame.setSize(960,720);
        frame.setLayout(null);

        frame.setVisible(true);

        chooseCharacters(frame);
    }

    public void chooseCharacters(JFrame parentFrame) {
        int numPlayers = getIntegerInput(parentFrame, "Welcome to Cludeo!\nHow many players do you have?",  "Welcome", 3,6);

        // set up the frame (basically the window?)
        JDialog dialog = new JDialog(parentFrame, "Choose Characters", true);
        dialog.setSize(400,400);
        dialog.setLayout(null);

        AtomicReference<Integer> playerCount = new AtomicReference<>(1);

        // text at the top
        JLabel title = new JLabel("Choose character for player " + playerCount + ":");
        title.setBounds(30,10,250,25);
        dialog.add(title);

        // text field for inputting player's name
        JLabel chooseNameLabel = new JLabel("Player's name:");
        chooseNameLabel.setBounds(30,190,250,25);
        dialog.add(chooseNameLabel);
        JTextField textField = new JTextField();
        textField.setBounds(30,220,100,25);
        dialog.add(textField);

        // a continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(30,260,100,25);
        continueButton.setEnabled(false);
        dialog.add(continueButton);

        // create the radio buttons, based on player name Strings
        int y = 10;
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String name : NAMES) {
            JRadioButton radioButton = new JRadioButton(name);
            radioButton.addActionListener(e -> continueButton.setEnabled(true));
            radioButton.setActionCommand(name);  // it will return this String
            radioButton.setBounds(30,y+=25,120,25);
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
                    System.out.printf("Player %d (%s) selected %s%n", playerCount.get(), playerName, button.getActionCommand());

                    if (playerCount.getAndSet(playerCount.get() + 1) >= numPlayers)
                        dialog.setVisible(false);
                    title.setText("Choose character for player " + playerCount + ":");
                }
            }
        });

        // NOTE: it seems to work better putting this at the end
        // otherwise some things aren't visible
        dialog.setVisible(true);
    }


    public int getIntegerInput(Component parentComponent, String question, String title, int lowerBound, int upperBound) {

        boolean firstLoop = true;
        String q = String.format("%s [%d-%d]", question, lowerBound, upperBound);

        while (true) {
            try {
                int input = Integer.parseInt(JOptionPane.showInputDialog(parentComponent, q, title, JOptionPane.QUESTION_MESSAGE));

                if (input >= lowerBound && input <= upperBound)
                    return input;

            } catch (Exception ignored) {}

            if (firstLoop){
                q += "\nPlease input a number between " + lowerBound + " and " + upperBound;
                firstLoop = false;
            }
        }
    }

    public static void main(String[] args) {
        new SwingTest();
    }
}
