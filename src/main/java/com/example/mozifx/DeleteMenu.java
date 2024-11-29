package com.example.mozifx;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DeleteMenu {

    private ComboBox<String> tableComboBox;
    private ComboBox<String> idComboBox;
    private Button deleteButton;
    private VBox recordFields;
    String url = "jdbc:sqlite:C:/adatok/adatok.database";

    private static final Map<String, String[]> tableIdAndNameMap = new HashMap<>() {{
        put("filmek", new String[]{"id", "cim"});
        put("szinhazak", new String[]{"id", "nev"});
        put("eloadas", new String[]{"moziid", "datum"});
    }};

    public VBox showDeleteMenu() {
        VBox deleteMenu = new VBox();
        deleteMenu.setSpacing(10);
        deleteMenu.setPadding(new Insets(10));

        tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("filmek", "szinhazak", "eloadas");
        tableComboBox.setValue("filmek");
        tableComboBox.setOnAction(e -> loadIdsAndNames());

        deleteButton = new Button("Rekord törlése");
        deleteButton.setOnAction(e -> deleteRecord());

        idComboBox = new ComboBox<>();

        recordFields = new VBox();
        recordFields.setSpacing(10);

        loadIdsAndNames();

        deleteMenu.getChildren().addAll(
                new Label("Tábla választása:"), tableComboBox,
                new Label("Rekord azonosító:"), idComboBox,
                new Label("Rekord részletei:"), recordFields,
                deleteButton
        );

        return deleteMenu;
    }
    private void loadIdsAndNames() {
        String selectedTable = tableComboBox.getValue();
        String[] columns = tableIdAndNameMap.get(selectedTable);

        idComboBox.getItems().clear();
        recordFields.getChildren().clear();

        String sql = "SELECT " + columns[0] + ", " + columns[1] + " FROM " + selectedTable;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                idComboBox.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        idComboBox.setOnAction(e -> showRecordDetails());
    }

    private void showRecordDetails() {
        String selectedTable = tableComboBox.getValue();
        String[] columns = tableIdAndNameMap.get(selectedTable);
        String selectedId = idComboBox.getValue();

        String sql = "";
        if (selectedId != null) {
            sql = "SELECT * FROM " + selectedTable + " WHERE " + columns[0] + " = ?";

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, selectedId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    recordFields.getChildren().clear();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Label label = new Label(rs.getMetaData().getColumnName(i));
                        TextField textField = new TextField(rs.getString(i));
                        textField.setEditable(false);
                        recordFields.getChildren().addAll(label, textField);
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void deleteRecord() {
        String selectedTable = tableComboBox.getValue();
        String[] columns = tableIdAndNameMap.get(selectedTable);
        String selectedId = idComboBox.getValue();

        if (selectedId == null) {
            showAlert("Kérjük, válasszon ki egy rekordot ID alapján a törléshez.");
            return;
        }
        String sql = "";

        if (selectedId != null) {
            sql = "DELETE FROM " + selectedTable + " WHERE " + columns[0] + " = ?";

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, selectedId);

                pstmt.executeUpdate();
                showAlert("Rekord sikeresen törölve.");
                loadIdsAndNames();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                showAlert("Hiba történt a rekord törlése során.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Információ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
