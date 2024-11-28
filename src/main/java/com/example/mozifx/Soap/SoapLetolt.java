package com.example.mozifx.Soap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SoapLetolt {
    private Button button;
    private Label label;

    public VBox showPage(){
        button = new Button();
        label = new Label();
        button.setText("Letöltés");
        button.setOnAction(event -> {
            letoltOne();
        });
        VBox vbox = new VBox(button, label);
        return vbox;
    }

    private void letoltOne(){
        Main.main(new String[]{"2022-02-02","2022-02-20","EUR"});
        try {
            File file=new File("MNB.txt");
            Scanner scanner = new Scanner(file);
            label.setText("letöltés sikeres");
            scanner.close();
        }catch(FileNotFoundException e){
            label.setText("letöltés sikertelen");
        }
    }

}
