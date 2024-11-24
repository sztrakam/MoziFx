package com.example.mozifx.Thread;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Szal1 implements Runnable {

    @FXML
    private Label sz1;

    @FXML
    @Override
    public void run(){
        sz1=new Label();
        while(true){
            System.out.println(sz1.getText());
            sz1.setText("Szia");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            sz1.setText("Hello");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
