package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import deprecated.People;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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
     * @param string Экземплят типа {@link People} для добавления в коллекцию.
     * @version 2.0
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
            if (people == null) {
                throw new NullPointerException();
            }
            Storage.getInstanceOf().getFamily().put(key, people);
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "\nОбъект успешно добавлен");
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,"Error", "Не ввели данные об объекте");
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        }
        dataStage.close();
        dataStage = null;
    }
}
