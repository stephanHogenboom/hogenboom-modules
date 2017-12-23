package modules.model;

import acces.GenerelDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FinancialDAO {
    private GenerelDAO dao = new GenerelDAO();

    public void createEntryTableIfNotExist() {
        Connection con = dao.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS financial_entry (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	value real,\n"
                + " categorie_id integer, \n"
                + " timestamp TEXT"
                + ");";
        try {
            Statement statement = con.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createCategorieTableIfNotExist() {
        Connection con = dao.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS categorie (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL\n"
                + ");";
        try {
            Statement statement = con.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean insertCategorie(Category categorie) {
        Connection con = dao.getConnection();
        String sql = "INSERT INTO categorie VALUES(?,?)";
        boolean flag = false;
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, categorie.getId());
            statement.setString(2, categorie.getName());
            flag = statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean insertEntry(FinancialEntry entry) {
        Connection con = dao.getConnection();
        String sql = "INSERT INTO financial_entry VALUES(?,?,?,?,?)";
        boolean flag = false;
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, entry.getId());
            statement.setString(2, entry.getName());
            statement.setInt(3, entry.getCategory().getId());
            statement.setDouble(4, entry.getValue());
            statement.setString(5, entry.getDate().toString());
            flag = statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public ArrayList<FinancialEntry> getAllFinancialEntries() {
        Connection con = dao.getConnection();
        ArrayList<FinancialEntry> financialEntries = new ArrayList<>();
        String sql = "SELECT * FROM financial_entry";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                FinancialEntryBuilder bldr = new FinancialEntryBuilder();
                financialEntries.add(
                        bldr.setId(rs.getInt(1))
                                .setName(rs.getString(2))
                                .setCategorie(getCategorie(rs.getInt(3)))
                                .setValue(rs.getDouble(4))
                                .setDate(LocalDateTime.parse(rs.getString(5)))
                                .build()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return financialEntries;
    }

    public Category getCategorie(int id) {
        Connection con = dao.getConnection();
        String sql = String.format("SELECT * FROM categorie where id = %s", id);
        try {
            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            if (rs.next()) {
                return new Category(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Integer incrementAndGetId() {
        Connection con = dao.getConnection();
        String sql = "SELECT max(id) FROM categorie";
        try {
            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) + 1;
            } else return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Category> getAllCategories() {
        Connection con = dao.getConnection();
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                categories.add(new Category(rs.getInt(1), rs.getString(2)));
            }
            return categories;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
