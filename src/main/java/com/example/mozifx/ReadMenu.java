package com.example.mozifx;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadMenu {

    private TableView<ObservableList<String>> table;
    private ComboBox<String> tableSelector;

    public VBox showData() {
        table = new TableView<>();

        tableSelector = new ComboBox<>();
        tableSelector.getItems().addAll("eloadas", "filmek", "szinhazak");
        tableSelector.setOnAction(e -> loadData());

        VBox vbox = new VBox(tableSelector, table);
        return vbox;
    }

    private void loadData() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";
        String selectedTable = tableSelector.getValue();
        String sql = "SELECT * FROM " + selectedTable;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            table.getColumns().clear();


            if (selectedTable.equals("eloadas")) {
                createColumns("ID", "Film ID", "Dátum", "Színház azonosító", "Nézőszám");
            } else if (selectedTable.equals("filmek")) {
                createColumns("ID", "Cím", "Év", "Hossz");
            } else if (selectedTable.equals("szinhazak")) {
                createColumns("ID", "Név", "Város", "Férőhely");
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        table.setItems(data);
    }

    private void createColumns(String... columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            final int index = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(index)));
            table.getColumns().add(column);
        }
    }

    public TableView<ObservableList<String>> getTable() {
        return table;
    }
}
