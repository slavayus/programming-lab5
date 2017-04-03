package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by slavik on 01.04.17.
 */
public class MainWindow implements Runnable{

    @Override
    public void run() {

        Stage mainWindow = new Stage();
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_File");
        fileMenu.setMnemonicParsing(true);
        MenuItem exitFileMenuItem = new MenuItem("Exit");
        exitFileMenuItem.setOnAction(event -> Platform.exit());
        exitFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F4"));
        fileMenu.getItems().add(exitFileMenuItem);
        MenuItem homeFileMenuItem = new MenuItem("Home");
//        homeFileMenuItem.setOnAction(event -> {
//            primaryStage.close();
//            Thread.currentThread().run();
//        });
        fileMenu.getItems().add(homeFileMenuItem);

        Menu productsMenu = new Menu("_Products");
        productsMenu.setMnemonicParsing(true);

        MenuItem gitRepositoryProductsMenu = new MenuItem("Git Repository");
        productsMenu.getItems().add(gitRepositoryProductsMenu);
        gitRepositoryProductsMenu.setOnAction((ActionEvent event) -> {
            try {
                new ProcessBuilder("x-www-browser", "https://github.com/slavayus").start();
            } catch (Exception e) {
                OpenURLException();
            }
        });


        Menu aboutMenu = new Menu("_About");
        aboutMenu.setMnemonicParsing(true);
        MenuItem ourVKAboutMenuItem = new MenuItem("Our vk");
        aboutMenu.getItems().add(ourVKAboutMenuItem);


        ourVKAboutMenuItem.setOnAction((ActionEvent event) -> {
            try {
                new ProcessBuilder("x-www-browser", "https://vk.com/slava_yus").start();
            } catch (IOException e) {
                OpenURLException();
            }
        });


        menuBar.getMenus().addAll(fileMenu, productsMenu, aboutMenu);
        menuBar.setUseSystemMenuBar(true);

        VBox root = new VBox();
        root.getChildren().addAll(menuBar);

        mainWindow.setTitle("Work with Collection");
        Scene scene = new Scene(root, 400, 350);
        mainWindow.setScene(scene);
        mainWindow.show();
    }


    private void OpenURLException() {
        Stage errorStage = new Stage();
        Label errorLabel = new Label("Cannot open this URL");
        errorStage.setScene(new Scene(errorLabel, 150, 100));
        errorStage.show();
    }

    public void getRegisterWindow(Stage primaryStage) {
    }
}
