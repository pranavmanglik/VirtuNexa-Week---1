package com.virtunexa.week1.controllers;
import com.virtunexa.week1.models.Expense;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;


public class DashboardController {

    @FXML private Label clockLabel;

    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField descriptionField;

    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> amountColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> descriptionColumn;
    @FXML private TableColumn<Expense, Void> actionColumn;

    private final ObservableList<Expense> expenseList = FXCollections.observableArrayList();
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    public void initialize() {
        // Init clock
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    clockLabel.setText(LocalTime.now().format(timeFormat));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        // Populate categories
        categoryBox.setItems(FXCollections.observableArrayList("Food", "Transport", "Bills", "Health", "Other"));

        // Setup TableView columns
        amountColumn.setCellValueFactory(data -> data.getValue().amountProperty());
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());

        // Bind table to data
        expenseTable.setItems(expenseList);

        // 🔥 Set up delete button in each row
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    expenseList.remove(expense);
                });
                deleteBtn.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
    }

    @FXML
    private void handleAddExpense() {
        String amount = amountField.getText();
        String category = categoryBox.getValue();
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();

        if (amount.isEmpty() || category == null || date == null) {
            showAlert("Please fill all required fields!");
            return;
        }

        Expense expense = new Expense(amount, category, date.toString(), description);
        expenseList.add(expense);

        amountField.clear();
        categoryBox.setValue(null);
        datePicker.setValue(null);
        descriptionField.clear();
    }

    @FXML
    private void handleGenerateReport() {
        if (expenseList.isEmpty()) {
            showAlert("No expenses to report.");
            return;
        }

        double total = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense e : expenseList) {
            double amt = Double.parseDouble(e.getAmount());
            total += amt;

            categoryTotals.merge(e.getCategory(), amt, Double::sum);
        }

        StringBuilder report = new StringBuilder("Expense Report (Session Only):\n\n");
        report.append("Total: ₹").append(total).append("\n\n");
        report.append("Breakdown by Category:\n");

        for (var entry : categoryTotals.entrySet()) {
            report.append("- ").append(entry.getKey())
                    .append(": ₹").append(entry.getValue()).append("\n");
        }

        // Show in alert dialog
        Alert reportDialog = new Alert(Alert.AlertType.INFORMATION);
        reportDialog.setTitle("Expense Report");
        reportDialog.setHeaderText("Current Session Summary");
        reportDialog.setContentText(report.toString());
        reportDialog.getDialogPane().setPrefWidth(400);
        reportDialog.showAndWait();
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
