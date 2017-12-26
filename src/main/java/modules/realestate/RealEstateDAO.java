package modules.realestate;

import acces.GeneralDAO;
import elements.AlertBox;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PriceHistoryEntry;
import modules.realestate.model.PropertyEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RealEstateDAO extends GeneralDAO{
    private Connection connection = getConnection();


    public void createPropertyEntryTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS property_entry (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	address_id NOT NULL,\n"
                + " date TEXT, \n"
                + " is_sold INTEGER, \n"
                + " sell_price INTEGER);";
        executeStatement(sql);
    }

    public void createAddressTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS address (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	street TEXT NOT NULL,\n"
                + " house_number INTEGER NOT NULL, \n"
                + " extension TEXT, \n"
                + " postal_code TEXT NOT NULL);";
        executeStatement(sql);
    }

    public void createPriceHistoryEntryTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS price_history_entry (\n"
                + " id integer PRIMARY KEY, \n"
                + " price INTEGER NOT NULL, \n"
                + " date TEXT);";
        executeStatement(sql);
    }

    public boolean insertPropertyEntry(PropertyEntry entry) throws SQLException {
        connection.setAutoCommit(false);
        insertAddress(entry.getAddress());
        List<Addressee> addressees = entry.getAddressees().orElse(new ArrayList<Addressee>());
        for (Addressee addressee : addressees) {
            insertAddressee(addressee);
        }
        for (PriceHistoryEntry priceEntry : entry.getPriceHistories()) {
            insertPriceHistoryEntry(priceEntry);
        }

        String sql = "INSERT INTO property_entry (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1,  incrementAndGetMaxId("property_entry"));
            stmnt.setInt(2, entry.getAddress().getAddressId());
            stmnt.setString(3, LocalDate.now().toString());
            stmnt.setBoolean(4, entry.isSold());
            stmnt.setLong(5, entry.getSellPrice());
            if (stmnt.execute()) {
                return true;
            } else {
                handleSqlException();
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        handleSqlException();
        return  false;
    }

    private boolean insertAddress(Address address) {
        String sql = "Insert into address (?, ?, ?,?,?)";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, incrementAndGetMaxId("address"));
            stmnt.setString(2, address.getStreet());
            stmnt.setInt(3, address.getHouseNumber());
            stmnt.setString(4, address.getExtension());
            stmnt.setString(5, address.getPostalCode());
            return stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private int incrementAndGetMaxId(String tableName) {
        String sql = String.format("SELECT max(id) from %s", tableName);
        int maxId = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            maxId = rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return maxId + 1;
    }

    private void handleSqlException() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        }  catch (SQLException e) {
            AlertBox.display("error", "the database crashed, the program will now shutdown");
            System.exit(    3);
        }
    }

    public void createTableAddresseeIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS addressee (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name TEXT NOT NULL,\n"
                + " phone_number TEXT NOT NULL, \n"
                + " emai TEXT);";
        executeStatement(sql);
    }

    private boolean insertAddressee(Addressee addressee) {
        String sql = "Insert INTO addressee (?, ?, ?, ?)";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, incrementAndGetMaxId("addressee"));
            stmnt.setString(2, addressee.getName());
            stmnt.setString(3, addressee.getPhoneNumber().orElse(""));
            stmnt.setString(4, addressee.getEmail().orElse(""));
            return stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void executeStatement(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean insertPriceHistoryEntry(PriceHistoryEntry priceEntry) {
        String sql = "INSERT INTO price_history_entry (?, ?, ?)";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, incrementAndGetMaxId("price_history_entry"));
            stmnt.setLong(2, priceEntry.getAskingPrice());
            stmnt.setString(3, LocalDate.now().toString());
            return stmnt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
