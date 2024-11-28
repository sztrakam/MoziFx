package com.example.mozifx.Soap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SoapLetoltForm {
    private TextField currency;
    private TextField begYear;
    private TextField endYear;
    private Label label;

    public VBox showPage(){
        VBox form = createForm();

        return new VBox(form);
    }
    public VBox createForm(){
        GridPane formGrid = new GridPane();
        formGrid.add(new Label("Letöltés szűrővel:"), 0, 0);
        currency = new TextField();
        begYear = new TextField();
        endYear = new TextField();
        label = new Label();
        begYear.setPromptText("yyyy-MM-dd");
        formGrid.add(new Label("Kezdő időpont"),0,1);
        formGrid.add(begYear, 1, 1);
        endYear.setPromptText("yyyy-MM-dd");
        formGrid.add(new Label("Végső időpont"),0,2);
        formGrid.add(endYear, 1, 2);
        currency.setPromptText("Pl: EUR");
        formGrid.add(new Label("Keresett valuta"),0,3);
        formGrid.add(currency, 1, 3);
        Button okButton = new Button("Ok");
        okButton.setOnAction(event -> {filterData();});
        formGrid.add(okButton, 0, 4);
        formGrid.add(label, 1, 4);
        return new VBox(formGrid);
    }

    private void filterData(){
        String currencyFilter = currency.getText();
        String begYearFilter = begYear.getText();
        String endYearFilter = endYear.getText();
        Main.main(new String[]{begYearFilter, endYearFilter, currencyFilter});
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
