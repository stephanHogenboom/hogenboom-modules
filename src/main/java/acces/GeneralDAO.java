package acces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
*
*/
public class GeneralDAO {

    private Connection connection;

    public Connection getConnection() {
        String currentDir =  System.getProperty("user.dir");
        if (this.connection == null) {
            try {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s%s", currentDir, "/financial.db"));
            } catch (SQLException e) {
                System.out.println("Error getting connection!!!!!!!!");
                return null;
            }
        }
        return connection;
    }
}
