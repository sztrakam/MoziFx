package com.example.mozifx.Thread;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Szal2 implements Runnable {
    @FXML
    private Label sz2;

    @FXML
    @Override
    public void run(){
        sz2=new Label();
        while(true){
            System.out.println(sz2.getText());
            sz2.setText("Világ");
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            sz2.setText("World");
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
