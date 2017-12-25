package modules.realestate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PropertyEntree {

    private Address address;
    private List<PriceHistoryEntree> priceHistories;
    private Optional<Addressee> addressee;
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

    public List<PriceHistoryEntree> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(List<PriceHistoryEntree> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public Optional<Addressee> getAddressee() {
        return addressee;
    }

    public void setAddressee(Optional<Addressee> addressee) {
        this.addressee = addressee;
    }
}
