import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class CityMap extends MainMap {
    public String[] CITIES;
    public String city;
    private static final int GRID_SIZE = 20;

    public CityMap() {
        super("City Map", GRID_SIZE, "City");
        JButton loadButton = new JButton("Load City");
        loadCityData();
        loadButton.addActionListener(e -> {
            loadCityData();
            city = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a City",
                    "Choose City",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    CITIES,
                    CITIES[0]
            );
            setWilayahMap(city);
            loadMapData(city);
            loadMap();
            actionPerformed();
        });
        add(loadButton, BorderLayout.SOUTH);
    }

    protected void loadMapData(String provinceName) {
        // Load province data from database
        String url = "jdbc:mysql://127.0.0.1:3306/map";
        String username = "root";
        String password = "";
        StringBuilder query = new StringBuilder();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            query.append("SELECT map_data FROM city WHERE nama = '");
            query.append(city);
            query.append("'");
            PreparedStatement statement = connection.prepareStatement(query.toString());
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {
                String mapData = resultSet.getString("map_data");

                // Pastikan panjang mapData sesuai dengan GRID_SIZE x GRID_SIZE
                if (mapData.length() <= GRID_SIZE * GRID_SIZE) {
                    int index = 0;
                    for (int row = 0; row < GRID_SIZE; row++) {
                        for (int col = 0; col < GRID_SIZE; col++) {
                            if (index == mapData.length()){

                            } else {
                                maze[row][col] = Integer.parseInt(String.valueOf(mapData.charAt(index)));
                                index++;
                                System.out.println(maze[row][col]);
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
}

