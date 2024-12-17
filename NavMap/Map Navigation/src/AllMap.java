import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AllMap extends JFrame {

    public int[][] maps;
    public String[] PROVINCES;
    public String province;

    public String[] CITIES;
    public String city;
    public int GRID_SIZE;


    public AllMap() {
        super("All Maps");
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
            loadCityData();
            GRID_SIZE = 20;
            this.maps = new int[GRID_SIZE][GRID_SIZE];
            JButton loadButton = new JButton("Load CityMap");
            loadButton.addActionListener(e -> {
                city = (String) JOptionPane.showInputDialog(
                        this,
                        "Select a Province",
                        "Choose Province",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        CITIES,
                        CITIES[0]
                );
                loadMapData(city, choice);
                JPanel mapPanel = createMapPanel(this.maps);
                getContentPane().removeAll();
                add(mapPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            });
            add(loadButton, BorderLayout.SOUTH);
        } else if ("Province".equals(choice)) {
            loadProvinceData();
            GRID_SIZE = 64;
            this.maps = new int[GRID_SIZE][GRID_SIZE];

            loadProvinceData();
            JButton loadButton = new JButton("Load Province");
            loadButton.addActionListener(e -> {
                loadProvinceData();
                province = (String) JOptionPane.showInputDialog(
                        this,
                        "Select a Province",
                        "Choose Province",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        PROVINCES,
                        PROVINCES[0]
                );
                loadMapData(province, choice);
                createMapPanel(this.maps);
                JPanel mapPanel = createMapPanel(this.maps);
                getContentPane().removeAll();
                add(mapPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            });
            add(loadButton, BorderLayout.SOUTH);
        }


        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel createMapPanel(int[][] map) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(map.length, map[0].length));
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(20, 20));
                if (map[row][col] == 1) {
                    cell.setBackground(Color.BLACK);
                } else if (map[row][col] == 9){
                    cell.setBackground(Color.GREEN);
                } else {
                    cell.setBackground(Color.WHITE);
                }
                panel.add(cell);
            }
        }
        return panel;
    }

    protected void loadMapData(String Name, String Type) {
        // Load province data from database
        String url = "jdbc:mysql://127.0.0.1:3306/map";
        String username = "root";
        String password = "";
        StringBuilder query = new StringBuilder();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (Type.equals("Province")) {
                query.append("SELECT map_data FROM province WHERE nama = '");
            } else if (Type.equals("City")) {
                query.append("SELECT map_data FROM city WHERE nama = '");
            }
            query.append(Name);
            query.append("'");
            PreparedStatement statement = connection.prepareStatement(query.toString());
            ResultSet resultSet = statement.executeQuery();

            System.out.println(query);

            if (resultSet.next()) {
                String mapData = resultSet.getString("map_data");
                System.out.println(mapData);

                // Pastikan panjang mapData sesuai dengan GRID_SIZE x GRID_SIZE
                if (mapData.length() <= GRID_SIZE * GRID_SIZE) {
                    System.out.println("aku disini");
                    int index = 0;
                    for (int row = 0; row < GRID_SIZE; row++) {
                        for (int col = 0; col < GRID_SIZE; col++) {
                            if (index == mapData.length()){

                            } else {
                                this.maps[row][col] = Integer.parseInt(String.valueOf(mapData.charAt(index)));
                                index++;
                            }
                        }
                    }
                } else {
                    System.err.println("Error: Map data length is incorrect. Expected " + GRID_SIZE * GRID_SIZE + " characters.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadCityData() {
        // Load province
        String url = "jdbc:mysql://127.0.0.1:3306/map";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT nama FROM city";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                List<String> provincesList = new ArrayList<>();

                do {
                    provincesList.add(resultSet.getString("nama"));
                } while (resultSet.next());

                CITIES = new String[provincesList.size()];

                for (int i = 0; i < provincesList.size(); i++){
                    CITIES[i] = provincesList.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void loadProvinceData() {
        // Load province
        String url = "jdbc:mysql://127.0.0.1:3306/map";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT nama FROM province";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                List<String> provincesList = new ArrayList<>();

                do {
                    provincesList.add(resultSet.getString("nama"));
                } while (resultSet.next());

                PROVINCES = new String[provincesList.size()];

                for (int i = 0; i < provincesList.size(); i++){
                    PROVINCES[i] = provincesList.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}