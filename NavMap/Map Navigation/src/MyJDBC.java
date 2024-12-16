import java.sql.*;

public class MyJDBC {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/map",
                    "root",
                    ""
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CITY");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("nama"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
