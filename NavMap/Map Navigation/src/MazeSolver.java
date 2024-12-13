import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MazeSolver extends JFrame implements ActionListener {

    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;

    private int startRow = 0;
    private int startCol = 0;
    private int endRow = 19;
    private int endCol = 19;

    private int[][] maze;
    private JButton[][] cells;
    private JButton startButton;

    private boolean isSolving = false;

    private int[][] distances;
    private Point[][] predecessors;
    private PriorityQueue<Point> pq;
    private Timer timer;
    private Stack<Point> dfsStack;
    private boolean[][] visited;
    private boolean dfsPathFound = false;
    private boolean dijkstraPathFound = false;

    private int dijkstraOptimalCost = 0;
    private int dfsOptimalCost = 0;
    private JLabel costLabel;

    private Timer highlightTimer;
    private boolean showDijkstra = true;

    private static final String[] PROVINCES = {
            "Jakarta", "Bali", "Yogyakarta", "West Java", "East Java"
    };
    private Map<String, int[][]> provinceMaps = new HashMap<>();

    public MazeSolver() {
        super("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GRID_SIZE * CELL_SIZE + 16, GRID_SIZE * CELL_SIZE + 100);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        maze = new int[GRID_SIZE][GRID_SIZE];
        cells = new JButton[GRID_SIZE][GRID_SIZE];
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (getText().equals("●")) {
                            g.setColor(Color.ORANGE);
                            int diameter = Math.min(getWidth(), getHeight()) - 10;
                            g.fillOval((getWidth() - diameter) / 2, (getHeight() - diameter) / 2, diameter, diameter);
                        }
                    }
                };
                cells[row][col].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                gridPanel.add(cells[row][col]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        startButton = new JButton("Set Points & Solve");
        startButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        costLabel = new JLabel("Total Cost: Dijkstra: 0 | DFS: 0");
        JPanel costPanel = new JPanel();
        costPanel.add(costLabel);
        add(costPanel, BorderLayout.NORTH);

        timer = new Timer(100, e -> executeStep());

        initializeProvinceMaps();
    }

    private void initializeProvinceMaps() {
        provinceMaps.put("Jakarta", generateRandomMap());
        provinceMaps.put("Bali", generateRandomMap());
        provinceMaps.put("Yogyakarta", generateRandomMap());
        provinceMaps.put("West Java", generateRandomMap());
        provinceMaps.put("East Java", generateRandomMap());
    }

    private int[][] generateRandomMap() {
        Random rand = new Random();
        int[][] map = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                map[row][col] = rand.nextInt(3) == 0 ? 1 : 0;
            }
        }
        return map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton && !isSolving) {
            String province = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a Province",
                    "Choose a Province",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    PROVINCES,
                    PROVINCES[0]
            );
            if (province != null) {
                loadProvinceMap(province);
                setStartAndEndPoints();
                startAlgorithms();
            }
        }
    }

    private void loadProvinceMap(String province) {
        maze = provinceMaps.get(province);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setBackground(maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
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

    private void startAlgorithms() {
        distances = new int[GRID_SIZE][GRID_SIZE];
        predecessors = new Point[GRID_SIZE][GRID_SIZE];
        Arrays.stream(distances).forEach(a -> Arrays.fill(a, Integer.MAX_VALUE));
        distances[startRow][startCol] = 0;
        pq = new PriorityQueue<>(Comparator.comparingInt(p -> distances[p.x][p.y]));
        pq.add(new Point(startRow, startCol));

        visited = new boolean[GRID_SIZE][GRID_SIZE];
        dfsStack = new Stack<>();
        dfsStack.push(new Point(startRow, startCol));

        dijkstraOptimalCost = 0;
        dfsOptimalCost = 0;
        costLabel.setText("Total Cost: Dijkstra: 0 | DFS: 0");

        isSolving = true;
        timer.start();
    }

    private void executeStep() {
        if (pq.isEmpty() && dfsStack.isEmpty()) {
            timer.stop();
            isSolving = false;
            startHighlightAlternation();
            return;
        }

        if (!pq.isEmpty() && !dijkstraPathFound) {
            executeDijkstraStep();
        }

        if (!dfsStack.isEmpty() && !dfsPathFound) {
            executeDFSStep();
        }

        if (dijkstraPathFound && dfsPathFound) {
            timer.stop();
            isSolving = false;
            startHighlightAlternation();
        }
    }

    private void startHighlightAlternation() {
        highlightTimer = new Timer(1000, e -> {
            if (showDijkstra) {
                highlightDijkstraPath();
            } else {
                highlightDFSPath();
            }
            showDijkstra = !showDijkstra;
        });
        highlightTimer.start();
    }

    private void highlightDijkstraPath() {
        resetGrid();

        Point current = new Point(endRow, endCol);
        while (current != null) {
            cells[current.x][current.y].setBackground(Color.BLUE);
            current = predecessors[current.x][current.y];
        }
    }

    private void highlightDFSPath() {
        resetGrid();

        Stack<Point> dfsPath = new Stack<>();
        Point[][] dfsPredecessors = new Point[GRID_SIZE][GRID_SIZE];
        boolean[][] dfsVisited = new boolean[GRID_SIZE][GRID_SIZE];

        dfsStack.push(new Point(startRow, startCol));
        dfsVisited[startRow][startCol] = true;

        while (!dfsStack.isEmpty()) {
            Point current = dfsStack.pop();

            if (current.x == endRow && current.y == endCol) {
                Point temp = current;
                while (temp != null) {
                    dfsPath.push(temp);
                    temp = dfsPredecessors[temp.x][temp.y];
                }
                dfsPathFound = true;
                dfsOptimalCost = dfsPath.size() - 1;
                costLabel.setText("Total Cost: Dijkstra: " + dijkstraOptimalCost + " | DFS: " + dfsOptimalCost);
                break;
            }

            int[] dRow = {-1, 1, 0, 0};
            int[] dCol = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int newRow = current.x + dRow[i];
                int newCol = current.y + dCol[i];

                if (isValidMove(newRow, newCol) && !dfsVisited[newRow][newCol]) {
                    dfsVisited[newRow][newCol] = true;
                    dfsStack.push(new Point(newRow, newCol));
                    dfsPredecessors[newRow][newCol] = current;
                }
            }
        }

        while (!dfsPath.isEmpty()) {
            Point p = dfsPath.pop();
            cells[p.x][p.y].setBackground(Color.GREEN);
        }
    }

    private void resetGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setBackground(maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private void executeDijkstraStep() {
        Point current = pq.poll();
        int row = current.x;
        int col = current.y;

        if (row == endRow && col == endCol) {
            reconstructPathDijkstra();
            dijkstraPathFound = true;
            dijkstraOptimalCost = distances[endRow][endCol];
            costLabel.setText("Total Cost: Dijkstra: " + dijkstraOptimalCost + " | DFS: " + dfsOptimalCost);
            return;
        }

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidMove(newRow, newCol) && distances[newRow][newCol] > distances[row][col] + 1) {
                distances[newRow][newCol] = distances[row][col] + 1;
                predecessors[newRow][newCol] = current;
                pq.add(new Point(newRow, newCol));
                cells[newRow][newCol].setBackground(Color.CYAN);
            }
        }
    }

    private void executeDFSStep() {
        if (dfsPathFound) return; // Stop if DFS has already found the path

        Point current = dfsStack.pop();
        int row = current.x;
        int col = current.y;

        if (row == endRow && col == endCol) {

            dfsPathFound = true;
            dfsOptimalCost = dfsStack.size();
            costLabel.setText("Total Cost: Dijkstra: " + dijkstraOptimalCost + " | DFS: " + dfsOptimalCost);
            return;
        }

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidMove(newRow, newCol) && !visited[newRow][newCol]) {
                visited[newRow][newCol] = true;
                dfsStack.push(new Point(newRow, newCol));
                cells[newRow][newCol].setText("●");
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE && maze[row][col] == 0;
    }

    private void reconstructPathDijkstra() {
        Point current = new Point(endRow, endCol);
        while (current != null) {
            cells[current.x][current.y].setBackground(Color.BLUE);
            current = predecessors[current.x][current.y];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MazeSolver().setVisible(true));
    }
}
