package com.example.mozifx;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WriteMenu {

    private TextField titleField;
    private TextField yearField;
    private TextField lengthField;
    private TextField filmIdField;
    private TextField moziIdField;
    private ComboBox<String> tableComboBox;
    private GridPane formGrid;

    public VBox showWriteMenu() {
        VBox layout = new VBox(10);
        layout.getChildren().add(createForm());
        return layout;
    }

    private VBox createForm() {
        formGrid = new GridPane();

        tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("filmek", "szinhazak", "eloadas");
        tableComboBox.setValue("filmek");
        tableComboBox.setOnAction(e -> updateFormFields());
        formGrid.add(new Label("Tábla választása:"), 0, 0);
        formGrid.add(tableComboBox, 1, 0);


        updateFormFields();

        return new VBox(formGrid);
    }

    private void updateFormFields() {
        formGrid.getChildren().clear();
        formGrid.add(new Label("Tábla választása:"), 0, 0);
        formGrid.add(tableComboBox, 1, 0);

        String selectedTable = tableComboBox.getValue();

        if ("filmek".equals(selectedTable)) {
            titleField = new TextField();
            formGrid.add(new Label("Film cím:"), 0, 1);
            formGrid.add(titleField, 1, 1);

            yearField = new TextField();
            formGrid.add(new Label("Film év:"), 0, 2);
            formGrid.add(yearField, 1, 2);

            lengthField = new TextField();
            formGrid.add(new Label("Film hossz:"), 0, 3);
            formGrid.add(lengthField, 1, 3);
        } else if ("szinhazak".equals(selectedTable)) {
            titleField = new TextField();
            formGrid.add(new Label("Szinház név:"), 0, 1);
            formGrid.add(titleField, 1, 1);

            yearField = new TextField();
            formGrid.add(new Label("Város:"), 0, 2);
            formGrid.add(yearField, 1, 2);

            lengthField = new TextField();
            formGrid.add(new Label("Férőhely:"), 0, 3);
            formGrid.add(lengthField, 1, 3);
        } else if ("eloadas".equals(selectedTable)) {
            titleField = new TextField();
            formGrid.add(new Label("Előadás név:"), 0, 1);
            formGrid.add(titleField, 1, 1);

            filmIdField = new TextField();
            formGrid.add(new Label("Film ID:"), 0, 2);
            formGrid.add(filmIdField, 1, 2);

            moziIdField = new TextField();
            formGrid.add(new Label("Mozi ID:"), 0, 3);
            formGrid.add(moziIdField, 1, 3);

            yearField = new TextField();
            formGrid.add(new Label("Dátum:"), 0, 4);
            formGrid.add(yearField, 1, 4);

            lengthField = new TextField();
            formGrid.add(new Label("Nézőszám:"), 0, 5);
            formGrid.add(lengthField, 1, 5);
        }

        Button addButton = new Button("Hozzáadás");
        addButton.setOnAction(e -> addRecord());
        formGrid.add(addButton, 1, 6);
    }

    private void addRecord() {
        String selectedTable = tableComboBox.getValue();
        String title = titleField.getText();
        String year = yearField.getText();
        String length = lengthField.getText();
        String filmId = filmIdField != null ? filmIdField.getText() : "1";
        String moziId = moziIdField != null ? moziIdField.getText() : "1";

        String insertQuery = "";
        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/java előadás beadandó adatbázis/Java előadás beadandó/MoziFx/mozi.database";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            if ("filmek".equals(selectedTable)) {
                insertQuery = "INSERT INTO " + selectedTable + " (cim, ev, hossz) VALUES ('" + title + "', " + year + ", " + length + ")";
            } else if ("szinhazak".equals(selectedTable)) {
                insertQuery = "INSERT INTO " + selectedTable + " (nev, varos, ferohely) VALUES ('" + title + "', '" + year + "', " + length + ")";
            } else if ("eloadas".equals(selectedTable)) {
                insertQuery = "INSERT INTO " + selectedTable + " (moziid, filmid, datum, nezoszam, bevetel) VALUES (" + moziId + ", " + filmId + ", '" + title + "', " + year + ", " + length + ")";
            }

            stmt.executeUpdate(insertQuery);
            System.out.println("Rekord sikeresen hozzáadva.");
            titleField.clear();
            yearField.clear();
            lengthField.clear();
            if (filmIdField != null) filmIdField.clear();
            if (moziIdField != null) moziIdField.clear();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
