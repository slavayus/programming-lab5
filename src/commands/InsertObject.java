package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import old.school.Man;
import old.school.People;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slavik on 08.04.17.
 */
public class InsertObject {
    private static Gson gson = new GsonBuilder().create();
    private Stage dataStage = null;

    /**
     * Команда insert.
     * Добавляет новый элемент с заданным ключом.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void insertNewObject(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readKeyAndObject(peopleTree);
        } else {
            dataStage.toFront();
        }
    }


    private void readKeyAndObject(TreeView<Container> peopleTree) {
        dataStage = new Stage();


        Label keyLabel = new Label("Please, enter Data");
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));


        TextField keyTextField = new TextField();
        keyTextField.setPromptText("Key");

        TextField objectTextField = new TextField();
        objectTextField.setPromptText("{name=\"name\";age=1}");


        Button buttonOK = new Button("OK");
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 9, 0, 255));


        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                insertToCollection(peopleTree, objectTextField, keyTextField);
            }
        });


        buttonOK.setOnMouseClicked(event -> insertToCollection(peopleTree, objectTextField, keyTextField));

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, objectTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.setTitle("Object");
        dataStage.centerOnScreen();
        dataStage.setScene(new Scene(enterKeyVBox, 300, 120));
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    private void insertToCollection(TreeView<Container> peopleTree, TextField objectTextField, TextField keyTextField) {
        String object = objectTextField.getText();
        String key = keyTextField.getText();

        try {
            People people = gson.fromJson(object, People.class);
            if (people == null || people.getAge() < 0 || !people.setName(people.getName())) {
                throw new NullPointerException();
            }

            Map<String, Man> newData = new LinkedHashMap<>();
            newData.put(key, people);
            System.out.println(people);

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "INSERT_NEW_OBJECT");
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "\n" + messageFromClient.getMsg());

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не верно введены данные об объекте");
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
        dataStage.close();
        dataStage = null;
    }
}
