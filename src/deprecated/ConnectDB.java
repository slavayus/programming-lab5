package deprecated;
import java.sql.*;
import java.util.Properties;

/**
 * Created by slavik on 20.02.17.
 */
public class ConnectDB {
    public java.sql.Statement connectDB() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/writers";
            Properties properties = new Properties();
            properties.setProperty("user", "postgres");
            properties.setProperty("password", "1111");
            Connection connection = DriverManager.getConnection(url, properties);
            return connection.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Не удалось подключиться к базе.");
            ex.printStackTrace();
            Thread.interrupted();
            return null;
        }
    }

    public ResultSet readDataFromDB() throws SQLException {
        Statement statement = new ConnectDB().connectDB();
        ResultSet resultSet = statement.executeQuery("select * from COMMANDS");
        return resultSet;
    }
}
