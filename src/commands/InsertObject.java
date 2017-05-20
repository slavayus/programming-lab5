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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by slavik on 08.04.17.
 */
public class InsertObject {
    private Stage dataStage;

    /**
     * Команда insert.
     * Добавляет новый элемент с заданным ключом.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @param bundle
     * @param ownerStage
     * @version 3.0
     */
    public void insertNewObject(TreeView<Container> peopleTree, ResourceBundle bundle, Stage ownerStage) {
        readKeyAndObject(peopleTree, (PropertyResourceBundle) bundle, ownerStage);
    }


    private void readKeyAndObject(TreeView<Container> peopleTree, PropertyResourceBundle bundle, Stage ownerStage) {
        dataStage = new Stage();
        dataStage.initModality(Modality.WINDOW_MODAL);
        dataStage.initOwner(ownerStage.getScene().getWindow());


        Label keyLabel = new Label(bundle.getString("message.please.enter.object"));
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));


        TextField keyTextField = new TextField();
        keyTextField.setPromptText(bundle.getString("message.key"));

        TextField objectTextField = new TextField();
        objectTextField.setPromptText("{name=\"" + bundle.getString("message.name") + "\";age=1}");


        Button buttonOK = new Button(bundle.getString("message.button.OK"));
        HBox buttonOKHBox = new HBox(buttonOK);
//        buttonOKHBox.setPadding(new Insets(0, 9, 0, 255));


        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                insertToCollection(peopleTree, objectTextField, keyTextField, bundle);
            }
        });


        buttonOK.setOnMouseClicked(event -> insertToCollection(peopleTree, objectTextField, keyTextField, bundle));

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, objectTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        dataStage.setTitle(bundle.getString("message.object"));
        dataStage.centerOnScreen();
        dataStage.setScene(new Scene(enterKeyVBox, 300, 120));
        dataStage.toFront();
        dataStage.setMaximized(false);
        dataStage.setResizable(false);
        dataStage.show();
        dataStage.setOnCloseRequest(event -> dataStage = null);
    }

    private void insertToCollection(TreeView<Container> peopleTree, TextField objectTextField, TextField keyTextField, PropertyResourceBundle bundle) {
        Gson gson = new GsonBuilder().create();

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

            if (!clientLoad.getConnection()) {
                new ShowAlert(Alert.AlertType.ERROR, bundle.getString("message.error"), messageFromClient.getMsg(), bundle);
                return;
            }

            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, bundle.getString("message.done"),
                        bundle.getString("message.you.had.an.old.version.of.the.collection") + "\n" +
                                bundle.getString("message.the.collection.was.updated"),
                        bundle);
            }
            new ShowAlert(Alert.AlertType.INFORMATION,
                    bundle.getString("message.done"), "\n" +
                    messageFromClient.getMsg(),
                    bundle);

        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"), "\n" +
                    bundle.getString("message.incorrect.object.data.entered"),
                    bundle);
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.unable.to.recognize.object") + ", \n" +
                            bundle.getString("message.verify.the.correctness.of.the.data"),
                    bundle);
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, bundle.getString("message.error"), "\n" + bundle.getString("message.could.not.connect.to.server"), bundle);
        }
        dataStage.close();
        dataStage = null;
    }
}
