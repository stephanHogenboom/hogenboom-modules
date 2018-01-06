package main.java.modules.financialmodule.model;

import main.java.util.Validator;

import java.time.LocalDateTime;

public class FinancialEntryBuilder {
    private Integer id;
    private String name;
    private Double value;
    private Category category;
    private LocalDateTime date;
    private Validator validator = new Validator();

    public FinancialEntryBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public FinancialEntryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public FinancialEntryBuilder setValue(Double value) {
        this.value = value;
        return this;
    }

    public FinancialEntryBuilder setCategorie(Category category) {
        this.category = category;
        return this;
    }

    public FinancialEntryBuilder setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public FinancialEntry build() {
        validator.assertNotNull(id, "id");
        validator.assertNotNull(name, "name");
        validator.assertNotNull(value, "value");
        validator.assertNotNull(category, "category");
        return new FinancialEntry(id, name, value, category);
    }
}
