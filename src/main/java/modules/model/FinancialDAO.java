package modules.model;

import acces.GenerelDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FinancialDAO {
    private final Connection connection = getConnection();


    public void createEntryTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS financial_entry (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	value real,\n"
                + " categorie_id integer, \n"
                + " timestamp TEXT"
                + ");";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createCategorieTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS category (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL\n"
                + ");";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertCategorie(Category categorie) {
        if (getCategorie(categorie.getName()) == null) {
            String sql = "INSERT INTO category VALUES(?,?)";
            boolean flag = false;
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, categorie.getId());
                statement.setString(2, categorie.getName());
                flag = statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean insertEntry(FinancialEntry entry) {
        String sql = "INSERT INTO financial_entry VALUES(?,?,?,?,?)";
        boolean flag = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, entry.getId());
            statement.setString(2, entry.getName());
            statement.setDouble(3, entry.getValue());
            statement.setInt(4, entry.getCategory().getId());
            statement.setString(5, entry.getDate().toString());
            flag = statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public ArrayList<FinancialEntry> getAllFinancialEntries() {
        ArrayList<FinancialEntry> financialEntries = new ArrayList<>();
        String sql = "SELECT * FROM financial_entry";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                FinancialEntryBuilder bldr = new FinancialEntryBuilder();
                financialEntries.add(
                        bldr.setId(rs.getInt(1))
                                .setName(rs.getString(2))
                                .setCategorie(getCategorie(rs.getInt(4)))
                                .setValue(rs.getDouble(3))
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
        String sql = String.format("SELECT * FROM category where id = %s", id);
        try {
            Statement stmnt = connection.createStatement();
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

    public Category getCategorie(String name) {
        String sql = String.format("SELECT * FROM category where name = '%s'", name);
        try {
            System.out.println(sql);
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            if (rs.next()) {
                return new Category(rs.getInt(1), rs.getString(2));
            } else {
                System.out.println("no name found for name : " + name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Integer incrementAndGetId(String name) {
        String sql = String.format("SELECT max(id) FROM %s", name);
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

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try {
            Statement statement = connection.createStatement();
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

    private Connection getConnection() {
        if (this.connection == null) {
            GenerelDAO dao = new GenerelDAO();
            return dao.getConnection();
        } else {
            return this.connection;
        }
    }


}
