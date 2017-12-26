package modules.realestate.model;

import java.time.LocalDate;

public class PriceHistoryEntry {

    private int id;
    private Long askingPrice;
    private LocalDate date;

    public Long getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(Long askingPrice) {
        this.askingPrice = askingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
