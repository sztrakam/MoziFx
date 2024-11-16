package com.example.mozifx.Forex;

import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import okhttp3.*;

import java.io.IOException;

public class ForexPositionOpenMenu {
    private ComboBox<String> currencyPairComboBox;
    private TextField quantityField;
    private ComboBox<String> directionComboBox;

    public VBox showPositionOpen() {
        currencyPairComboBox = new ComboBox<>();
        currencyPairComboBox.getItems().addAll("EUR/USD", "GBP/USD", "USD/JPY");

        quantityField = new TextField();
        quantityField.setPromptText("Mennyiség");

        directionComboBox = new ComboBox<>();
        directionComboBox.getItems().addAll("Vétel", "Eladás");

        Button openPositionButton = new Button("Pozíció Nyitás");
        openPositionButton.setOnAction(e -> openPosition());

        VBox vbox = new VBox(currencyPairComboBox, quantityField, directionComboBox, openPositionButton);
        return vbox;
    }

    private void openPosition() {
        String currencyPair = currencyPairComboBox.getValue();
        String quantity = quantityField.getText();
        String direction = directionComboBox.getValue();

        if (currencyPair == null || quantity.isEmpty() || direction == null) {
            System.out.println("Kérlek, töltsd ki az összes mezőt.");
            return;
        }

        int units = direction.equals("Eladás") ? -Integer.parseInt(quantity) : Integer.parseInt(quantity);

        String jsonPayload = String.format(
                "{ \"order\": { \"units\": \"%d\", \"instrument\": \"%s\", \"timeInForce\": \"FOK\", \"type\": \"MARKET\", \"positionFill\": \"DEFAULT\" } }",
                units, currencyPair.replace("/", "_")
        );

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

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
                    System.out.println("Pozíció sikeresen megnyitva!");
                    System.out.println(response.body().string());
                } else {
                    System.out.println("Hiba történt: " + response.message());
                    System.out.println(response.body().string());
                }
            }
        });
    }

}
