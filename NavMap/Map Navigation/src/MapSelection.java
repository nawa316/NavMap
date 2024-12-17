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
            JOptionPane.showMessageDialog(this, "Opening " + "OpenAllMap");
            SwingUtilities.invokeLater(() -> new AllMap().setVisible(true));
        } else if (e.getSource() == Navigate) {
            JOptionPane.showMessageDialog(this, "Opening " + "Navigate");
            SwingUtilities.invokeLater(() -> {
                String[] options = {"City", "Province"};
                String choice = (String) JOptionPane.showInputDialog(
                        null,
                        "Select the type of map to display:",
                        "Map Selector",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if ("City".equals(choice)) {
                    new CityMap().setVisible(true);
                } else if ("Province".equals(choice)) {
                    new ProvinceMap().setVisible(true);
                }
            });
        } else if (e.getSource() == ScanMap) {
            JOptionPane.showMessageDialog(this, "Opening " + "ScanMap");
        }
    }
}