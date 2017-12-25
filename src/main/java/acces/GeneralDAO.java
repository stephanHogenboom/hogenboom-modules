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
        String currentDir =  System.getProperty("user.dir");
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s%s", currentDir, "/financial.db"));
            } catch (SQLException e) {
                System.out.printf("Error getting connection: %s", e.getMessage() + "\n");
                return null;
            }
        }
        return connection;
    }
}
