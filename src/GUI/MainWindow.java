package GUI;

import commands.*;
import old.school.People;
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
import java.util.Date;
import java.util.Map;

/**
 * Created by slavik on 01.04.17.
 */
public class MainWindow {
    private Stage mainWindow = new Stage();
    private TreeView<String> peopleTree;
    private RemoveObject removeObject = new RemoveObject();
    private AddObjects addObjects = new AddObjects();


    public void showWindow() {
        Thread loadThread = new Thread(Storage.getInstanceOf());
        loadThread.start();
        try {
            loadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        peopleTree = new TreeView<>(getTreeForPeople());


        peopleTree.setPrefWidth(10000);
        peopleTree.setStyle("-fx-background-color: #000000");

        HBox listViewHBox = new HBox(getAnchorPaneForListView(), peopleTree);

        VBox root = new VBox(listViewHBox);


        mainWindow.setTitle("Work with Collection");
        mainWindow.setScene(new Scene(root, 428, 382));
        mainWindow.show();
    }

    public static TreeItem<String> getTreeForPeople() {
        TreeItem<String> family = new TreeItem<>( "Family");
        family.setExpanded(true);

        if (Storage.getInstanceOf() != null) {
            TreeItem<String> nameItem;
            TreeItem<String> ageItem;
            for (Map.Entry<String, People> entry : Storage.getInstanceOf().getFamily().entrySet()) {
                nameItem = new TreeItem<>(entry.getValue().getName());
                ageItem = new TreeItem<>(String.valueOf(entry.getValue().getAge()));
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

    private ListView<StringBuilder> getListView() {

        ObservableList<StringBuilder> commands = FXCollections.observableArrayList(
                new StringBuilder("Remove greater key"),
                new StringBuilder("Remove with key"),
                new StringBuilder("Remove lower key"),
                new StringBuilder("Add if max"),
                new StringBuilder("Add if min"));

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
            }
        });
    }
}