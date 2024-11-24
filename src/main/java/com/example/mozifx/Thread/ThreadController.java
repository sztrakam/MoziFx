package com.example.mozifx.Thread;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ThreadController {
    @FXML
    public Label welcomeText;
    @FXML
    public Label sz1;
    @FXML
    public Label sz2;



    @FXML
    protected void onHelloButtonClick() {
        sz1.setText("szál1");
        sz2.setText("szál2");
        Szal1 szál1 = new Szal1();
        Thread thread1 = new Thread(szál1);
        thread1.start();
        Szal2 szál2 = new Szal2();
        Thread thread2 = new Thread(szál2);
        thread2.start();
    }
}