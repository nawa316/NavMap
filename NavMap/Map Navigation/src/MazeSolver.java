import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MazeSolver extends JFrame implements ActionListener {

    private static final int GRID_SIZE = 20; // Ukuran grid
    private static final int CELL_SIZE = 20; // Ukuran setiap sel
    private int startRow = 0; // Baris awal untuk start point
    private int startCol = 0; // Kolom awal untuk start point
    private int endRow = 19; // Baris akhir untuk end point
    private int endCol = 19; // Kolom akhir untuk end point

    private int[][] maze;
    private JButton[][] cells;
    private JButton startButton;
    private boolean isSolving = false;

    private int[][] distances;
    private Point[][] predecessors;
    private PriorityQueue<Point> pq;
    private Timer timer;

    public MazeSolver() {
        super("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GRID_SIZE * CELL_SIZE + 16, GRID_SIZE * CELL_SIZE + 38);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        maze = new int[GRID_SIZE][GRID_SIZE];
        cells = new JButton[GRID_SIZE][GRID_SIZE];
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JButton();
                cells[row][col].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cells[row][col].addActionListener(this);
                gridPanel.add(cells[row][col]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        startButton = new JButton("Set Points & Solve");
        startButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        timer = new Timer(100, this); // Delay in milliseconds
        timer.setInitialDelay(0);

        initializeMaze(); // Inisialisasi maze saat aplikasi dijalankan
    }

    private void initializeMaze() {
        Random rand = new Random();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                maze[row][col] = rand.nextInt(3) == 0 ? 1 : 0;
                cells[row][col].setBackground(maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
        maze[startRow][startCol] = 0; // Pastikan titik awal adalah jalur
        maze[endRow][endCol] = 0; // Pastikan titik akhir adalah jalur
        cells[startRow][startCol].setBackground(Color.GREEN);
        cells[endRow][endCol].setBackground(Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton && !isSolving) {
            setStartAndEndPoints();
            isSolving = true;
            startSolving();
        } else if (e.getSource() == timer) {
            executeSolvingStep();
        }
    }

    private void setStartAndEndPoints() {
        String start = JOptionPane.showInputDialog("Enter start coordinates (row,col):");
        String end = JOptionPane.showInputDialog("Enter end coordinates (row,col):");
        if (start != null && end != null && start.contains(",") && end.contains(",")) {
            startRow = Integer.parseInt(start.split(",")[0].trim());
            startCol = Integer.parseInt(start.split(",")[1].trim());
            endRow = Integer.parseInt(end.split(",")[0].trim());
            endCol = Integer.parseInt(end.split(",")[1].trim());
        }
        maze[startRow][startCol] = 0;
        maze[endRow][endCol] = 0;
        cells[startRow][startCol].setBackground(Color.GREEN);
        cells[endRow][endCol].setBackground(Color.RED);
    }

    private void startSolving() {
        // Reset the maze visual
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i][j].setBackground(maze[i][j] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
        cells[startRow][startCol].setBackground(Color.GREEN);
        cells[endRow][endCol].setBackground(Color.RED);

        // Start solving the maze
        distances = new int[GRID_SIZE][GRID_SIZE];
        predecessors = new Point[GRID_SIZE][GRID_SIZE];
        Arrays.stream(distances).forEach(a -> Arrays.fill(a, Integer.MAX_VALUE));
        Arrays.stream(predecessors).forEach(a -> Arrays.fill(a, null));

        distances[startRow][startCol] = 0;
        pq = new PriorityQueue<>(Comparator.comparingInt(p -> distances[p.x][p.y]));
        pq.add(new Point(startRow, startCol));

        timer.start();
    }

    private void executeSolvingStep() {
        if (pq.isEmpty()) {
            timer.stop();
            reconstructPath();
            return;
        }

        Point current = pq.poll();
        int row = current.x;
        int col = current.y;

        if (row == endRow && col == endCol) {
            timer.stop();
            reconstructPath();
            return;
        }

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidMove(newRow, newCol)) {
                int newDist = distances[row][col] + 1;
                if (newDist < distances[newRow][newCol]) {
                    distances[newRow][newCol] = newDist;
                    predecessors[newRow][newCol] = new Point(row, col);
                    pq.add(new Point(newRow, newCol));
                    cells[newRow][newCol].setBackground(Color.BLUE); // Mark as visited
                }
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && col >= 0 && row < GRID_SIZE && col < GRID_SIZE && maze[row][col] == 0;
    }

    private void reconstructPath() {
        Point current = new Point(endRow, endCol);
        while (current != null) {
            cells[current.x][current.y].setBackground(Color.RED);
            current = predecessors[current.x][current.y];
        }
        isSolving = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeSolver mazeSolver = new MazeSolver();
            mazeSolver.setVisible(true);
        });
    }
}


//tolong nanti berikan 2 rute pilihan dari titik A ke titik B bila ada, jangan mengurangi kode yang saya kirim