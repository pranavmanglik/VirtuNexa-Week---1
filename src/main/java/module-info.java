module com.virtunexa.week1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.virtunexa.week1 to javafx.fxml;
    exports com.virtunexa.week1;
    exports com.virtunexa.week1.controllers;
    opens com.virtunexa.week1.controllers to javafx.fxml;
}