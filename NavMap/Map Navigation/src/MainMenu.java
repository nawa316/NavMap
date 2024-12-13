import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {

    private JButton openMapButton;
    private JButton instructionsButton;
    private JButton exitButton;

    public MainMenu() {
        super("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        openMapButton = new JButton("Open Map");
        instructionsButton = new JButton("Instructions");
        exitButton = new JButton("Exit");

        openMapButton.addActionListener(this);
        instructionsButton.addActionListener(this);
        exitButton.addActionListener(this);

        add(openMapButton);
        add(instructionsButton);
        add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openMapButton) {
            new MapSelection().setVisible(true); // Open the map selection window
            this.dispose(); // Close the main menu
        } else if (e.getSource() == instructionsButton) {
            Instructions.showInstructions(); // Show instructions
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}