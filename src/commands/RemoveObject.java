package commands;

import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import deprecated.People;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

/**
 * Created by slavik on 21.02.17.
 */
public class RemoveObject {
    private static Gson gson = new GsonBuilder().create();
    private static PrintWriter printWriter = new PrintWriter(System.out, true);
//    private String key;

    /**
     * Команда: remove_greater_key.
     * Удаляет из коллекции все элементы, ключ которых превышает заданный.
     *
     * @param key Ключ определенного объекта, который лежит в коллекции {@link Storage#family}.
     *            Ожидается формат {String}
     * @version 2.0
     */
    public void removeGreaterKey() throws InterruptedException {

        System.out.println("I'm here");
        getKey("Key");

    }

    /**
     * Команда remove.
     * Удаляет элемент из коллекции по его ключу.
     *
     * @param key Ключ - строковая переменная определенного объекта, который лежит в коллекции {@link Storage#family}
     * @version 2.0
     */
    public static void removeWithKey() {
        //need to correct
        String key = "";
        try {
            int size = Storage.getInstanceOf().getFamily().size();

            int first = key.indexOf('{');
            if (first < 0)
                throw new IndexOutOfBoundsException();

            int last = ++first;
            while (key.charAt(last) != '}') {
                last++;
            }

            final String substring = key.substring(first, last);
            Storage.getInstanceOf().getFamily().remove(substring);
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (IndexOutOfBoundsException e) {
            printWriter.println("Укажите корректный ключ");
        } catch (NullPointerException ex) {
            printWriter.println("Ужите ключ");
        }
    }

    /**
     * Команда remove_greater.
     * Удаляет из коллекции все элементы, превышающие заданный.
     *
     * @param object Ожидается строка формата json для преобразования в объект {@link People}.
     * @version 2.0
     * @since 1.0
     */
    public static void removeGreater() {
        //need to correct
        String object = "";
        try {
            int size = Storage.getInstanceOf().getFamily().size();
            People people = gson.fromJson(object, People.class);
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge() > people.getAge());
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }

    }

    /**
     * Команда remove_all.
     * Удалят из коллекции все элементы, эквивалентные заданному.
     *
     * @param object Ожидается строка формата json для преобразования в объект {@link People}
     * @version 2.0
     * @since 1.0
     */
    public static void removeAll() {
        //need to correct
        String object = "";
        try {
            int size = Storage.getInstanceOf().getFamily().size();
            People people = gson.fromJson(object, People.class);
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge() == people.getAge());
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }
    }

    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param object Строковая переменная.
     *               Можно передать строку в формате json для парсинга в конкретный экзеплят {@link People}.
     *               Так же можно передать ключ для коллекции {@link Storage#family}.
     * @version 1.0
     * @see RemoveObject#removeLowerKey(String)
     * @see RemoveObject#removeLowerObject(String)
     */
    public static void removeLower() {
        //need to correct
        String object = "";
        if (!removeLowerObject(/*object*/)) {
            removeLowerKey(/*object*/);
        }
    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param object Ожидается строка формата json для преобразования в конкретный экземпляр класса {@link People}
     * @return boolean Сигнал об успешном распозновании объекта.
     * @version 2.0
     * @since 1.0
     */
    private static boolean removeLowerObject() {
        //need to correct
        String object = "";
        try {
            People people = gson.fromJson(object, People.class);
            int size = Storage.getInstanceOf().getFamily().size();
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge() < people.getAge());

            printWriter.printf("Удаление по объекту. Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, ключ которых меньше, чем заданный.
     *
     * @param key Ожидается ключ для коллекции {@link Storage#family}.
     * @version 1.0
     */
    private static void removeLowerKey() {
        //need to correct
        String key = "";
        try {
            int size = Storage.getInstanceOf().getFamily().size();

            int first = key.indexOf('{');
            if (first < 0)
                throw new IndexOutOfBoundsException();

            int last = ++first;
            while (key.charAt(last) != '}') {
                last++;
            }

            final String substring = key.substring(first, last);
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getKey().compareTo(substring) < 0);
            printWriter.printf("Удаление по ключу. Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (IndexOutOfBoundsException e) {
            printWriter.println("Укажите корректный ключ");
        } catch (NullPointerException ex) {
            printWriter.println("Ужите ключ");
        }
    }

    private void getKey(String element) {
        Stage keyStage = new Stage();

        Button buttonOK = new Button("OK");
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(0, 18, 0, 245));


        Label keyLabel = new Label("Please, enter " + element);
        keyLabel.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        TextField keyTextField = new TextField();
        keyTextField.setPromptText(element);

        VBox enterKeyVBox = new VBox(keyLabel, keyTextField, buttonOKHBox);
        enterKeyVBox.setSpacing(5);

        keyStage.setTitle("Enter " + element);
        keyStage.centerOnScreen();
        keyStage.setScene(new Scene(enterKeyVBox, 300, 90));
        keyStage.toFront();
        keyStage.setMaximized(false);
        keyStage.setResizable(false);
        keyStage.show();


        buttonOK.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String key = keyTextField.getText();
                keyStage.close();

                try {
                    int size = Storage.getInstanceOf().getFamily().size();

                    Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getKey().compareTo(key) > 0);
                    printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
                } catch (IndexOutOfBoundsException e) {
                    printWriter.println("Укажите корректный ключ");
                } catch (NullPointerException ex) {
                    printWriter.println("Ужите ключ");
                }



            }
        });

        buttonOK.setOnMouseClicked(event -> {
            keyStage.close();
//            key = keyTextField.getText();
        });
    }
}
