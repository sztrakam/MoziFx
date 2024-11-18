package com.example.mozifx.Forex;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class ForexCurrentRatesMenu {
    private ComboBox<String> currencyPairComboBox;
    private TextField rateTextField;

    public VBox showCurrentRates() {
        currencyPairComboBox = new ComboBox<>();
        currencyPairComboBox.getItems().addAll("EUR/USD", "GBP/USD", "USD/JPY");

        rateTextField = new TextField();
        rateTextField.setPromptText("Aktuális ár...");
        rateTextField.setEditable(false);

        Button getRateButton = new Button("Lekérés");
        getRateButton.setOnAction(e -> fetchCurrentRate(currencyPairComboBox.getValue()));

        VBox vbox = new VBox(currencyPairComboBox, getRateButton, rateTextField);
        return vbox;
    }

    private void fetchCurrentRate(String currencyPair) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001/pricing?instruments=" + currencyPair;
        Request request = new Request.Builder()
                .url(url)
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

                    double rate = jsonResponse.getJSONArray("prices")
                            .getJSONObject(0)
                            .getDouble("closeoutBid");

                    Platform.runLater(() -> {
                        rateTextField.setText(String.format("%.4f", rate));
                    });
                }
            }
        });
    }
}
