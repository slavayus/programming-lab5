package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import old.school.Man;
import old.school.People;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by slavik on 22.02.17.
 */

public class AddObjects {
    private static Gson gson = new GsonBuilder().create();
    private Stage dataStage = null;
    private TextField keyTextField = new TextField();
    private Button buttonOK;
    private PropertyResourceBundle bundle;

    /**
     * Команда: add_if_max.
     * Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void addIfMax(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField(false, peopleTree, bundle, ownerStage);
    }

    /**
     * Команда add_if_min.
     * Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 2.0
     */
    public void addIfMin(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        readDataFromTextField(true, peopleTree, bundle, ownerStage);
    }

    private void readDataFromTextField(boolean min, TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        dataStage = new Stage();
        dataStage.initModality(Modality.WINDOW_MODAL);
        dataStage.initOwner(ownerStage.getScene().getWindow());

        setDataStageScene(bundle);

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addToCollection(min, peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> addToCollection(min, peopleTree, keyTextField));

        dataStage.centerOnScreen();
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    public Scene generateScene(ResourceBundle bundle) {
        Label keyLabel = new Label(bundle.getString("message.please.enter.object"));
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        keyTextField.setPromptText("{name=\"" + bundle.getString("message.name") + "\";age=1}");


        buttonOK = new Button(bundle.getString("message.button.OK"));
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setCenterShape(true);
//        buttonOKHBox.setPadding(new Insets(0, 9, 0, 255));


        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.setTitle(bundle.getString("message.object"));
        return new Scene(enterKeyVBox, 300, 90);
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

            if (!clientLoad.getConnection()) {
                new ShowAlert(Alert.AlertType.ERROR, bundle.getString("message.error"), messageFromClient.getMsg(), bundle);
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
                    bundle.getString("message.done"), "\n" +
                    messageFromClient.getMsg(),
                    bundle);

        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.unable.to.recognize.object") + ",\n" +
                            bundle.getString("message.verify.the.correctness.of.the.data"), bundle);
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"), "\n" +
                    bundle.getString("message.incorrect.object.data.entered"),
                    bundle);
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"), "\n" +
                    bundle.getString("message.could.not.connect.to.server"),
                    bundle);
        }

        peopleTree.setRoot(MainWindow.getTreeForPeople());
        dataStage.close();
        dataStage = null;
    }


    public void setDataStageScene(ResourceBundle bundle) {
        this.dataStage.setScene(generateScene(bundle));
        this.bundle = (PropertyResourceBundle) bundle;
    }

    public Stage getDataStage() {
        return dataStage;
    }
}