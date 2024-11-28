package com.example.mozifx.Soap;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SoapGrafikon {
    private LineChart<Number, Number> lineChart;

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private List<String> date;
    private List<Float> value;

    public VBox showPage(){
        GridPane formGrid = new GridPane();
        formGrid.add(new Label("Letöltés szűrővel:"), 0, 0);
        TextField currency = new TextField();
        TextField begYear = new TextField();
        TextField endYear = new TextField();
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
        okButton.setOnAction(event -> {felDolgoz();});
        formGrid.add(okButton, 0, 4);
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        VBox vbox = new VBox(formGrid, lineChart);
        return vbox;
    }
    public void felDolgoz(){
        date = new ArrayList<>();
        value = new ArrayList<>();
        Dictionary<String, Float> data=new Hashtable<String, Float>();
        try{
            File myObj = new File("MNB.txt");
            Scanner ms = new Scanner(myObj);
            ms.nextLine();
            String line=null;
            float value=0;
            String date =null;
            int i=0;
            while(ms.hasNext()){
                if (line!=null){
                    if (i%3==0){
                        date=line.split("=")[1];
                    }
                    else {
                        try {
                            value = Float.parseFloat(line.split("=")[1]);
                        }catch(NumberFormatException e){

                        }
                    }
                    if (value!=0&&date!=null){
                        data.put(date, value);
                    }
                }
                i++;
                line=ms.nextLine();
                line=line.replace(",","");
                line=line.replace("]","");
                line=line.replace("[","");
            }
            ms.close();
        }catch(FileNotFoundException e) {
            System.err.println(e);
        }
        Enumeration<String> keys=data.keys();
        while(keys.hasMoreElements()){
            date.add(keys.nextElement());
        }
        for(String hely : date){
            value.add(data.get(hely));
        }
        Enumeration<String> keys2=data.keys();
        XYChart.Series series = new XYChart.Series();
        series.setName("data");
        for(int i = 0; i<date.size(); i++) {
            series.getData().add(new XYChart.Data(i+1, value.get(i)));
        }
        lineChart.getData().add(series);
    }
}
