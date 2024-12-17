import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ProvinceMap extends MainMap {
    public String[] PROVINCES;
    public String province;
    private static final int GRID_SIZE = 64;

    public ProvinceMap() {
        super("Province Map", GRID_SIZE, "Province");
        JButton loadButton = new JButton("Load Province");
        loadProvinceData();
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
            setWilayahMap(province);
            loadMapData(province);
            resetGrid();
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
            query.append("SELECT map_data FROM province WHERE nama = '");
            query.append(province);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProvinceMap app = new ProvinceMap();
            app.setVisible(true);
        });
    }
}
