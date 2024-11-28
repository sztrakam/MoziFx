package com.example.mozifx.Thread;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Szal1 implements Runnable {
    private Label sz1;

    public Szal1(Label sz1) {
        this.sz1 = sz1;
    }

    @Override
    public void run() {
        while (true) {
            // UI frissítése JavaFX szálon
            Platform.runLater(() -> sz1.setText("Üdvözlöm"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            Platform.runLater(() -> sz1.setText("Welcome"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
