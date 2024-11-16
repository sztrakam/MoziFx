package com.example.mozifx.Forex;

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
                .url("https://api-fxpractice.oanda.com/v3/accounts/{101-004-30354312-001}") // Itt cseréld ki az accountID-t a megfelelő értékre
                .header("Authorization", "Bearer {f85eabd4e5a7244f676c1f274f7535a8-f21ee92983d86d7c35bedb0c62b55536}")  // Az OANDA API kulcs
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    String accountName = jsonResponse.getJSONObject("account").getString("name");
                    String balance = jsonResponse.getJSONObject("account").getString("balance");
                    table.getItems().add(new AccountInfo(accountName, balance));
                }
            }
        });
    }
}

class AccountInfo {
    private String accountName;
    private String balance;

    public AccountInfo(String accountName, String balance) {
        this.accountName = accountName;
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getBalance() {
        return balance;
    }
}
