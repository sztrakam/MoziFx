module com.example.mozifx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.mozifx to javafx.fxml;
    exports com.example.mozifx;
}