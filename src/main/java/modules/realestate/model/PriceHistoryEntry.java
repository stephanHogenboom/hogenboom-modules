package modules.realestate.model;

import java.util.Optional;

public class PriceHistoryEntry {

    private Optional<Long> askingPrice;
    private Optional<Long> sellPrice;

    public Optional<Long> getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(Optional<Long> askingPrice) {
        this.askingPrice = askingPrice;
    }

    public Optional<Long> getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Optional<Long> sellPrice) {
        this.sellPrice = sellPrice;
    }
}
