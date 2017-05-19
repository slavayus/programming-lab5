package GUI;

import commands.*;
import old.school.Man;
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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by slavik on 01.04.17.
 */
public class MainWindow extends TreeCell<Container> {
    private Stage mainWindow = new Stage();
    private static TreeView<Container> peopleTree;
    private RemoveObject removeObject = new RemoveObject();
    private AddObjects addObjects = new AddObjects();
    private ImportObjects importObjects = new ImportObjects();
    private InsertObject insertObject = new InsertObject();
    private static final Date currentDate = new Date();
    private final OtherMethods otherMethods = new OtherMethods();
    private Version version;

    public MainWindow(Version version) {
        this.version = version;
    }

    public void showWindow() {
        Storage.getInstanceOf().readFromDB();

        peopleTree = new TreeView<>(getTreeForPeople());
        if (version == Version.FULL) {
            peopleTree.setEditable(true);
        }


        peopleTree.setCellFactory(TreeTextFieldEditor::new);

        peopleTree.setPrefWidth(10000);
        peopleTree.setStyle("-fx-background-color: #000000");


        ResourceBundle defaultBundle = ResourceBundle.getBundle("bundles.Locale");

        HBox listViewHBox = new HBox(getAnchorPaneForListView(defaultBundle), peopleTree);

        VBox root = new VBox(getMenuBar(defaultBundle), listViewHBox);


        mainWindow.setTitle(defaultBundle.getString("system.name"));
        mainWindow.setScene(new Scene(root, 428, 382));
        mainWindow.show();
    }


    public static TreeView<Container> getPeopleTree() {
        return peopleTree;
    }

    public static TreeItem<Container> getTreeForPeople() {
        TreeItem<Container> family = new TreeItem<>(new Container(null, "Family", ContainerType.COLLECTION));
        family.setExpanded(true);

        if (Storage.getInstanceOf() != null) {
            TreeItem<Container> nameItem;
            TreeItem<Container> ageItem;
            for (Map.Entry<String, Man> entry : Storage.getInstanceOf().getFamily().entrySet()) {
                nameItem = new TreeItem<>(new Container(entry.getKey(), entry.getValue().getName(), ContainerType.ELEMENT));
                ageItem = new TreeItem<>(new Container(entry.getKey(), String.valueOf(entry.getValue().getAge()), ContainerType.AGE));
                nameItem.getChildren().add(ageItem);
                family.getChildren().add(nameItem);
            }
        }

        return family;
    }

    private AnchorPane getAnchorPaneForListView(ResourceBundle bundle) {
        ListView<StringBuilder> listView = getListView(bundle);
        listView.setPrefSize(160, 115);

        AnchorPane.setTopAnchor(listView, 0.0);
        AnchorPane.setBottomAnchor(listView, 0.0);
        AnchorPane.setLeftAnchor(listView, 0.0);
        AnchorPane anchorPane = new AnchorPane(listView);

        anchorPane.setPrefSize(115, 10000);

        return anchorPane;
    }

    private MenuBar getMenuBar(ResourceBundle bundle) {
        MenuBar menuBar = new MenuBar(
                getFileMenu(bundle),
                getProductMenu(bundle),
                getAboutMenu(bundle),
                getEditItemMenu(bundle),
                languageSelector(bundle));

        menuBar.setUseSystemMenuBar(true);
        return menuBar;
    }

    private void OpenURLException() {
        Stage errorStage = new Stage();
        Label errorLabel = new Label("Cannot open this URL");
        errorStage.setScene(new Scene(errorLabel, 150, 100));
        errorStage.show();
    }

