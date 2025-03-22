module com.example.mappracticalpractice1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset;

    opens CourierApp to javafx.fxml;
    exports CourierApp;
}