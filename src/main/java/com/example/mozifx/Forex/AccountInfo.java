package com.example.mozifx.Forex;

public class AccountInfo {
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
