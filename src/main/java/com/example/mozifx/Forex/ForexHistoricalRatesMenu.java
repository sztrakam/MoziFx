package com.example.mozifx.Forex;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;


public class ForexHistoricalRatesMenu {
    private ComboBox<String> currencyPairComboBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private LineChart<Number, Number> chart;

    public VBox showHistoricalRates() {
        currencyPairComboBox = new ComboBox<>();
        currencyPairComboBox.getItems().addAll("EUR/USD", "GBP/USD", "USD/JPY");

        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        Button fetchButton = new Button("Lekérés");
        fetchButton.setOnAction(e -> fetchHistoricalRates());

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Dátum");
        yAxis.setLabel("Árfolyam");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Historikus árfolyamok");

        VBox vbox = new VBox(currencyPairComboBox, startDatePicker, endDatePicker, fetchButton, chart);
        return vbox;
    }

    private void fetchHistoricalRates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        String instrument = currencyPairComboBox.getValue().replace("/", "_");  // Pl. EUR/USD -> EUR_USD

        String url = String.format("https://api-fxpractice.oanda.com/v3/instruments/%s/candles?granularity=D&start=%s&end=%s",
                instrument, startDate, endDate);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer 49d2429c39fc29c10fb4b5dafaf81280-588024a435952ec7343e640ec48eff64")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONArray candles = new JSONObject(response.body().string()).getJSONArray("candles");
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();

                    for (int i = 0; i < candles.length(); i++) {
                        JSONObject candle = candles.getJSONObject(i);

                        String timeString = candle.getString("time");
                        long time = java.time.Instant.parse(timeString).getEpochSecond();

                        double close = candle.getJSONObject("mid").getDouble("c");

                        series.getData().add(new XYChart.Data<>(time, close));
                    }

                    Platform.runLater(() -> {
                        chart.getData().clear();
                        chart.getData().add(series);
                    });
                } else {
                    System.out.println("API hiba történt: " + response.code() + " - " + response.message());
                    System.out.println("Válasz törzs: " + response.body().string());
                }
            }
        });
    }


}


