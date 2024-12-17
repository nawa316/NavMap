import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

public class MainMap extends JFrame {
    protected int[][] maze;
    protected int gridSize;
    public JButton[][] cells;
    protected String typeMap;
    protected String wilayahMap;

    public String getWilayahMap() {
        return wilayahMap;
    }

    public void setWilayahMap(String wilayahMap) {
        this.wilayahMap = wilayahMap;
    }

    protected JButton startButton;

    protected boolean isSolving = false;

    public int startRow = 0;
    public int startCol = 0;
    public int endRow = gridSize-1;
    public int endCol = gridSize-1;

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

    public MainMap(String title, int gridSize, String type) {
        super(title);
        this.typeMap = type;
        this.gridSize = gridSize;
        startButton = new JButton("Start");
        startButton.addActionListener(e -> actionPerformed());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(gridSize * gridSize + 16, gridSize * gridSize + 100);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        maze = new int[gridSize][gridSize];
        cells = new JButton[gridSize][gridSize];
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
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
                cells[row][col].setPreferredSize(new Dimension(gridSize, gridSize));
                gridPanel.add(cells[row][col]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        costLabel = new JLabel("Total Cost: Dijkstra: 0 | DFS: 0");
        JPanel costPanel = new JPanel();
        costPanel.add(costLabel);
        add(costPanel, BorderLayout.NORTH);

        timer = new Timer(50, e -> executeStep());
    }

    public void actionPerformed() {
                loadMap();
                setStartAndEndPoints();
                startAlgorithms();
    }

    public void loadMap() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cells[row][col].setBackground(maze[row][col] == 9 ? Color.GREEN : maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
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
            distances = new int[gridSize][gridSize];
            predecessors = new Point[gridSize][gridSize];
            Arrays.stream(distances).forEach(a -> Arrays.fill(a, Integer.MAX_VALUE));
            distances[startRow][startCol] = 0;
            pq = new PriorityQueue<>(Comparator.comparingInt(p -> distances[p.x][p.y]));
            pq.add(new Point(startRow, startCol));

            visited = new boolean[gridSize][gridSize];
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
        Point[][] dfsPredecessors = new Point[gridSize][gridSize];
        boolean[][] dfsVisited = new boolean[gridSize][gridSize];

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
            Point point = dfsPath.pop();
            cells[point.x][point.y].setBackground(Color.YELLOW);
        }
    }

    public void resetGrid() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cells[row][col].setBackground(maze[row][col] == 9 ? Color.GREEN : maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize && (maze[row][col] == 0 || maze[row][col] == 9);
    }

    private void executeDijkstraStep() {
        Point current = pq.poll();
        if (current == null) return;

        cells[current.x][current.y].setBackground(Color.BLUE);

        if (current.x == endRow && current.y == endCol) {
            reconstructPathDijkstra();
            dijkstraPathFound = true;
            dijkstraOptimalCost = distances[endRow][endCol];
            costLabel.setText("Total Cost: Dijkstra: " + dijkstraOptimalCost + " | DFS: " + dfsOptimalCost);
            return;
        }

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = current.x + dRow[i];
            int newCol = current.y + dCol[i];

            if (isValidMove(newRow, newCol)) {
                int newDist = distances[current.x][current.y] + 1;
                if (newDist < distances[newRow][newCol]) {
                    distances[newRow][newCol] = newDist;
                    predecessors[newRow][newCol] = current;
                    pq.add(new Point(newRow, newCol));
                }
            }
        }
    }

    private void reconstructPathDijkstra() {
        Point current = new Point(endRow, endCol);
        while (current != null) {
            cells[current.x][current.y].setBackground(Color.BLUE);
            current = predecessors[current.x][current.y];
        }
    }

    private void executeDFSStep() {
        if (dfsStack.isEmpty()) return;

        Point current = dfsStack.pop();
        if (current.x == endRow && current.y == endCol) {
            dfsPathFound = true;
            return;
        }

        cells[current.x][current.y].setText("●");

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = current.x + dRow[i];
            int newCol = current.y + dCol[i];

            if (isValidMove(newRow, newCol) && !visited[newRow][newCol]) {
                visited[newRow][newCol] = true;
                dfsStack.push(new Point(newRow, newCol));
            }
        }
    }
}
