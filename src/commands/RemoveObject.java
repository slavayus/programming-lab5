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
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slavik on 21.02.17.
 */
public class RemoveObject {
    private static Gson gson = new GsonBuilder().create();
    private String data;
    private People people;
    private Stage dataStage = null;
    private TextField keyTextField = new TextField();
    private Button buttonOK = new Button("OK");

    /**
     * Команда: remove_greater_key.
     * Удаляет из коллекции все элементы, ключ которых превышает заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void removeGreaterKey(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Key");
        } else {
            dataStage.toFront();
        }
        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_GREATER_KEY");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_GREATER_KEY"));

    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, ключ которых меньше, чем заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 1.0
     */
    public void removeLowerKey(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Key");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_LOWER_KEY");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_LOWER_KEY"));

    }


    /**
     * Команда remove.
     * Удаляет элемент из коллекции по его ключу.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void removeWithKey(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Key");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_WITH_KEY");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_WITH_KEY"));

    }


    /**
     * Команда remove_greater.
     * Удаляет из коллекции все элементы, превышающие заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeGreater(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_GREATER");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_GREATER"));

    }

    /**
     * Команда remove_all.
     * Удалят из коллекции все элементы, эквивалентные заданному.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeAll(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_ALL");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_ALL"));

    }

    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeLowerObject(TreeView<Container> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_LOWER_OBJECT");
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_LOWER_OBJECT"));

    }


    private void readDataFromTextField(String element) {
        dataStage = new Stage();


        Label keyLabel = new Label("Please, enter " + element);
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        keyTextField.setPromptText(element);


        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 18, 0, 245));

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.setTitle(element);
        dataStage.centerOnScreen();
        dataStage.setScene(new Scene(enterKeyVBox, 300, 90));
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    private void removeFromCollectionWithKey(TreeView<Container> peopleTree, TextField textField, String command) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;


        try {
            Map<String, Man> newData = new LinkedHashMap<>();
            newData.put(data, new People("null"));
            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, command);
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", messageFromClient.getMsg() + " \nWas removed " + messageFromClient.getModifiedRow() + " objects");

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не верно введены данные об объекте");
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
    }


    private void removeFromCollectionWithObject(TreeView<Container> peopleTree, TextField textField, String command) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;

        try {
            People people = gson.fromJson(data, People.class);
            if (people == null || people.getAge() < 0 || !people.setName(people.getName())) {
                throw new NullPointerException();
            }

            Map<String, Man> newData = new LinkedHashMap<>();
            newData.put("0", people);

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, command);
            MessageFromClient messageFromClient = clientLoad.readData();

            if (!clientLoad.getConnection()) {
                new ShowAlert(Alert.AlertType.ERROR, "Error", messageFromClient.getMsg());
                return;
            }

            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", messageFromClient.getMsg() + " \nWas removed " + messageFromClient.getModifiedRow() + " objects");

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не верно введены данные об объекте");
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
    }


}
