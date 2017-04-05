package GUI;

import deprecated.People;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * Created by slavik on 01.04.17.
 */
public class MainWindow implements Runnable {
    private Stage mainWindow = new Stage();

    @Override
    public void run() {
        VBox root = new VBox(getMenuBar(),getAnchorPaneForListView());

        mainWindow.setTitle("Work with Collection");
        mainWindow.setScene(new Scene(root,428,382));
        mainWindow.show();
    }

    private AnchorPane getAnchorPaneForListView(){
        ListView<String> listView = getListView();
        listView.setPrefSize(150,100);

        AnchorPane.setTopAnchor(listView,0.0);
        AnchorPane.setBottomAnchor(listView,0.0);
        AnchorPane.setLeftAnchor(listView,0.0);
        AnchorPane anchorPane =new AnchorPane(listView);

        anchorPane.setPrefSize(100,10000);

        return anchorPane;
    }

    private MenuBar getMenuBar(){
        MenuBar menuBar = new MenuBar(getFileMenu(),getProductMenu(),getAboutMenu());
        menuBar.setUseSystemMenuBar(true);
        return menuBar;
    }

    private void OpenURLException() {
        Stage errorStage = new Stage();
        Label errorLabel = new Label("Cannot open this URL");
        errorStage.setScene(new Scene(errorLabel, 150, 100));
        errorStage.show();
    }

    private Menu getFileMenu() {
        Menu fileMenu = new Menu("_File");
        fileMenu.setMnemonicParsing(true);
        MenuItem exitFileMenuItem = new MenuItem("Exit");

        //ExitFileMenuListener
        exitFileMenuItem.setOnAction(event -> Platform.exit());

        exitFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F4"));
        fileMenu.getItems().add(exitFileMenuItem);
        MenuItem homeFileMenuItem = new MenuItem("Home");

        //HomeFileMenuItemListener
        homeFileMenuItem.setOnAction(event -> {
            mainWindow.close();
            new Thread(new RegisterWindow(new Stage())).run();
        });

        fileMenu.getItems().add(homeFileMenuItem);

        return fileMenu;
    }

    private Menu getProductMenu(){

        Menu productsMenu = new Menu("_Products");
        productsMenu.setMnemonicParsing(true);

        MenuItem gitRepositoryProductsMenu = new MenuItem("Git Repository");
        productsMenu.getItems().add(gitRepositoryProductsMenu);

        //gitRepositoryProductsMenuListener
        gitRepositoryProductsMenu.setOnAction((ActionEvent event) -> {
            try {
                new ProcessBuilder("x-www-browser", "https://github.com/slavayus").start();
            } catch (Exception e) {
                OpenURLException();
            }
        });

        return productsMenu;
    }

    private Menu getAboutMenu(){
        Menu aboutMenu = new Menu("_About");
        aboutMenu.setMnemonicParsing(true);
        MenuItem ourVKAboutMenuItem = new MenuItem("Our vk");
        aboutMenu.getItems().add(ourVKAboutMenuItem);

        //OurVKAboutMenuListener
        ourVKAboutMenuItem.setOnAction((ActionEvent event) -> {
            try {
                new ProcessBuilder("x-www-browser", "https://vk.com/slava_yus").start();
            } catch (IOException e) {
                OpenURLException();
            }
        });

        return aboutMenu;
    }

    private ListView<String> getListView() {
        Storage.setInstanceOf(new Storage());
        Storage.getInstanceOf().loadDefaultObjects();

        ListView<String> peopleListView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        for(People people: Storage.getInstanceOf().getFamily().values()){
            items.add(people.getName());
        }

        peopleListView.setItems(items);

        return peopleListView;
    }
}
