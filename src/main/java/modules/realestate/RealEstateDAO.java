package modules.realestate;

import acces.GeneralDAO;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PriceHistoryEntry;
import modules.realestate.model.PropertyEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RealEstateDAO extends GeneralDAO {
    private Connection connection = getConnection();

    boolean deletePropertyByKixcode(String kix) {
        String sql = "DELETE FROM property_entry where address_id = ?;";
        boolean flag = false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, kix);
            flag = statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("flag=" + flag);
        return flag;
    }

    List<PropertyEntry> getPropertyEntries() {
        List<PropertyEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM property_entry;";
        try (Statement stmnt = connection.createStatement();
             ResultSet rs = stmnt.executeQuery(sql);) {
            while (rs.next()) {
                PropertyEntry entry = getEntry(rs);
                entries.add(entry);
            }
            return entries;
        } catch (SQLException e) {
            e.printStackTrace();
            return entries;
        }
    }

    private List<PriceHistoryEntry> getPriceHistoryEntries(int propertyId) {
        List<PriceHistoryEntry> entries = new ArrayList<>();
        String sql = String.format("SELECT * FROM price_history_entry where property_id = '%s';", propertyId);
        try (Statement stmnt = connection.createStatement();
             ResultSet rs = stmnt.executeQuery(sql);) {
            while (rs.next()) {
                PriceHistoryEntry entry = new PriceHistoryEntry();
                entry.setAskingPrice(rs.getLong(2));
                entry.setDate(LocalDate.parse(rs.getString(3)));
                entries.add(entry);
            }
            return entries;
        } catch (SQLException e) {
            e.printStackTrace();
            return entries;
        }
    }

    private Address getAddress(String kix) {
        Address address = new Address();
        String sql = String.format("SELECT * FROM address where kix_code = '%s';", kix);
        try (Statement stmnt = connection.createStatement();
             ResultSet rs = stmnt.executeQuery(sql)) {
            if (rs.next()) {
                address.setKixCode(kix);
                address.setCountry(rs.getString(2));
                address.setStreet(rs.getString(3));
                address.setHouseNumber(rs.getInt(4));
                address.setExtension(GeneralDAO.getStringSaveNullSave(rs.getString(5)));
                address.setPostalCode(rs.getString(6));
                address.setCity(rs.getString(7));
                return address;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Addressee> getAddressees(Address address) {
        List<Addressee> addresseeList = new ArrayList<>();
        String sql = String.format("SELECT * FROM addressee where kix_code = '%s';",address.getKixCode());
        try (Statement stmnt = connection.createStatement();
             ResultSet rs = stmnt.executeQuery(sql)) {
            while (rs.next()) {
                Addressee addressee = new Addressee();
                addressee.setName(rs.getString(1));
                addressee.setPhoneNumber(Optional.ofNullable(rs.getString(2)));
                addressee.setEmail(Optional.ofNullable(rs.getString(3)));
                addressee.setAddress(address);
                addresseeList.add(addressee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return addresseeList;
    }


    void insertPropertyEntry(PropertyEntry entry) throws SQLException {
        System.out.println(entry);
        insertAddress(entry.getAddress());
        System.out.println(entry.getAddressees());
        List<Addressee> addressees = entry.getAddressees().orElse(new ArrayList());
        for (Addressee addressee : addressees) {
            insertAddressee(addressee);
        }
        entry.setId(incrementAndGetMaxId("property_entry"));
        for (PriceHistoryEntry priceEntry : entry.getPriceHistories()) {
            insertPriceHistoryEntry(priceEntry, entry);
        }
        String sql = "INSERT INTO property_entry  VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, entry.getId());
            stmnt.setString(2, entry.getAddress().getKixCode());
            stmnt.setString(3, entry.getDate().toString());
            stmnt.setBoolean(4, entry.isSold());
            stmnt.setLong(5, entry.getSellPrice() != null ? entry.getSellPrice() : 0);
            stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertAddress(Address address) {
        String sql = "Insert into address VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, address.getKixCode());
            stmnt.setString(2, address.getCountry());
            stmnt.setString(3, address.getStreet());
            stmnt.setInt(4, address.getHouseNumber());
            stmnt.setString(5, address.getExtension());
            stmnt.setString(6, address.getPostalCode());
            stmnt.setString(7, address.getCity());
            stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private int incrementAndGetMaxId(String tableName) {
        String sql = String.format("SELECT max(oid) from %s", tableName);
        int maxId = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            maxId = rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return maxId + 1;
    }

    private boolean insertAddressee(Addressee addressee) {
        String sql = "Insert INTO addressee VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, addressee.getName());
            stmnt.setString(2, addressee.getPhoneNumber().orElse(""));
            stmnt.setString(3, addressee.getEmail().orElse(""));
            stmnt.setString(4, addressee.getAddress().getKixCode());
            return stmnt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertPriceHistoryEntry(PriceHistoryEntry priceEntry, PropertyEntry entry) {
        String sql = "INSERT INTO price_history_entry VALUES (?, ?, ?);";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setLong(1, entry.getId());
            stmnt.setLong(2, priceEntry.getAskingPrice());
            stmnt.setString(3, LocalDate.now().toString());
            return stmnt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PropertyEntry getEntry(ResultSet rs) {
        PropertyEntry entry = new PropertyEntry();
        try {
            entry.setId(rs.getInt(1));
            entry.setAddress(getAddress(rs.getString(2)));
            entry.setDate(LocalDate.parse(rs.getString(3)));
            entry.setSold(rs.getInt(4) == 1); // boolean is 0 for false 1 for true
            entry.setSellPrice(rs.getLong(5));
            entry.setPriceHistories(getPriceHistoryEntries(entry.getId()));
            entry.setAddressees(Optional.ofNullable(getAddressees(entry.getAddress())));
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
            return null;
        }
        return entry;
    }
}
