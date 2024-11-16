package com.example.mozifx.Forex;

public class Position {
    private String id;
    private String instrument;

    // Konstruktor
    public Position(String id, String instrument) {
        this.id = id;
        this.instrument = instrument;
    }

    // Getterek
    public String getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }
}
