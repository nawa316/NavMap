
import javax.swing.*;

public class MapTest {
    public static void main(String[] args) {
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
    }
}
