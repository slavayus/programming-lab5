package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import old.school.Man;
import old.school.People;

import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slavik on 22.02.17.
 */

public class AddObjects {
    private static Gson gson = new GsonBuilder().create();
    private Stage dataStage = null;
    private TextField keyTextField = new TextField();


    /**
     * Команда: add_if_max.
     * Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void addIfMax(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField(false, peopleTree);
        } else {
            dataStage.toFront();
        }
    }

    /**
     * Команда add_if_min.
     * Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 2.0
     */
    public void addIfMin(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField(true, peopleTree);
        } else {
            dataStage.toFront();
        }
    }

    private void readDataFromTextField(boolean min, TreeView<Container> peopleTree) {
        dataStage = new Stage();


        Label keyLabel = new Label("Please, enter Object");
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        keyTextField.setPromptText("{name=\"name\";age=1}");


        Button buttonOK = new Button("OK");
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 9, 0, 255));

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addToCollection(min, peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> addToCollection(min, peopleTree, keyTextField));

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.setTitle("Object");
        dataStage.centerOnScreen();
        dataStage.setScene(new Scene(enterKeyVBox, 300, 90));
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    private void addToCollection(boolean min, TreeView<Container> peopleTree, TextField keyTextField) {
        String data = keyTextField.getText();
        try {
            People people = gson.fromJson(data, People.class);

            if (people == null || people.getAge() < 0 || !people.setName(people.getName())) {
                throw new NullPointerException();
            }

            Map<String, Man> newData = new LinkedHashMap<>();
            newData.put("0", people);

            ClientLoad clientLoad = new ClientLoad();
            if (min) {
                clientLoad.send(newData, "ADD_IF_MIN");
            } else {
                clientLoad.send(newData, "ADD_IF_MAX");
            }
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "\n" + messageFromClient.getMsg());

        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект,\nпроверьте корректность данных");
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nНе верно введены данные об объекте");
        } catch (SocketException e) {
//            e.printStackTrace();
        }

        peopleTree.setRoot(MainWindow.getTreeForPeople());
        dataStage.close();
        dataStage = null;
    }


}