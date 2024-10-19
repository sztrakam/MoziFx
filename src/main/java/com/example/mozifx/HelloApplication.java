package com.example.mozifx;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mozi CRUD Alkalmazás");

        VBox root = new VBox();
        root.getChildren().add(createMenu(primaryStage));

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
            Scene scene = new Scene(vbox, 400, 300);
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
            Scene scene = new Scene(vbox, 400, 300);
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
            Scene scene = new Scene(vbox, 400, 300);
            deleteStage.setScene(scene);
            currentStage.close();
            deleteStage.show();
        });

        adatbazisMenu.getItems().addAll(olvasMenuItem, olvas2MenuItem, irMenuItem, modosítMenuItem, torolMenuItem);
        menuBar.getMenus().add(adatbazisMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
