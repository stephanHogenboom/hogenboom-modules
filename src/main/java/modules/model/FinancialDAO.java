package modules.model;

import acces.GenerelDAO;

import java.sql.Connection;

public class FinancialDAO {
    private GenerelDAO dao = new GenerelDAO();

    public void createTableIfNotExist() {
        Connection con = dao.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS financial_entry (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	value real,\n"
                + " categorie_id integer, \n"
                + " timestamp TEXT"
                + ");";
    }
}
