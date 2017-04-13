package GUI;

import commands.*;
import deprecated.People;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by slavik on 01.04.17.
 */
public class MainWindow implements Runnable {
    private Stage mainWindow = new Stage();
    private TreeView<String> peopleTree;
    private RemoveObject removeObject = new RemoveObject();
    private AddObjects addObjects = new AddObjects();
    private ImportObjects importObjects = new ImportObjects();
    private InsertObject insertObject = new InsertObject();
    private static final Date currentDate = new Date();
    private final OtherMethods otherMethods = new OtherMethods();

    @Override
    public void run() {
//        new Thread(Storage.getInstanceOf()).start();
//        Thread.sleep(1000);
        Storage.getInstanceOf().run();

        peopleTree = new TreeView<>(getTreeForPeople());
        peopleTree.setEditable(true);
        peopleTree.setCellFactory(param -> new TreeTextFieldEditor());

        peopleTree.setPrefWidth(10000);
        peopleTree.setStyle("-fx-background-color: #000000");

        HBox listViewHBox = new HBox(getAnchorPaneForListView(), peopleTree);

        VBox root = new VBox(getMenuBar(), listViewHBox);


        mainWindow.setTitle("Work with Collection");
        mainWindow.setScene(new Scene(root, 428, 382));
        mainWindow.show();
    }

    public static TreeItem<String> getTreeForPeople() {
        TreeItem<String> family = new TreeItem<>("Family");
        family.setExpanded(true);

        if (Storage.getInstanceOf() != null) {
            TreeItem<String> nameItem;
            TreeItem<String> ageItem;
            for (People people : Storage.getInstanceOf().getFamily().values()) {
                nameItem = new TreeItem<>(people.getName());
                ageItem = new TreeItem<>(String.valueOf(people.getAge()));
                nameItem.getChildren().add(ageItem);
                family.getChildren().add(nameItem);
            }
        }

        return family;
    }

    private AnchorPane getAnchorPaneForListView() {
        ListView<StringBuilder> listView = getListView();
        listView.setPrefSize(160, 115);

        AnchorPane.setTopAnchor(listView, 0.0);
        AnchorPane.setBottomAnchor(listView, 0.0);
        AnchorPane.setLeftAnchor(listView, 0.0);
        AnchorPane anchorPane = new AnchorPane(listView);

        anchorPane.setPrefSize(115, 10000);

        return anchorPane;
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar(getFileMenu(), getProductMenu(), getAboutMenu(), getEditItemMenu());
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
        exitFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F4"));

        //ExitFileMenuListener
        exitFileMenuItem.setOnAction(event -> Platform.exit());


        MenuItem homeFileMenuItem = new MenuItem("Home");
        //HomeFileMenuItemListener
        homeFileMenuItem.setOnAction(event -> {
            mainWindow.close();
            new Thread(new RegisterWindow(new Stage())).run();
        });


        MenuItem infoFileMenuItem = new MenuItem("_Info");
        infoFileMenuItem.setMnemonicParsing(true);
        //InfoFileMenuItem
        infoFileMenuItem.setOnAction(event -> {
            Stage infoStage = new Stage();

            Class cl = Storage.getInstanceOf().getFamily().getClass();
            Label infoLabel = new Label("Имя коллекции - " + cl.getCanonicalName() +
                    "\nДата инициализации - " + currentDate +
                    "\nКоличество элемнтов - " + Storage.getInstanceOf().getFamily().size() +
                    "\nПакет - " + cl.getPackage() +
                    "\nИмя родительсокго класса - " + cl.getSuperclass().getName());

            infoLabel.setCenterShape(true);
            infoLabel.setAlignment(Pos.CENTER);
            infoLabel.setPadding(new Insets(20, 0, 0, 20));


            Button buttonOK = new Button("OK");
            HBox buttonOKHBox = new HBox(buttonOK);
            buttonOKHBox.setPadding(new Insets(10, 9, 0, 455));
            buttonOK.setOnAction(eventAction -> infoStage.close());

            buttonOK.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    infoStage.close();
                }
            });

            infoStage.setTitle("Info");
            infoStage.centerOnScreen();
            infoStage.setScene(new Scene(new VBox(infoLabel, buttonOKHBox), 500, 143));
            infoStage.toFront();
            infoStage.setMaximized(false);
            infoStage.setResizable(false);
            infoStage.show();
        });


        fileMenu.getItems().addAll(infoFileMenuItem, homeFileMenuItem, exitFileMenuItem);

        return fileMenu;
    }

    private Menu getProductMenu() {

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

    private Menu getAboutMenu() {
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

    private Menu getEditItemMenu() {
        Menu aboutMenu = new Menu("_Edit");
        aboutMenu.setMnemonicParsing(true);

        MenuItem saveMenuItem = new MenuItem("Save");
        aboutMenu.getItems().add(saveMenuItem);

        //EditSaveMenuListener
        saveMenuItem.setOnAction((ActionEvent event) -> otherMethods.save());

        MenuItem clearMenuItem = new MenuItem("Clear");
        aboutMenu.getItems().add(clearMenuItem);

        //EditClearMenuListener
        clearMenuItem.setOnAction((ActionEvent event) -> otherMethods.clear(peopleTree));

        MenuItem loadMenuItem = new MenuItem("Load");
        aboutMenu.getItems().add(loadMenuItem);

        //EditSaveMenuListener
        loadMenuItem.setOnAction((ActionEvent event) -> otherMethods.loadDefaultObjects(peopleTree));

        return aboutMenu;
    }

    private ListView<StringBuilder> getListView() {

        ObservableList<StringBuilder> commands = FXCollections.observableArrayList(
                new StringBuilder("Remove greater key"),
                new StringBuilder("Remove with key"),
                new StringBuilder("Remove greater"),
                new StringBuilder("Remove all"),
                new StringBuilder("Remove lower key"),
                new StringBuilder("Remove lower object"),
                new StringBuilder("Add if max"),
                new StringBuilder("Add if min"),
                new StringBuilder("Import all from file"),
                new StringBuilder("Insert new object"));

        ListView<StringBuilder> peopleListView = new ListView<>(commands);

        listViewActionListener(peopleListView);

        return peopleListView;
    }

    private void listViewActionListener(ListView<StringBuilder> peopleListView) {
        peopleListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends StringBuilder> observable, StringBuilder oldValue, StringBuilder newValue) -> {
            StringBuilder method = new StringBuilder(newValue);

            String className = String.valueOf(method.indexOf(" ") == -1 ?
                    method.replace(0, 1, method.substring(0, 1).toLowerCase()) :
                    new StringBuilder(method.substring(0, method.indexOf(" "))));

            method.replace(0, 1, method.substring(0, 1).toLowerCase());


            while (method.indexOf(" ") != -1) {
                int start = method.indexOf(" ") + 1;
                int finish = method.indexOf(" ") + 2;

                method.replace(start, finish, method.substring(start, finish).toUpperCase());
                method.deleteCharAt(start - 1);

            }

            switch (className) {
                case "Remove": {
                    try {
                        RemoveObject.class.getMethod(String.valueOf(method), peopleTree.getClass()).invoke(removeObject, peopleTree);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Method not found");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "Add": {
                    try {
                        AddObjects.class.getMethod(String.valueOf(method), peopleTree.getClass()).invoke(addObjects, peopleTree);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Method not found");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "Import": {
                    importObjects.importAllFromFile(peopleTree);
                    break;
                }
                case "Insert": {
                    insertObject.insertNewObject(peopleTree);
                    break;
                }
            }
        });
    }
}