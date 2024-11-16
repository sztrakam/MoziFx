package com.example.mozifx.Forex;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ForexPositionCloseMenu {
    private TextField positionIdField;
    private final ObservableList<Position> openPositions = FXCollections.observableArrayList();
    private TableView<Position> tableView;

    public VBox showPositionClose() {
        positionIdField = new TextField();
        positionIdField.setPromptText("Pozíció ID");

        Button closePositionButton = new Button("Pozíció Zárás");
        closePositionButton.setOnAction(e -> closePosition());

        tableView = new TableView<>();
        tableView.setItems(openPositions);

        TableColumn<Position, String> instrumentColumn = new TableColumn<>("Instrument");
        instrumentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument()));
        tableView.getColumns().add(instrumentColumn);

        VBox vbox = new VBox(positionIdField, closePositionButton, tableView);
        return vbox;
    }

    private void closePosition() {
        String tradeID = positionIdField.getText();
        if (tradeID == null || tradeID.isEmpty()) {
            System.out.println("Nem adtál meg trade ID-t.");
            return;
        }

        closeTradeById(tradeID);
    }
    private void closeTradeById(String tradeID) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001/trades/" + tradeID + "/close";

        RequestBody body = RequestBody.create("", null);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer 49d2429c39fc29c10fb4b5dafaf81280-588024a435952ec7343e640ec48eff64")
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                System.out.println("Pozíció zárás API válasz: " + responseBody);
                if (response.isSuccessful()) {
                    System.out.println("Pozíció sikeresen lezárva (tradeID: " + tradeID + ").");
                    refreshOpenPositions();
                } else {
                    System.out.println("Hiba a trade zárásakor: " + response.code() + " - " + responseBody);
                }
            }
        });
    }


    private void refreshOpenPositions() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001/positions")
                .header("Authorization", "Bearer 49d2429c39fc29c10fb4b5dafaf81280-588024a435952ec7343e640ec48eff64")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray positions = jsonResponse.optJSONArray("positions");
                    updateOpenPositions(positions);
                } else {
                    System.out.println("Hiba a pozíciók lekérésekor: " + response.code());
                }
            }
        });
    }

    private void updateOpenPositions(JSONArray positions) {
        Platform.runLater(() -> {
            openPositions.clear();
            for (int i = 0; i < positions.length(); i++) {
                JSONObject position = positions.getJSONObject(i);
                String instrument = position.getString("instrument");
                Position pos = new Position(instrument, instrument);
                openPositions.add(pos);
            }
            tableView.setItems(openPositions);
            tableView.refresh();
        });
    }


    private void closePositionWithUnits(String positionId, String instrument, String units) {
        OkHttpClient client = new OkHttpClient();
        String json = "{"
                + "\"order\": {"
                + "\"units\": \"" + units + "\","
                + "\"instrument\": \"" + instrument + "\","
                + "\"type\": \"MARKET\","
                + "\"positionFill\": \"DEFAULT\""
                + "} "
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001/orders")
                .header("Authorization", "Bearer 49d2429c39fc29c10fb4b5dafaf81280-588024a435952ec7343e640ec48eff64")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Pozíció sikeresen lezárva.");
                    removePositionFromList(positionId);
                    refreshOpenPositions();
                } else {
                    String errorMessage = response.body().string();
                    System.out.println("Hiba a pozíció zárásakor: " + response.code() + " - " + errorMessage);
                }
            }
        });
    }

    private void removePositionFromList(String positionId) {
        Platform.runLater(() -> {
            boolean removed = openPositions.removeIf(position -> position.getId().equals(positionId));
            if (removed) {
                System.out.println("Pozíció eltávolítva: " + positionId);
            } else {
                System.out.println("Pozíció nem található: " + positionId);
            }
            tableView.refresh();
        });
    }
}
