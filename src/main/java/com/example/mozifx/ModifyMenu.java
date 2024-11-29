package com.example.mozifx;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.*;

public class ModifyMenu {

    private ComboBox<Integer> recordIdComboBox;
    private ComboBox<String> tableComboBox;
    private VBox dynamicFieldsContainer;
    private Map<String, TextField> textFieldsMap = new HashMap<>();

    String url = "jdbc:sqlite:C:/adatok/adatok.database";

    public VBox showModifyMenu() {
        VBox layout = new VBox(10);
        layout.getChildren().add(createForm());
        return layout;
    }

    private VBox createForm() {
        GridPane formGrid = new GridPane();

        tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("filmek", "szinhazak", "eloadas");
        tableComboBox.setValue("filmek");
        formGrid.add(new Label("Tábla választása:"), 0, 0);
        formGrid.add(tableComboBox, 1, 0);

        recordIdComboBox = new ComboBox<>();
        updateRecordIds();
        formGrid.add(new Label("Rekord azonosítója:"), 0, 1);
        formGrid.add(recordIdComboBox, 1, 1);

        dynamicFieldsContainer = new VBox(10);
        formGrid.add(dynamicFieldsContainer, 0, 2, 2, 1);

        Button modifyButton = new Button("Módosítás");
        modifyButton.setOnAction(e -> modifyRecord());
        formGrid.add(modifyButton, 1, 3);

        tableComboBox.setOnAction(e -> {
            updateRecordIds();
            updateDynamicFields();
        });

        recordIdComboBox.setOnAction(e -> loadRecordData());

        return new VBox(formGrid);
    }

    private void updateRecordIds() {
        String selectedTable = tableComboBox.getValue();
        recordIdComboBox.getItems().clear();
        String query = selectedTable.equals("eloadas") ? "SELECT filmid AS id FROM " + selectedTable : "SELECT id FROM " + selectedTable;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                recordIdComboBox.getItems().add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateDynamicFields() {
        String selectedTable = tableComboBox.getValue();
        dynamicFieldsContainer.getChildren().clear();
        textFieldsMap.clear();

        String query = "PRAGMA table_info(" + selectedTable + ")";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String columnName = rs.getString("name");
                Label columnLabel = new Label(columnName + ":");
                TextField textField = new TextField();
                dynamicFieldsContainer.getChildren().addAll(columnLabel, textField);
                textFieldsMap.put(columnName, textField);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadRecordData() {
        String selectedTable = tableComboBox.getValue();
        Integer selectedId = recordIdComboBox.getValue();
        if (selectedId == null) {
            return;
        }

        String query = "SELECT * FROM " + selectedTable + " WHERE id = " + selectedId;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                for (Map.Entry<String, TextField> entry : textFieldsMap.entrySet()) {
                    String columnName = entry.getKey();
                    TextField field = entry.getValue();
                    field.setText(rs.getString(columnName));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void modifyRecord() {
        String selectedTable = tableComboBox.getValue();
        Integer selectedId = recordIdComboBox.getValue();
        StringBuilder updateQuery = new StringBuilder("UPDATE ").append(selectedTable).append(" SET ");

        for (Map.Entry<String, TextField> entry : textFieldsMap.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue().getText();
            if (!value.isEmpty()) {
                updateQuery.append(columnName).append(" = '").append(value).append("', ");
            }
        }

        updateQuery.setLength(updateQuery.length() - 2);
        updateQuery.append(" WHERE id = ").append(selectedId);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(updateQuery.toString());
            System.out.println("Rekord sikeresen módosítva.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
