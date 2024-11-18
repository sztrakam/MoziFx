package com.example.mozifx.Forex;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import javafx.application.Platform;

public class ForexOpenPositionsMenu {
    private TableView<PositionInfo> table;

    public VBox showOpenPositions() {
        table = new TableView<>();

        TableColumn<PositionInfo, String> column1 = new TableColumn<>("Pozíció ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("positionId"));

        TableColumn<PositionInfo, String> column2 = new TableColumn<>("Devizapár");
        column2.setCellValueFactory(new PropertyValueFactory<>("currencyPair"));

        TableColumn<PositionInfo, String> column3 = new TableColumn<>("Mennyiség");
        column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<PositionInfo, String> column4 = new TableColumn<>("Irány");
        column4.setCellValueFactory(new PropertyValueFactory<>("direction"));

        table.getColumns().addAll(column1, column2, column3, column4);

        fetchOpenPositions();

        VBox vbox = new VBox(table);
        return vbox;
    }

    private void fetchOpenPositions() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001/positions")
                .header("Authorization", "Bearer 49d2429c39fc29c10fb4b5dafaf81280-588024a435952ec7343e640ec48eff64")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hiba");
                    alert.setHeaderText("API-hívás hiba");
                    alert.setContentText("Nem sikerült kapcsolódni a szerverhez.");
                    alert.showAndWait();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("API válasz: " + responseBody);

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray positions = jsonResponse.optJSONArray("positions");

                    if (positions == null || positions.length() == 0) {
                        System.out.println("Nincs nyitott pozíció.");
                    } else {
                        Platform.runLater(() -> {
                            table.getItems().clear();
                            for (int i = 0; i < positions.length(); i++) {
                                JSONObject position = positions.getJSONObject(i);
                                String currencyPair = position.getString("instrument");

                                JSONObject longPosition = position.getJSONObject("long");
                                String longUnits = longPosition.getString("units");
                                if (!longUnits.equals("0")) {
                                    String positionId = getPositionId(longPosition);
                                    table.getItems().add(new PositionInfo(positionId, currencyPair, longUnits, "Vétel"));
                                }

                                JSONObject shortPosition = position.getJSONObject("short");
                                String shortUnits = shortPosition.getString("units");
                                if (!shortUnits.equals("0")) {
                                    String positionId = getPositionId(shortPosition);
                                    table.getItems().add(new PositionInfo(positionId, currencyPair, shortUnits, "Eladás"));
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("API hiba: " + response.message());
                }
            }
        });
    }

    private String getPositionId(JSONObject position) {
        JSONArray tradeIDs = position.optJSONArray("tradeIDs");
        return (tradeIDs != null && tradeIDs.length() > 0) ? tradeIDs.getString(0) : "N/A";
    }

}

