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
    private TextField idSearchField;
    private ComboBox<String> tableComboBox;
    private RadioButton firstCheckbox;
    private RadioButton secondCheckbox;

    public VBox showData() {
        table = new TableView<>();

        loadTableData("filmek");

        VBox formAndTable = createForm();

        return new VBox(formAndTable, table);
    }

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
        searchField.setPromptText("Keresés név szerint...");
        formGrid.add(new Label("Név keresés:"), 0, 1);
        formGrid.add(searchField, 1, 1);

        idSearchField = new TextField();
        idSearchField.setPromptText("Keresés ID szerint...");
        formGrid.add(new Label("ID keresés:"), 0, 2);
        formGrid.add(idSearchField, 1, 2);

        firstCheckbox = new RadioButton();
        secondCheckbox = new RadioButton();
        formGrid.add(firstCheckbox, 0, 3);
        formGrid.add(secondCheckbox, 0, 4);

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
        String idSearchTerm = idSearchField.getText();
        String selectedTable = tableComboBox.getValue();

        boolean firstCheckboxSelected = firstCheckbox.isSelected();
        boolean secondCheckboxSelected = secondCheckbox.isSelected();

        StringBuilder filterQuery = new StringBuilder("SELECT * FROM ").append(selectedTable);

        boolean hasCondition = false;

        if (!idSearchTerm.isEmpty()) {
            filterQuery.append(" WHERE id = ").append(idSearchTerm);
            hasCondition = true;
        }

        if (!searchTerm.isEmpty()) {
            if (hasCondition) {
                filterQuery.append(" AND");
            } else {
                filterQuery.append(" WHERE");
                hasCondition = true;
            }
            filterQuery.append(" cim LIKE '%").append(searchTerm).append("%'");
        }

        switch (selectedTable) {
            case "filmek":
                if (firstCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" ev > 1950");
                } else if (secondCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" ev < 1950");
                }
                break;

            case "szinhazak":
                if (firstCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" ferohely < 300");
                } else if (secondCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" ferohely >= 300");
                }
                break;

            case "eloadas":
                if (firstCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" nezoszam < 100");
                } else if (secondCheckboxSelected) {
                    filterQuery.append(hasCondition ? " AND" : " WHERE").append(" nezoszam >= 100");
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

        String url = "jdbc:sqlite:C:/adatok/adatok.database";
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
