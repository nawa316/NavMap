import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapSelection extends JFrame implements ActionListener {

    private JButton OpenAllMap;
    private JButton Navigate;
    private JButton ScanMap;
    private JButton backButton;

    public MapSelection() {
        super("Nav Map");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        OpenAllMap = new JButton("Open All Map");
        Navigate = new JButton("Navigate");
        ScanMap = new JButton("Scan a Map");
        backButton = new JButton("Back to Main Menu");

        OpenAllMap.addActionListener(this);
        Navigate.addActionListener(this);
        ScanMap.addActionListener(this);
        backButton.addActionListener(e -> {
            this.dispose(); // Close the map selection window
            new MainMenu().setVisible(true); // Return to the main menu
        });

        add(OpenAllMap);
        add(Navigate);
        add(ScanMap);
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == OpenAllMap) {
            openMap("Map 1");
        } else if (e.getSource() == Navigate) {
            openMap("Map 2");
        } else if (e.getSource() == ScanMap) {
            openMap("Map 3");
        }
    }

    private void openMap(String mapName) {
        // Here you can implement the logic to open the selected map
        JOptionPane.showMessageDialog(this, "Opening " + mapName);
        // Example: new MazeSolver(mapName).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapSelection().setVisible(true));
    }
}