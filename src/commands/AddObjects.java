package commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

import GUI.*;
import com.google.gson.reflect.TypeToken;
import deprecated.People;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

/**
 * Created by slavik on 22.02.17.
 */

public class AddObjects {
    private static Gson gson = new GsonBuilder().create();
    private static PrintWriter printWriter = new PrintWriter(System.out, true);
    private Stage dataStage = null;
    private People people;


    /**
     * Команда: add_if_max.
     * Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
     *
     * @param object     Экземплят типа {@link People} для добавления в коллекцию.
     * @param peopleTree
     * @version 2.0
     */
    public void addIfMax(TreeView<String> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField(1, peopleTree);
        } else {
            dataStage.toFront();
        }
    }

    /**
     * Команда add_if_min.
     * Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     *
     * @param object     Ожидается конкретный экземпляр класса {@link People}
     * @param peopleTree
     * @version 2.0
     */
    public void addIfMin(TreeView<String> peopleTree) {
        if (dataStage == null) {
            readDataFromTextField(2, peopleTree);
        } else {
            dataStage.toFront();
        }
    }

    /**
     * Команда insert.
     * Добавляет новый элемент с заданным ключом.
     *
     * @param string Экземплят типа {@link People} для добавления в коллекцию.
     * @version 2.0
     */
    public void insertNewObject(TreeView<String> peopleTree) {
        boolean flag = true;
        String string = "";
        String object = null, key = null;
        try {
            key = string.substring(string.indexOf('{') + 1, string.indexOf('}'));
            int first = string.indexOf('}') + 2;
            int last = first;
            while (string.charAt(last) != '}') {
                last++;
            }
            object = string.substring(first - 1, last + 1);
            object = object.trim();
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Не правильно введены данные об объекте");
            flag = false;
        } catch (NullPointerException ex) {
            System.out.println("Введите данные об объекте");
            flag = false;
        }

        if (flag) {
            try {
                People people = gson.fromJson(object, People.class);
                if (people == null) {
                    throw new NullPointerException();
                }
                Storage.getInstanceOf().getFamily().put(key, people);
                printWriter.println("Объект успешно добавлен");
            } catch (NullPointerException ex) {
                printWriter.println("Не ввели данные об объекте");
            } catch (JsonSyntaxException ex) {
                printWriter.println("Не удалось распознать объект, проверьте корректность данных");
                printWriter.println(ex.getCause());
            }
        }

    }

    private void readDataFromTextField(int min, TreeView<String> peopleTree) {
        dataStage = new Stage();


        Label keyLabel = new Label("Please, enter Object");
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        TextField keyTextField = new TextField();
        keyTextField.setPromptText("Object");


        Button buttonOK = new Button("OK");
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 18, 0, 245));

        buttonOK.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addToCollection(min, peopleTree, keyTextField);
            }
        });

        buttonOK.setOnMouseClicked(event -> {
            addToCollection(min, peopleTree, keyTextField);
        });

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

    private void addToCollection(int min, TreeView<String> peopleTree, TextField keyTextField) {
        String data = keyTextField.getText();
        try {
            People people = gson.fromJson(data, People.class);
            if (people == null) {
                throw new NullPointerException();
            }

            boolean flag = true;

            switch (min) {
                case 1:
                    for (People peopleCollection : Storage.getInstanceOf().getFamily().values()) {
                        if (peopleCollection.getAge() > people.getAge()) {
                            flag = false;
                            break;
                        }
                    }
                    break;
                case 2:
                    for (People peopleCollection : Storage.getInstanceOf().getFamily().values()) {
                        if (peopleCollection.getAge() < people.getAge()) {
                            flag = false;
                            break;
                        }
                    }
                    break;
            }


            if (flag) {
                switch (min) {
                    case 1: {
                        Storage.getInstanceOf().getFamily().put(String.valueOf(Storage.getInstanceOf().getFamily().size()), people);
                        break;
                    }
                    case 2: {
                        Storage.getInstanceOf().getFamily().put(String.valueOf(Storage.getInstanceOf().getFamily().size()), people);
                    }
                }

                printWriter.println("Объект успешно добавлен");
            } else {
                System.out.println("Объект не добавлен");
            }
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }

        peopleTree.setRoot(MainWindow.getTreeForPeople());
        dataStage.close();
        dataStage = null;
    }
}