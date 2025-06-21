package com.virtunexa.week1.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Expense {
    private final StringProperty amount;
    private final StringProperty category;
    private final StringProperty date;
    private final StringProperty description;

    public Expense(String amount, String category, String date, String description) {
        this.amount = new SimpleStringProperty(amount);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
    }

    // For TableView binding
    public StringProperty amountProperty() { return amount; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty dateProperty() { return date; }
    public StringProperty descriptionProperty() { return description; }

    // For controller logic (delete, reports)
    public String getAmount() { return amount.get(); }
    public String getCategory() { return category.get(); }
    public String getDate() { return date.get(); }
    public String getDescription() { return description.get(); }
}
