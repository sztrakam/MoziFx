package com.example.mozifx.Forex;

public class Position {
    private String id;
    private String instrument;

    public Position(String id, String instrument) {
        this.id = id;
        this.instrument = instrument;
    }

    public String getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }
}
