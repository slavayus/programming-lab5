package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import old.school.Man;
import old.school.People;
import deprecated.Place;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slavik on 05.04.17.
 */
public class OtherMethods {


    /**
     * Команда clear.
     * Очищает коллекцию.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     * @since 1.0
     */
    public void clear(TreeView<Container> peopleTree) {

        try {
            Map<String, Man> newData = new LinkedHashMap<>();

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "CLEAR");
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", messageFromClient.getMsg() + " \nWas removed " + messageFromClient.getModifiedRow() + " objects");
            peopleTree.setRoot(MainWindow.getTreeForPeople());
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
    }

    /**
     * Команда save.
     * Сохраняет весь объект типа {@link Storage} в файл.
     *
     * @version 3.0
     */
    public void save() {
        new Thread(new SaveDataToFile()).start();
        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Коллекция 'family' будет сохранена в файл: \nobjects");
    }

    /**
     * Команда load.
     * Загружает дефолтные объекты типа {@link Storage} данные в коллекцию.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void loadDefaultObjects(TreeView<Container> peopleTree) {
        Map<String, Man> newData = new LinkedHashMap<>();

        People x = new People();

        for (int i = 0; i < Storage.getInstanceOf().getАllPlaces(); i++) {
            Storage.getInstanceOf().getPlaces().add(new Place());
        }

        x.setName("Малыш");
        x.setAge(7);
        newData.put("0", x);

        x = new People("мама");
        x.setAge(31);
        newData.put("1", x);

        x = new People("Папа");
        x.setAge(32);
        newData.put("2", x);

        x = new People("Босс");
        x.setAge(23);
        newData.put("3", x);

        x = new People("Бетан");
        x.setAge(26);
        newData.put("4", x);

        x = new People("Бок");
        x.setAge(38);
        newData.put("5", x);

        x = new People("Фрид");
        x.setAge(45);
        newData.put("6", x);


        try {
            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "LOAD");
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }

            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "\n" + messageFromClient.getMsg() + "\n");

            peopleTree.setRoot(MainWindow.getTreeForPeople());
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
    }
}
