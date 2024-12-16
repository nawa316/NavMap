import javax.swing.*;
import java.awt.*;

public class Instructions extends JFrame {

    public Instructions() {
        super("Instructions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a text area to display instructions
        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setText("Instructions for Using the Maze Solver:\n\n" +
                "1. Click on 'Set Points & Solve' to start the maze solving process.\n" +
                "2. Enter the start and end coordinates in the format (row,col).\n" +
                "3. The maze will be generated randomly, with walls represented in black.\n" +
                "4. The start point will be marked in green, and the end point in red.\n" +
                "5. The algorithm will visualize the solving process:\n" +
                "   - Dijkstra's algorithm will show the optimal path in pink.\n" +
                "   - Depth-First Search (DFS) will mark the path with orange dots.\n" +
                "6. You can view the total cost of the paths found by both algorithms.\n" +
                "7. Click 'Exit' to close the application.\n\n" +
                "Enjoy solving the maze!");
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(instructionsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add a close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        add(closeButton, BorderLayout.SOUTH);
    }

    public static void showInstructions() {
        SwingUtilities.invokeLater(() -> new Instructions().setVisible(true));
    }
}