    private Menu getFileMenu(ResourceBundle bundle) {
        Menu fileMenu = new Menu("_" + bundle.getString("menu.file"));
        fileMenu.setMnemonicParsing(true);

        MenuItem exitFileMenuItem = new MenuItem(bundle.getString("menu.file.exit"));
        exitFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F4"));

        //ExitFileMenuListener
        exitFileMenuItem.setOnAction(event -> Platform.exit());


        MenuItem homeFileMenuItem = null;
        homeFileMenuItem = new MenuItem(bundle.getString("menu.file.home"));

        //HomeFileMenuItemListener
        homeFileMenuItem.setOnAction(event -> {
            mainWindow.close();
            new LoginWindow(new Stage()).run();
        });


        MenuItem infoFileMenuItem = new MenuItem(bundle.getString("menu.file.info"));
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

    private Menu getProductMenu(ResourceBundle bundle) {

        Menu productsMenu = new Menu("_" + bundle.getString("menu.product"));
        productsMenu.setMnemonicParsing(true);

        MenuItem gitRepositoryProductsMenu = new MenuItem(bundle.getString("menu.product.git"));
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

    private Menu getAboutMenu(ResourceBundle bundle) {
        Menu aboutMenu = new Menu("_" + bundle.getString("menu.about"));
        aboutMenu.setMnemonicParsing(true);
        MenuItem ourVKAboutMenuItem = new MenuItem(bundle.getString("menu.about.vk"));
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

    private Menu getEditItemMenu(ResourceBundle bundle) {
        Menu aboutMenu = new Menu("_" + bundle.getString("menu.edit"));
        aboutMenu.setMnemonicParsing(true);

        MenuItem saveMenuItem = new MenuItem(bundle.getString("menu.edit.save"));
        aboutMenu.getItems().add(saveMenuItem);

        //EditSaveMenuListener
        saveMenuItem.setOnAction((ActionEvent event) -> otherMethods.save());

        MenuItem clearMenuItem = new MenuItem(bundle.getString("menu.edit.clear"));
        aboutMenu.getItems().add(clearMenuItem);

        //EditClearMenuListener
        clearMenuItem.setOnAction((ActionEvent event) -> otherMethods.clear(peopleTree));

        MenuItem loadMenuItem = new MenuItem(bundle.getString("menu.edit.load"));
        aboutMenu.getItems().add(loadMenuItem);

        //EditSaveMenuListener
        loadMenuItem.setOnAction((ActionEvent event) -> otherMethods.loadDefaultObjects(peopleTree));

        return aboutMenu;
    }

    private Menu languageSelector(ResourceBundle bundle) {
        Menu languageMenu = new Menu("_" + bundle.getString("menu.language"));
        languageMenu.setMnemonicParsing(true);


        //enMenuItem
        MenuItem enMenuItem = new MenuItem(bundle.getString("menu.language.en"));
        languageMenu.getItems().add(enMenuItem);
        enMenuItem.setOnAction((ActionEvent event) -> {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.Locale", new Locale("en", "EN"));

            HBox listViewHBox = new HBox(getAnchorPaneForListView(resourceBundle), peopleTree);

            VBox root = new VBox(getMenuBar(resourceBundle), listViewHBox);

            mainWindow.setTitle(resourceBundle.getString("system.name"));
            mainWindow.setScene(new Scene(root, 428, 382));
        });


        //ruMenuItem
        MenuItem ruMenuItem = new MenuItem(bundle.getString("menu.language.ru"));
        languageMenu.getItems().add(ruMenuItem);
        ruMenuItem.setOnAction((ActionEvent event) -> {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.Locale", new Locale("ru", "RU"));

            HBox listViewHBox = new HBox(getAnchorPaneForListView(resourceBundle), peopleTree);


            VBox root = new VBox(getMenuBar(resourceBundle), listViewHBox);

            mainWindow.setTitle(resourceBundle.getString("system.name"));
            mainWindow.setScene(new Scene(root, 428, 382));
        });


        //mkMenuItem
        MenuItem mkMenuItem = new MenuItem(bundle.getString("menu.language.mk"));
        languageMenu.getItems().add(mkMenuItem);
        mkMenuItem.setOnAction((ActionEvent event) -> {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.Locale", new Locale("mk", "MK"));

            HBox listViewHBox = new HBox(getAnchorPaneForListView(resourceBundle), peopleTree);

            VBox root = new VBox(getMenuBar(resourceBundle), listViewHBox);

            mainWindow.setTitle(resourceBundle.getString("system.name"));
            mainWindow.setScene(new Scene(root, 428, 382));
        });


        //bgMenuItem
        MenuItem bgMenuItem = new MenuItem(bundle.getString("menu.language.bg"));
        languageMenu.getItems().add(bgMenuItem);
        bgMenuItem.setOnAction((ActionEvent event) -> {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.Locale", new Locale("bg", "BG"));

            HBox listViewHBox = new HBox(getAnchorPaneForListView(resourceBundle), peopleTree);

            VBox root = new VBox(getMenuBar(resourceBundle), listViewHBox);

            mainWindow.setTitle(resourceBundle.getString("system.name"));
            mainWindow.setScene(new Scene(root, 428, 382));
        });


        return languageMenu;
    }

    private ListView<StringBuilder> getListView(ResourceBundle bundle) {

        Map<String, String> commandsMap = initializeContainer(bundle);

        ObservableList<StringBuilder> commands = FXCollections.observableArrayList(
                new StringBuilder(bundle.getString("command.remove.greater.key")),
                new StringBuilder(bundle.getString("command.remove.with.key")),
                new StringBuilder(bundle.getString("command.remove.greater")),
                new StringBuilder(bundle.getString("command.remove.all")),
                new StringBuilder(bundle.getString("command.remove.lower.key")),
                new StringBuilder(bundle.getString("command.add.if.min")),
                new StringBuilder(bundle.getString("command.remove.lower.object")),
                new StringBuilder(bundle.getString("command.import.all.from.file")),
                new StringBuilder(bundle.getString("command.add.if.max")),
                new StringBuilder(bundle.getString("command.insert.new.object")));

        ListView<StringBuilder> peopleListView = new ListView<>(commands);


        listViewActionListener(peopleListView, commandsMap);


        return peopleListView;
    }

    private Map<String, String> initializeContainer(ResourceBundle bundle) {
        ResourceBundle enBundle = ResourceBundle.getBundle("bundles.Locale", new Locale("en", "EN"));

        Map<String, String> commands = new HashMap<>();

        commands.put((bundle.getString("command.remove.greater.key")), enBundle.getString("command.remove.greater.key"));
        commands.put((bundle.getString("command.remove.with.key")), enBundle.getString("command.remove.with.key"));
        commands.put((bundle.getString("command.remove.greater")), enBundle.getString("command.remove.greater"));
        commands.put((bundle.getString("command.remove.all")), enBundle.getString("command.remove.all"));
        commands.put((bundle.getString("command.remove.lower.key")), enBundle.getString("command.remove.lower.key"));
        commands.put(bundle.getString("command.remove.lower.object"), enBundle.getString("command.remove.lower.object"));
        commands.put(bundle.getString("command.add.if.max"), enBundle.getString("command.add.if.max"));
        commands.put(bundle.getString("command.add.if.min"), enBundle.getString("command.add.if.min"));
        commands.put(bundle.getString("command.import.all.from.file"), enBundle.getString("command.import.all.from.file"));
        commands.put(bundle.getString("command.insert.new.object"), enBundle.getString("command.insert.new.object"));


        return commands;
    }

    private void listViewActionListener(ListView<StringBuilder> peopleListView, Map<String, String> commandsMap) {

        peopleListView.setOnMouseClicked(event -> {
            if (!peopleListView.getSelectionModel().isEmpty()) {
                peopleListView.getSelectionModel().clearSelection();
            }
        });

        peopleListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends StringBuilder> observable, StringBuilder oldValue, StringBuilder newValue) -> {
            StringBuilder method = null;
            try {
                method = new StringBuilder(commandsMap.get(newValue.toString()));
            } catch (Exception e) {
                return;
            }

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