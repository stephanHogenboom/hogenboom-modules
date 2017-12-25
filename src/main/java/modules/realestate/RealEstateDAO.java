package modules.realestate;

import acces.GeneralDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RealEstateDAO extends GeneralDAO{
    private Connection connection = getConnection();


    public void createPropertyEntreeTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS property_entry (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	address_id NOT NULL,\n"
                + " categorie_id integer, \n"
                + " date TEXT);";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}
