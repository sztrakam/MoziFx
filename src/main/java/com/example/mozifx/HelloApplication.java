package com.example.mozifx;

import com.example.mozifx.Soap.SoapGrafikon;
import com.example.mozifx.Soap.SoapLetolt;
import com.example.mozifx.Soap.SoapLetoltForm;
import com.example.mozifx.Thread.Szal1;
import com.example.mozifx.Thread.Szal2;
import com.example.mozifx.Forex.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mozi CRUD Alkalmazás");
        Label szal1=new Label();
        szal1.setFont(new Font("Arial", 40));
        szal1.setPadding(new Insets(100,10,10,300));
        Label szal2=new Label();
        szal2.setFont(new Font("Arial", 40));
        szal2.setPadding(new Insets(10,10,10,250));
        VBox root = new VBox();
        root.getChildren().addAll(createMenu(primaryStage),szal1,szal2);

        Szal1 sz1=new Szal1(szal1);
        Thread thread1 = new Thread(sz1);
        thread1.start();

        Szal2 sz2=new Szal2(szal2);
        Thread thread2 = new Thread(sz2);
        thread2.start();

        String imagePath = "file:src/main/resources/images/könyvels.png";
        setBackgroundImage(root, imagePath);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private MenuBar createMenu(Stage currentStage) {
        MenuBar menuBar = new MenuBar();
        Menu adatbazisMenu = new Menu("Menüpontok");

        MenuItem olvasMenuItem = new MenuItem("Olvas");
        olvasMenuItem.setOnAction(e -> {
            ReadMenu olvas = new ReadMenu();
            Stage olvasStage = new Stage();
            olvasStage.setTitle("Olvasás menü");
            VBox vbox = new VBox(createMenu(olvasStage), olvas.showData());
            Scene scene = new Scene(vbox, 800, 600);
            olvasStage.setScene(scene);
            currentStage.close();
            olvasStage.show();
        });

        MenuItem olvas2MenuItem = new MenuItem("Olvas2");
        olvas2MenuItem.setOnAction(e -> {
            ReadMenuWithFilter olvas2 = new ReadMenuWithFilter();
            Stage olvas2Stage = new Stage();
            olvas2Stage.setTitle("Olvasás 2 menü");
            VBox vbox = new VBox(createMenu(olvas2Stage), olvas2.showData());
            Scene scene = new Scene(vbox, 800, 600);
            olvas2Stage.setScene(scene);
            currentStage.close();
            olvas2Stage.show();
        });


        MenuItem irMenuItem = new MenuItem("Ír");
        irMenuItem.setOnAction(e -> {
            WriteMenu writeMenu = new WriteMenu();
            Stage writeStage = new Stage();
            writeStage.setTitle("Rekord Hozzáadás");
            VBox vbox = new VBox(createMenu(writeStage), writeMenu.showWriteMenu());
            Scene scene = new Scene(vbox, 800, 600);
            writeStage.setScene(scene);
            currentStage.close();
            writeStage.show();
        });


        MenuItem modosítMenuItem = new MenuItem("Módosít");
        modosítMenuItem.setOnAction(e -> {
            ModifyMenu modifyMenu = new ModifyMenu();
            Stage modifyStage = new Stage();
            modifyStage.setTitle("Rekord Módosítás");
            VBox vbox = new VBox(createMenu(modifyStage), modifyMenu.showModifyMenu());
            Scene scene = new Scene(vbox, 800, 600);
            modifyStage.setScene(scene);
            currentStage.close();
            modifyStage.show();
        });


        MenuItem torolMenuItem = new MenuItem("Töröl");
        torolMenuItem.setOnAction(e -> {
            DeleteMenu deleteMenu = new DeleteMenu();
            Stage deleteStage = new Stage();
            deleteStage.setTitle("Rekord Törlés");
            VBox vbox = new VBox(createMenu(deleteStage), deleteMenu.showDeleteMenu());
            Scene scene = new Scene(vbox, 800, 600);
            deleteStage.setScene(scene);
            currentStage.close();
            deleteStage.show();
        });


        Menu forexMenu = new Menu("Forex");

        MenuItem szamlaMenuItem = new MenuItem("Számlainformációk");
        szamlaMenuItem.setOnAction(e -> {
            ForexAccountInfoMenu accountInfoMenu = new ForexAccountInfoMenu();
            Stage accountInfoStage = new Stage();
            accountInfoStage.setTitle("Számlainformációk");
            VBox vbox = new VBox(createMenu(accountInfoStage), accountInfoMenu.showAccountInfo());
            Scene scene = new Scene(vbox, 800, 600);
            accountInfoStage.setScene(scene);
            currentStage.close();
            accountInfoStage.show();
        });

        MenuItem aktualisArakMenuItem = new MenuItem("Aktuális árak");
        aktualisArakMenuItem.setOnAction(e -> {
            ForexCurrentRatesMenu currentRatesMenu = new ForexCurrentRatesMenu();
            Stage currentRatesStage = new Stage();
            currentRatesStage.setTitle("Aktuális árak");
            VBox vbox = new VBox(createMenu(currentRatesStage), currentRatesMenu.showCurrentRates());
            Scene scene = new Scene(vbox, 800, 600);
            currentRatesStage.setScene(scene);
            currentStage.close();
            currentRatesStage.show();
        });

        MenuItem historikusArakMenuItem = new MenuItem("Historikus árak");
        historikusArakMenuItem.setOnAction(e -> {
            ForexHistoricalRatesMenu historicalRatesMenu = new ForexHistoricalRatesMenu();
            Stage historicalRatesStage = new Stage();
            historicalRatesStage.setTitle("Historikus árak");
            VBox vbox = new VBox(createMenu(historicalRatesStage), historicalRatesMenu.showHistoricalRates());
            Scene scene = new Scene(vbox, 800, 600);
            historicalRatesStage.setScene(scene);
            currentStage.close();
            historicalRatesStage.show();
        });

        MenuItem pozicioNyitasMenuItem = new MenuItem("Pozíció nyitás");
        pozicioNyitasMenuItem.setOnAction(e -> {
            ForexPositionOpenMenu positionOpenMenu = new ForexPositionOpenMenu();
            Stage positionOpenStage = new Stage();
            positionOpenStage.setTitle("Pozíció nyitás");
            VBox vbox = new VBox(createMenu(positionOpenStage), positionOpenMenu.showPositionOpen());
            Scene scene = new Scene(vbox, 800, 600);
            positionOpenStage.setScene(scene);
            currentStage.close();
            positionOpenStage.show();
        });

        MenuItem pozicioZarasMenuItem = new MenuItem("Pozíció zárás");
        pozicioZarasMenuItem.setOnAction(e -> {
            ForexPositionCloseMenu positionCloseMenu = new ForexPositionCloseMenu();
            Stage positionCloseStage = new Stage();
            positionCloseStage.setTitle("Pozíció zárás");
            VBox vbox = new VBox(createMenu(positionCloseStage), positionCloseMenu.showPositionClose());
            Scene scene = new Scene(vbox, 800, 600);
            positionCloseStage.setScene(scene);
            currentStage.close();
            positionCloseStage.show();
        });

        MenuItem nyitottPoziciokMenuItem = new MenuItem("Nyitott pozíciók");
        nyitottPoziciokMenuItem.setOnAction(e -> {
            ForexOpenPositionsMenu openPositionsMenu = new ForexOpenPositionsMenu();
            Stage openPositionsStage = new Stage();
            openPositionsStage.setTitle("Nyitott pozíciók");
            VBox vbox = new VBox(createMenu(openPositionsStage), openPositionsMenu.showOpenPositions());
            Scene scene = new Scene(vbox, 800, 600);
            openPositionsStage.setScene(scene);
            currentStage.close();
            openPositionsStage.show();
        });

        Menu soapMenu = new Menu("Soap");

        MenuItem letolt1MenuItem= new MenuItem("Letöltés1");
        letolt1MenuItem.setOnAction(e -> {
            SoapLetolt letolt1 = new SoapLetolt();
            Stage letoltStage1 = new Stage();
            letoltStage1.setTitle("Letöltés");
            VBox vbox = new VBox(createMenu(letoltStage1), letolt1.showPage());
            Scene scene = new Scene(vbox, 800,600);
            letoltStage1.setScene(scene);
            currentStage.close();
            letoltStage1.show();
        });
        MenuItem letolt2MenuItem=new MenuItem("Letöltés2");
        letolt2MenuItem.setOnAction(e -> {
            SoapLetoltForm letolt2 = new SoapLetoltForm();
            Stage letoltStage2 = new Stage();
            letoltStage2.setTitle("Letöltés 2 menü");
            VBox vbox = new VBox(createMenu(letoltStage2), letolt2.showPage());
            Scene scene = new Scene(vbox, 800, 600);
            letoltStage2.setScene(scene);
            currentStage.close();
            letoltStage2.show();
        });
        MenuItem grafikonMenuItem = new MenuItem("Grafikon");
        grafikonMenuItem.setOnAction(e -> {
            SoapGrafikon grafikon = new SoapGrafikon();
            Stage grafikonStage = new Stage();
            grafikonStage.setTitle("Grafikon");
            VBox vbox = new VBox(createMenu(grafikonStage), grafikon.showPage());
            Scene scene = new Scene(vbox, 800, 600);
            grafikonStage.setScene(scene);
            currentStage.close();
            grafikonStage.show();
        });

        soapMenu.getItems().addAll(letolt1MenuItem,letolt2MenuItem,grafikonMenuItem);
        forexMenu.getItems().addAll(szamlaMenuItem, aktualisArakMenuItem, historikusArakMenuItem,
                pozicioNyitasMenuItem, pozicioZarasMenuItem, nyitottPoziciokMenuItem);

        adatbazisMenu.getItems().addAll(olvasMenuItem, olvas2MenuItem, irMenuItem, modosítMenuItem, torolMenuItem);
        menuBar.getMenus().add(adatbazisMenu);
        menuBar.getMenus().add(forexMenu);
        menuBar.getMenus().add(soapMenu);

        return menuBar;
    }

    private void setBackgroundImage(VBox vbox, String imagePath) {
        Image image = new Image(imagePath);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,false));
        vbox.setBackground(new Background(backgroundImage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
