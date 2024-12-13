import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AllMap extends JFrame {

    private List<int[][]> maps; // List to hold all generated maps

    public AllMap(List<int[][]> maps) {
        super("All Maps");
        this.maps = maps;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, maps.size())); // One row, multiple columns

        for (int[][] map : maps) {
            add(createMapPanel(map));
        }

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createMapPanel(int[][] map) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(map.length, map[0].length));
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(20, 20));
                if (map[row][col] == 1) {
                    cell.setBackground(Color.BLACK); // Wall
                } else {
                    cell.setBackground(Color.WHITE); // Path
                }
                panel.add(cell);
            }
        }
        return panel;
    }

    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            List<int[][]> maps = new ArrayList<>();
            // Add some example maps
            maps.add(new int[][]{
                    {0, 1, 0, 0},
                    {0, 1, 1, 0},
                    {0, 0, 0, 0},
                    {1, 1, 1, 0}
            });
            maps.add(new int[][]{
                    {0, 0, 1, 0},
                    {1, 0, 1, 0},
                    {0, 0, 0, 0},
                    {1, 1, 1, 0}
            });
            new AllMap(maps).setVisible(true);
        });
    }
}