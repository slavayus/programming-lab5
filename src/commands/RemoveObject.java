package commands;

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
     * @param key        Ключ определенного объекта, который лежит в коллекции {@link Storage#family}.
     *                   Ожидается формат {String}
     * @param peopleTree
     * @version 2.0
     */
    public void removeGreaterKey(TreeView<String> peopleTree) {
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
     * @param key Ключ - строковая переменная определенного объекта, который лежит в коллекции {@link Storage#family}
     * @version 2.0
     */
    public void removeWithKey(TreeView<String> peopleTree) {
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
     * @param object Ожидается строка формата json для преобразования в объект {@link People}.
     * @version 2.0
     * @since 1.0
     */
    public void removeGreater(TreeView<String> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object in Json");
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
     * @param object Ожидается строка формата json для преобразования в объект {@link People}
     * @version 2.0
     * @since 1.0
     */
    public void removeAll(TreeView<String> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object in Json");
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
     * @param object     Ожидается строка формата json для преобразования в конкретный экземпляр класса {@link People}
     * @param data
     * @param peopleTree
     * @return boolean Сигнал об успешном распозновании объекта.
     * @version 2.0
     * @since 1.0
     */
    public void removeLowerObject(TreeView<String> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField("Object in Json");
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
     * @param key        Ожидается ключ для коллекции {@link Storage#family}.
     * @param data
     * @param peopleTree
     * @version 1.0
     */
    public void removeLowerKey(TreeView<String> peopleTree) {
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

    private void removeFromCollectionWithKey(Predicate<Map.Entry<String, People>> predicate, TreeView<String> peopleTree, TextField textField) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;

        int size = Storage.getInstanceOf().getFamily().size();
        Storage.getInstanceOf().getFamily().entrySet().removeIf(predicate);
        peopleTree.setRoot(MainWindow.getTreeForPeople());
        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Операция выполнена успешно. \nУдалено " + (size - Storage.getInstanceOf().getFamily().size()) + " объекта.");
    }


    private void removeFromCollectionWithObject(Predicate<Map.Entry<String, People>> predicate, TreeView<String> peopleTree, TextField textField) {
        this.data = textField.getText();
        dataStage.close();
        dataStage = null;

        try {
            people = gson.fromJson(data, People.class);
            int size = Storage.getInstanceOf().getFamily().size();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(predicate);
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            new ShowAlert(Alert.AlertType.INFORMATION, "Done","Операция выполнена успешно. \nУдалено " + (size - Storage.getInstanceOf().getFamily().size()) + " объекта.");
        } catch (JsonSyntaxException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        }catch(NullPointerException ex){
            new ShowAlert(Alert.AlertType.ERROR,"Error", "Не ввели данные об объекте\n ");
        }
    }


}
