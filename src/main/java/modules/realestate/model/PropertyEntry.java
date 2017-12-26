package modules.realestate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PropertyEntry {

    private int id;
    private Address address;
    private List<PriceHistoryEntry> priceHistories;
    private Optional<List<Addressee>> addressees;
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<PriceHistoryEntry> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(List<PriceHistoryEntry> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public Optional<List<Addressee>> getAddressees() {
        return addressees;
    }

    public void setAddressees(Optional<List<Addressee>> addressees) {
        this.addressees = addressees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
