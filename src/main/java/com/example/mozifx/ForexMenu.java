package com.example.mozifx;

import com.example.mozifx.Forex.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForexMenu {

    public MenuBar createForexMenu(Stage currentStage) {
        MenuBar menuBar = new MenuBar();
        Menu forexMenu = new Menu("Forex Menüpontok");

        MenuItem accountInfoMenuItem = new MenuItem("Számlainformációk");
        accountInfoMenuItem.setOnAction(e -> {
            ForexAccountInfoMenu accountInfoMenu = new ForexAccountInfoMenu();
            Stage accountInfoStage = new Stage();
            accountInfoStage.setTitle("Számlainformációk");
            VBox vbox = new VBox(accountInfoMenu.showAccountInfo());
            Scene scene = new Scene(vbox, 800, 600);
            accountInfoStage.setScene(scene);
            currentStage.close();
            accountInfoStage.show();
        });

        MenuItem currentRatesMenuItem = new MenuItem("Aktuális árak");
        currentRatesMenuItem.setOnAction(e -> {
            ForexCurrentRatesMenu currentRatesMenu = new ForexCurrentRatesMenu();
            Stage currentRatesStage = new Stage();
            currentRatesStage.setTitle("Aktuális árak");
            VBox vbox = new VBox(currentRatesMenu.showCurrentRates());
            Scene scene = new Scene(vbox, 800, 600);
            currentRatesStage.setScene(scene);
            currentStage.close();
            currentRatesStage.show();
        });

        MenuItem historicalRatesMenuItem = new MenuItem("Historikus árak");
        historicalRatesMenuItem.setOnAction(e -> {
            ForexHistoricalRatesMenu historicalRatesMenu = new ForexHistoricalRatesMenu();
            Stage historicalRatesStage = new Stage();
            historicalRatesStage.setTitle("Historikus árak");
            VBox vbox = new VBox(historicalRatesMenu.showHistoricalRates());
            Scene scene = new Scene(vbox, 800, 600);
            historicalRatesStage.setScene(scene);
            currentStage.close();
            historicalRatesStage.show();
        });

        MenuItem openPositionMenuItem = new MenuItem("Pozíció Nyitás");
        openPositionMenuItem.setOnAction(e -> {
            ForexPositionOpenMenu openPositionMenu = new ForexPositionOpenMenu();
            Stage openPositionStage = new Stage();
            openPositionStage.setTitle("Pozíció Nyitás");
            VBox vbox = new VBox(openPositionMenu.showPositionOpen());
            Scene scene = new Scene(vbox, 800, 600);
            openPositionStage.setScene(scene);
            currentStage.close();
            openPositionStage.show();
        });

        MenuItem closePositionMenuItem = new MenuItem("Pozíció Zárás");
        closePositionMenuItem.setOnAction(e -> {
            ForexPositionCloseMenu closePositionMenu = new ForexPositionCloseMenu();
            Stage closePositionStage = new Stage();
            closePositionStage.setTitle("Pozíció Zárás");
            VBox vbox = new VBox(closePositionMenu.showPositionClose());
            Scene scene = new Scene(vbox, 800, 600);
            closePositionStage.setScene(scene);
            currentStage.close();
            closePositionStage.show();
        });

        MenuItem openPositionsMenuItem = new MenuItem("Nyitott pozíciók");
        openPositionsMenuItem.setOnAction(e -> {
            ForexOpenPositionsMenu openPositionsMenu = new ForexOpenPositionsMenu();
            Stage openPositionsStage = new Stage();
            openPositionsStage.setTitle("Nyitott pozíciók");
            VBox vbox = new VBox(openPositionsMenu.showOpenPositions());
            Scene scene = new Scene(vbox, 800, 600);
            openPositionsStage.setScene(scene);
            currentStage.close();
            openPositionsStage.show();
        });

        forexMenu.getItems().addAll(accountInfoMenuItem, currentRatesMenuItem, historicalRatesMenuItem,
                openPositionMenuItem, closePositionMenuItem, openPositionsMenuItem);

        menuBar.getMenus().add(forexMenu);

        return menuBar;
    }
}
