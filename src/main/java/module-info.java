module com.example.mozifx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires okhttp3;
    requires org.json;
    requires java.sql;
    requires java.desktop;
    opens com.example.mozifx to javafx.fxml;
    opens com.example.mozifx.Forex to javafx.base;

    exports com.example.mozifx;
    exports com.example.mozifx.Forex;
}
