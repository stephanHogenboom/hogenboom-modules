package acces;

import java.sql.*;

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

    public static String getStringSaveNullSave(String string) {
        return (string == null) ? "" : string;
    }

    protected Integer incrementAndGetId(String tableName) {
        String sql = String.format("SELECT max(id) FROM %s", tableName);
        try {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            if (rs.next()) {
                System.out.println(rs.getInt(1));
                return rs.getInt(1) + 1;
            } else return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String getConnectionString() {
        String currentDir =  System.getProperty("user.dir");
        return String.format("jdbc:sqlite:%s%s", currentDir, "/financial.db");
    }
}
