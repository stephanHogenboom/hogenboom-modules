package modules.realestate;

import acces.GeneralDAO;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PriceHistoryEntry;
import modules.realestate.model.PropertyEntry;

import java.sql.*;
import java.time.LocalDate;

public class RealEstateDAO extends GeneralDAO {
    private Connection connection = getConnection();


    public void insertPropertyEntry(PropertyEntry entry) throws SQLException {
        boolean sold = entry.getSellPrice() != null;
        insertAddress(entry.getAddress());
        /*List<Addressee> addressees = entry.getAddressees().orElse(new ArrayList());
        for (Addressee addressee : addressees) {
            insertAddressee(addressee);
        }*/
        for (PriceHistoryEntry priceEntry : entry.getPriceHistories()) {
            insertPriceHistoryEntry(priceEntry);
        }
        System.out.println(entry.getSellPrice());
        String sql = String.format("INSERT INTO property_entry  VALUES (?, ?, ?, ?%s)", sold? ",? " : "");
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, incrementAndGetMaxId("property_entry"));
            stmnt.setInt(2, entry.getAddress().getAddressId());
            stmnt.setString(3, LocalDate.now().toString());
            stmnt.setBoolean(4, entry.isSold());
            if (sold) stmnt.setLong(5, entry.getSellPrice());
            stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertAddress(Address address) {
        String sql = "Insert into address VALUES (?, ?, ?,?,?)";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, incrementAndGetMaxId("address"));
            stmnt.setString(2, address.getStreet());
            stmnt.setInt(3, address.getHouseNumber());
            stmnt.setString(4, address.getExtension());
            stmnt.setString(5, address.getPostalCode());
            stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
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

    private boolean insertAddressee(Addressee addressee) {
        String sql = "Insert INTO addressee VALUES (?, ?, ?, ?);";
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

    public boolean insertPriceHistoryEntry(PriceHistoryEntry priceEntry) {
        String sql = "INSERT INTO price_history_entry VALUES (?, ?, ?);";
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
