package com.example.mozifx;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DeleteMenu {

    private ComboBox<String> tableComboBox;
    private ComboBox<String> idComboBox;
    private ComboBox<String> nameComboBox;
    private Button deleteButton;

    private static final Map<String, String[]> tableIdAndNameMap = new HashMap<>() {{
        put("filmek", new String[]{"id", "cim"});
        put("szinhazak", new String[]{"id", "nev"});
        put("eloadas", new String[]{"moziid", "datum"});
    }};

    public VBox showDeleteMenu() {
        VBox deleteMenu = new VBox();

        tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("filmek", "szinhazak", "eloadas");
        tableComboBox.setValue("filmek");
        tableComboBox.setOnAction(e -> loadIdsAndNames());

        deleteButton = new Button("Rekord törlése");
        deleteButton.setOnAction(e -> deleteRecord());

        idComboBox = new ComboBox<>();
        nameComboBox = new ComboBox<>();
        loadIdsAndNames();

        deleteMenu.getChildren().addAll(new Label("Tábla választása:"), tableComboBox,
                new Label("Rekord azonosító:"), idComboBox,
                new Label("Rekord név:"), nameComboBox,
                deleteButton);

        return deleteMenu;
    }

    private void loadIdsAndNames() {
        String selectedTable = tableComboBox.getValue();
        String[] columns = tableIdAndNameMap.get(selectedTable);

        idComboBox.getItems().clear();
        nameComboBox.getItems().clear();

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";
        String sql = "SELECT " + columns[0] + ", " + columns[1] + " FROM " + selectedTable;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                idComboBox.getItems().add(rs.getString(1));  // ID-k betöltése
                nameComboBox.getItems().add(rs.getString(2));  // Nevek betöltése
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteRecord() {
        String selectedTable = tableComboBox.getValue();
        String[] columns = tableIdAndNameMap.get(selectedTable);
        String selectedId = idComboBox.getValue();
        String selectedName = nameComboBox.getValue();

        if (selectedId == null && selectedName == null) {
            showAlert("Kérjük, válasszon ki egy rekordot ID vagy név alapján a törléshez.");
            return;
        }

        String url = "jdbc:sqlite:C:/Users/msztr/Desktop/javabeadandó/MoziFx/mozi.database";
        String sql = "";

        if (selectedId != null) {
            sql = "DELETE FROM " + selectedTable + " WHERE " + columns[0] + " = ?";
        } else if (selectedName != null) {
            sql = "DELETE FROM " + selectedTable + " WHERE " + columns[1] + " = ?";
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (selectedId != null) {
                pstmt.setString(1, selectedId);
            } else if (selectedName != null) {
                pstmt.setString(1, selectedName);
            }

            pstmt.executeUpdate();
            showAlert("Rekord sikeresen törölve.");
            loadIdsAndNames();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Hiba történt a rekord törlése során.");
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
