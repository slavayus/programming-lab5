package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.stage.Modality;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

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
    public void removeGreaterKey(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Key", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_GREATER_KEY", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_GREATER_KEY", bundle));

    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, ключ которых меньше, чем заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 1.0
     */
    public void removeLowerKey(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Key", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_LOWER_KEY", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_LOWER_KEY", bundle));

    }


    /**
     * Команда remove.
     * Удаляет элемент из коллекции по его ключу.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void removeWithKey(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Key", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_WITH_KEY", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleTree, keyTextField, "REMOVE_WITH_KEY", bundle));

    }


    /**
     * Команда remove_greater.
     * Удаляет из коллекции все элементы, превышающие заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeGreater(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Object", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_GREATER", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_GREATER", bundle));

    }

    /**
     * Команда remove_all.
     * Удалят из коллекции все элементы, эквивалентные заданному.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeAll(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Object", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_ALL", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_ALL", bundle));

    }

    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void removeLowerObject(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField("Object", bundle, ownerStage);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_LOWER_OBJECT", bundle);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleTree, keyTextField, "REMOVE_LOWER_OBJECT", bundle));

    }


    private void readDataFromTextField(String element, PropertyResourceBundle bundle, Stage ownerStage) {
        dataStage = new Stage();
        dataStage.initModality(Modality.WINDOW_MODAL);
        dataStage.initOwner(ownerStage.getScene().getWindow());
        Label keyLabel;

        if (element.equals("Key")) {
            keyLabel = new Label(bundle.getString("message.please.enter.key"));
            keyTextField.setPromptText(bundle.getString("message.key"));
            dataStage.setTitle(bundle.getString("message.key"));
        } else {
            keyLabel = new Label(bundle.getString("message.please.enter.object"));
            keyTextField.setPromptText(bundle.getString("message.object"));
            dataStage.setTitle(bundle.getString("message.object"));
        }


        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));


        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 18, 0, 245));

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.centerOnScreen();
        dataStage.setScene(new Scene(enterKeyVBox, 300, 90));
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    private void removeFromCollectionWithKey(TreeView<Container> peopleTree, TextField textField, String command, PropertyResourceBundle bundle) {
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
                new ShowAlert(Alert.AlertType.INFORMATION,
                        bundle.getString("message.done"),
                        bundle.getString("message.you.had.an.old.version.of.the.collection") + "\n" +
                                bundle.getString("message.the.collection.was.updated"),
                        bundle);

            }
            new ShowAlert(Alert.AlertType.INFORMATION,
                    bundle.getString("message.done"),
                    messageFromClient.getMsg() + " \n" +
                            bundle.getString("message.was.removed") +
                            messageFromClient.getModifiedRow() + " objects",
                    bundle);

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.incorrect.object.data.entered"),
                    bundle);
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"), "\n" +
                    bundle.getString("message.could.not.connect.to.server"),
                    bundle);
        }
    }


    private void removeFromCollectionWithObject(TreeView<Container> peopleTree, TextField textField, String command, PropertyResourceBundle bundle) {
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
                new ShowAlert(Alert.AlertType.ERROR,
                        bundle.getString("message.error"),
                        messageFromClient.getMsg(),
                        bundle);
                return;
            }

            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION,
                        bundle.getString("message.done"),
                        bundle.getString("message.you.had.an.old.version.of.the.collection") + "\n" +
                                bundle.getString("message.the.collection.was.updated"),
                        bundle);
            }
            new ShowAlert(Alert.AlertType.INFORMATION,
                    bundle.getString("message.done"),
                    messageFromClient.getMsg() + " \n" +
                            bundle.getString("message.was.removed") +
                            messageFromClient.getModifiedRow() + " objects",
                    bundle);

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.incorrect.object.data.entered"),
                    bundle);
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error",
                    bundle.getString("message.unable.to.recognize.object") + ", \n" +
                            bundle.getString("message.verify.the.correctness.of.the.data"),
                    bundle);
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"), "\n" +
                    bundle.getString("message.could.not.connect.to.server"),
                    bundle);
        }
    }


}
