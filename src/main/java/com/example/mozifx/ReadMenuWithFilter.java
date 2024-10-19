package com.example.mozifx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.*;

public class ReadMenuWithFilter {

    private TableView<ObservableList<String>> table;
    private TextField searchField;
    private ComboBox<String> tableComboBox;


    public VBox showData() {
        table = new TableView<>();

        loadTableData("filmek");

        VBox formAndTable = createForm();

        return new VBox(formAndTable, table);
    }
    private RadioButton firstCheckbox;
    private RadioButton secondCheckbox;

    private VBox createForm() {
        GridPane formGrid = new GridPane();


        tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("filmek", "szinhazak", "eloadas");
        tableComboBox.setValue("filmek");
        tableComboBox.setOnAction(e -> {
            loadTableData(tableComboBox.getValue());
            updateCheckboxes();
        });
        formGrid.add(new Label("Tábla választás:"), 0, 0);
        formGrid.add(tableComboBox, 1, 0);

        searchField = new TextField();
        searchField.setPromptText("Keresés...");
        formGrid.add(new Label("Keresés:"), 0, 1);
        formGrid.add(searchField, 1, 1);

        firstCheckbox = new RadioButton();
        secondCheckbox = new RadioButton();
        formGrid.add(firstCheckbox, 0, 2);
        formGrid.add(secondCheckbox, 0, 3);

        Button searchButton = new Button("Keresés");
        searchButton.setOnAction(e -> filterData());
        formGrid.add(searchButton, 0, 5);

        updateCheckboxes();

        return new VBox(formGrid);
    }

    private void updateCheckboxes() {
        String selectedTable = tableComboBox.getValue();
        switch (selectedTable) {
            case "filmek":
                firstCheckbox.setText("1950 után");
                secondCheckbox.setText("1950 előtt");
                break;
            case "szinhazak":
                firstCheckbox.setText("Férőhely 300 alatt");
                secondCheckbox.setText("Férőhely 300 felett");
                break;
            case "eloadas":
                firstCheckbox.setText("Nézőszám 100 alatt");
                secondCheckbox.setText("Nézőszám 100 felett");
                break;
        }
    }
    private void filterData() {
        String searchTerm = searchField.getText();
        String selectedTable = tableComboBox.getValue();


        boolean firstCheckboxSelected = firstCheckbox.isSelected();
        if(firstCheckboxSelected) {
            secondCheckbox.setSelected(false);
        }
        boolean secondCheckboxSelected = secondCheckbox.isSelected();
        if(secondCheckboxSelected)
        {
            firstCheckbox.setSelected(false);
        }


        StringBuilder filterQuery = new StringBuilder("SELECT * FROM ").append(selectedTable);

        switch (selectedTable) {
            case "filmek":
                if (!searchTerm.isEmpty()) {
                    filterQuery.append(" WHERE cim LIKE '%").append(searchTerm).append("%'");
                }
                if (firstCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND ev > 1950");
                    } else {
                        filterQuery.append(" WHERE ev > 1950");
                    }
                }
                if (secondCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND ev < 1950");
                    } else {
                        filterQuery.append(" WHERE ev < 1950");
                    }
                }
                break;

            case "szinhazak":
                if (!searchTerm.isEmpty()) {
                    filterQuery.append(" WHERE nev LIKE '%").append(searchTerm).append("%'");
                }
                if (firstCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND ferohely < 300");
                    } else {
                        filterQuery.append(" WHERE ferohely < 300");
                    }
                }
                if (secondCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND ferohely >= 300");
                    } else {
                        filterQuery.append(" WHERE ferohely >= 300");
                    }
                }
                break;

            case "eloadas":
                if (!searchTerm.isEmpty()) {
                    filterQuery.append(" WHERE szinhaz_nev LIKE '%").append(searchTerm).append("%'");
                }
                if (firstCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND nezoszam < 100");
                    } else {
                        filterQuery.append(" WHERE nezoszam < 100");
                    }
                }
                if (secondCheckboxSelected) {
                    if (filterQuery.toString().contains("WHERE")) {
                        filterQuery.append(" AND nezoszam >= 100");
                    } else {
                        filterQuery.append(" WHERE nezoszam >= 100");
                    }
                }
                break;
        }

        loadTableData(selectedTable, filterQuery.toString());
    }
    private void loadTableData(String tableName) {
        loadTableData(tableName, "SELECT * FROM " + tableName);
    }

    private void loadTableData(String tableName, String query) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        table.getColumns().clear();

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final int colIndex = i - 1;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(i));
                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
                table.getColumns().add(column);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            table.setItems(data);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
