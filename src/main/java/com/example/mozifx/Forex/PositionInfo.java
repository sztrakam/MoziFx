package com.example.mozifx.Forex;

public class PositionInfo {
    public String positionId;
    public String currencyPair;
    public String quantity;
    public String direction;

    public PositionInfo(String positionId, String currencyPair, String quantity, String direction) {
        this.positionId = positionId;
        this.currencyPair = currencyPair;
        this.quantity = quantity;
        this.direction = direction;
    }

    public String getPositionId() {
        return positionId;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDirection() {
        return direction;
    }
}
