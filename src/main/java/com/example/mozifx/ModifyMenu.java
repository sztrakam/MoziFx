package com.example.mozifx;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.*;


public class ModifyMenu {

    private ComboBox<Integer> recordIdComboBox;
    private TextField titleField;
    private TextField yearField;
    private TextField lengthField;
    private ComboBox<String> tableComboBox;




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


        titleField = new TextField();
        formGrid.add(new Label("Film cím:"), 0, 2);

        formGrid.add(titleField, 1, 2);


        yearField = new TextField();
        formGrid.add(new Label("Film év:"), 0, 3);
        formGrid.add(yearField, 1, 3);


        lengthField = new TextField();
        formGrid.add(new Label("Film hossz:"), 0, 4);
        formGrid.add(lengthField, 1, 4);


        Button modifyButton = new Button("Módosítás");
        modifyButton.setOnAction(e -> modifyRecord());
        formGrid.add(modifyButton, 1, 5);


        tableComboBox.setOnAction(e -> updateRecordIds());

        return new VBox(formGrid);
    }

    private void updateRecordIds() {
        String selectedTable = tableComboBox.getValue();
        recordIdComboBox.getItems().clear();
        String query="";
        if(selectedTable=="eloadas")
        {
            query="Select filmid from "+selectedTable;
        }
        else
        {
            query = "SELECT id FROM " + selectedTable;
        }

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                if(selectedTable.equals("eloadas"))
                {
                    recordIdComboBox.getItems().add(rs.getInt("filmid"));
                }
                else
                {
                    recordIdComboBox.getItems().add(rs.getInt("id"));
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void modifyRecord() {
        String selectedTable = tableComboBox.getValue();
        Integer selectedId = recordIdComboBox.getValue();
        String title = titleField.getText();
        String year = yearField.getText();
        String length = lengthField.getText();

        StringBuilder updateQuery = new StringBuilder("UPDATE ").append(selectedTable).append(" SET ");

        if ("filmek".equals(selectedTable)) {
            if (!title.isEmpty()) {
                updateQuery.append("cim = '").append(title).append("', ");
            }
            if (!year.isEmpty()) {
                updateQuery.append("ev = ").append(year).append(", ");
            }
            if (!length.isEmpty()) {
                updateQuery.append("hossz = ").append(length).append(", ");
            }
        } else if ("szinhazak".equals(selectedTable)) {
            if (!title.isEmpty()) {
                updateQuery.append("nev = '").append(title).append("', ");
            }

        } else if ("eloadas".equals(selectedTable)) {
            if (!title.isEmpty()) {
                updateQuery.append("moziid = '").append(title).append("', ");
            }

        }


        updateQuery.setLength(updateQuery.length() - 2);
        updateQuery.append(" WHERE id = ").append(selectedId);

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(updateQuery.toString());
            System.out.println("Rekord sikeresen módosítva.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
