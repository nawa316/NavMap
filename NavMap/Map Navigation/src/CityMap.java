import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;


public class CityMap extends MainMap {
    protected String[] CITIES;
    private static final int GRID_SIZE = 20;

    public CityMap() {
        super("City Map", GRID_SIZE, "City");
        JButton loadButton = new JButton("Load Province");
        loadProvinceData();
        loadButton.addActionListener(e -> {
            String province = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a Province",
                    "Choose Province",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    CITIES,
                    CITIES[0]
            );
            if (province != null) {
                loadMapData(province);
                resetGrid();
            }
        });
        add(loadButton, BorderLayout.SOUTH);
    }

    protected void loadMapData(String provinceName) {
        // Load province data from database
        String url = "jdbc:mysql://127.0.0.1:3306/map";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT map_data FROM province WHERE idprovince = '1'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String mapData = resultSet.getString("map_data");
                int index = 0;
                for (int row = 0; row < GRID_SIZE; row++) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        maze[row][col] = mapData.charAt(index++) - '0';
                    }
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
            String query = "SELECT nama FROM city";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String cityText = resultSet.getString("nama");
                System.out.println(cityText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CityMap app = new CityMap();
            app.setVisible(true);
        });
    }
}
