package modules.realestate.model;

import java.time.LocalDate;

public class PriceHistoryEntry {

    private Long askingPrice;
    private LocalDate date;

    public Long getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(Long askingPrice) {
        this.askingPrice = askingPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PriceHistoryEntry{" +
                "askingPrice=" + askingPrice +
                ", date=" + date +
                '}';
    }

}
