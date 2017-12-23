package modules.model;

import java.time.LocalDateTime;

public class FinancialEntry {
    private String name;
    private Double value;
    private Category category;
    private LocalDateTime date;

    public FinancialEntry(String name, Double value, Category category) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.date = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
