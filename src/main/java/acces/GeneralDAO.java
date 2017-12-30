package acces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
*
*/
public class GeneralDAO {

    private static Connection connection;

    protected Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(getConnectionString());
            } catch (SQLException e) {
                System.out.printf("Error getting connection: %s", e.getMessage() + "\n");
                return null;
            }
        }
        return connection;
    }

    public static String getConnectionString() {
        String currentDir =  System.getProperty("user.dir");
        return String.format("jdbc:sqlite:%s%s", currentDir, "/financial.db");
    }
}
