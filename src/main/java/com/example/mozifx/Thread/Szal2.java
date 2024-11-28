package com.example.mozifx.Thread;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Szal2 implements Runnable {
    private Label sz2;

    public Szal2(Label sz2) {
        this.sz2 = sz2;
    }

    @Override
    public void run() {
        while (true) {
            // UI frissítése JavaFX szálon
            Platform.runLater(() -> sz2.setText("Applikációnkban"));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            Platform.runLater(() -> sz2.setText("In Our Application"));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
