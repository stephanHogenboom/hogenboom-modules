package modules.model;

import java.time.LocalDateTime;

public class FinancialEntry {
    private Integer id;
    private String name;
    private Double value;
    private Category category;
    private LocalDateTime date;

    public FinancialEntry(Integer id, String name, Double value, Category category) {
        this.id =id;
        this.name = name;
        this.value = value;
        this.category = category;
        this.date = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
