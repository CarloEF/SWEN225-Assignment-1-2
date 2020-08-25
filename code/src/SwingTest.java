import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SwingTest {

    public static String[] NAMES = {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};

    public SwingTest() {

        // set up the frame (basically the window?)
        JFrame frame = new JFrame("Cluedo");
        frame.setSize(400,400);
        frame.setLayout(null);

        AtomicReference<Integer> playerNum = new AtomicReference<>(1);

        // text at the top
        JLabel title = new JLabel("Choose character for player " + playerNum + ":");
        title.setBounds(30,10,250,25);
        frame.add(title);

        // text field for inputting player's name
        JLabel chooseNameLabel = new JLabel("Player's name:");
        chooseNameLabel.setBounds(30,190,250,25);
        frame.add(chooseNameLabel);
        JTextField textField = new JTextField();
        textField.setBounds(30,220,100,25);
        frame.add(textField);

        // a continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(30,260,100,25);
        continueButton.setEnabled(false);
        frame.add(continueButton);

        // create the radio buttons, based on player name Strings
        int y = 10;
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String name : NAMES) {
            JRadioButton radioButton = new JRadioButton(name);
            radioButton.addActionListener(e -> continueButton.setEnabled(true));
            radioButton.setActionCommand(name);  // it will return this String
            radioButton.setBounds(30,y+=25,120,25);
            buttonGroup.add(radioButton);
            frame.add(radioButton);
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
                    String playerName = textField.getText().equals("") ? "unnamed" : textField.getText();
                    textField.setText("");
                    System.out.printf("Player %d (%s) selected %s%n", playerNum.get(), playerName, button.getActionCommand());

                    playerNum.set(playerNum.get() + 1);
                    title.setText("Choose character for player " + playerNum + ":");
                }
            }
        });

        // NOTE: it seems to work better putting this at the end
        // otherwise some things aren't visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SwingTest();
    }
}
