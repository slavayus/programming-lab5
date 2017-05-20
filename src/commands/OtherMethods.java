package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import old.school.Man;
import old.school.People;
import deprecated.Place;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by slavik on 05.04.17.
 */
public class OtherMethods {


    /**
     * Команда clear.
     * Очищает коллекцию.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @param bundle
     * @version 3.0
     * @since 1.0
     */
    public void clear(TreeView<Container> peopleTree, ResourceBundle bundle) {

        try {
            Map<String, Man> newData = new LinkedHashMap<>();

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "CLEAR");
            MessageFromClient messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            new ShowAlert(Alert.AlertType.INFORMATION, bundle.getString("message.done"),
                    messageFromClient.getMsg() + " \n"+
                            bundle.getString("message.was.removed") +
                            messageFromClient.getModifiedRow() + " objects",
                    (PropertyResourceBundle) bundle);
            peopleTree.setRoot(MainWindow.getTreeForPeople());
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    "\n"+bundle.getString("message.could.not.connect.to.server"),
                    (PropertyResourceBundle)bundle);
        }
    }

    /**
     * Команда save.
     * Сохраняет весь объект типа {@link Storage} в файл.
     *
     * @param bundle
     * @version 3.0
     */
    public void save(ResourceBundle bundle) {
        new Thread(new SaveDataToFile()).start();
        new ShowAlert(Alert.AlertType.INFORMATION,
                bundle.getString("message.done"),
                bundle.getString("message.the.collection.family.will.be.saved.to.a.file") + "\nobjects",
                (PropertyResourceBundle) bundle);
    }

    /**
     * Команда load.
     * Загружает дефолтные объекты типа {@link Storage} данные в коллекцию.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @param bundle
     * @version 3.0
     */
    public void loadDefaultObjects(TreeView<Container> peopleTree, ResourceBundle bundle) {
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

            if (!clientLoad.getConnection()) {
                new ShowAlert(Alert.AlertType.ERROR,
                        bundle.getString("message.error"),
                        messageFromClient.getMsg(),
                        (PropertyResourceBundle)bundle);
                return;
            }

            peopleTree.setRoot(MainWindow.getTreeForPeople());

            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION,
                        bundle.getString("message.done"),
                        bundle.getString("message.you.had.an.old.version.of.the.collection") + "\n" +
                                bundle.getString("message.the.collection.was.updated"),
                        (PropertyResourceBundle) bundle);
            }

            new ShowAlert(Alert.AlertType.INFORMATION,
                    bundle.getString("message.done"),
                    "\n" + messageFromClient.getMsg() + "\n",
                    (PropertyResourceBundle)bundle);

            peopleTree.setRoot(MainWindow.getTreeForPeople());
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    "\n"+bundle.getString("message.could.not.connect.to.server"),
                    (PropertyResourceBundle)bundle);
        }
    }
}
