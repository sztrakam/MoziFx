package com.example.mozifx.Forex;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class ForexAccountInfoMenu {
    private TableView<AccountInfo> table;

    public VBox showAccountInfo() {
        table = new TableView<>();

        TableColumn<AccountInfo, String> column1 = new TableColumn<>("Számla Név");
        column1.setCellValueFactory(new PropertyValueFactory<>("accountName"));

        TableColumn<AccountInfo, String> column2 = new TableColumn<>("Számla Egyenleg");
        column2.setCellValueFactory(new PropertyValueFactory<>("balance"));

        table.getColumns().addAll(column1, column2);

        fetchAccountInfo();

        VBox vbox = new VBox(table);
        return vbox;
    }

    private void fetchAccountInfo() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-fxpractice.oanda.com/v3/accounts/101-004-30354312-001")
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
                    System.out.println("Válasz: " + responseBody);

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONObject account = jsonResponse.getJSONObject("account");
                    String accountName = account.getString("alias");
                    String balance = account.getString("balance");

                    Platform.runLater(() -> {
                        table.getItems().add(new AccountInfo(accountName, balance));
                    });
                } else {
                    System.out.println("API hiba: " + response.code() + " - " + response.message());
                }
            }
        });
    }

}

