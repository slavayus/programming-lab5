package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by slavik on 21.02.17.
 */
public class RemoveObject {
    private static Gson gson = new GsonBuilder().create();
    private static PrintWriter printWriter = new PrintWriter(System.out, true);
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
                removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().compareTo(data) > 0, peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().compareTo(data) > 0, peopleTree, keyTextField));

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
                removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().equals(data), peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().equals(data), peopleTree, keyTextField));

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
            readDataFromTextField("{name=\"name\";age=1}");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() > people.getAge(), peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() > people.getAge(), peopleTree, keyTextField));

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
            readDataFromTextField("{name=\"name\";age=1}");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() == people.getAge(), peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() == people.getAge(), peopleTree, keyTextField));

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
            readDataFromTextField("{name=\"name\";age=1}");
        } else {
            dataStage.toFront();
        }

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() < people.getAge(), peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithObject(peopleEntry -> peopleEntry.getValue().getAge() < people.getAge(), peopleTree, keyTextField));

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
                removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().compareTo(data) < 0, peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> removeFromCollectionWithKey(peopleEntry -> peopleEntry.getKey().compareTo(data) < 0, peopleTree, keyTextField));

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

    private void removeFromCollectionWithKey(Predicate<Map.Entry<String, Man>> predicate, TreeView<Container> peopleTree, TextField textField) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;

        int size = Storage.getInstanceOf().getFamily().size();
        Storage.getInstanceOf().getFamily().entrySet().removeIf(predicate);
        peopleTree.setRoot(MainWindow.getTreeForPeople());
        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Операция выполнена успешно. \nУдалено " + (size - Storage.getInstanceOf().getFamily().size()) + " объекта.");
    }


    private void removeFromCollectionWithObject(Predicate<Map.Entry<String, Man>> predicate, TreeView<Container> peopleTree, TextField textField) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;

        try {
            people = gson.fromJson(data, People.class);
            int size = Storage.getInstanceOf().getFamily().size();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(predicate);
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Операция выполнена успешно. \nУдалено " + (size - Storage.getInstanceOf().getFamily().size()) + " объекта.");
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не ввели данные об объекте\n ");
        }
    }


}
