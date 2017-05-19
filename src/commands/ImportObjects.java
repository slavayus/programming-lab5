package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.NoFileSelectedException;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import old.school.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by slavik on 08.04.17.
 */
public class ImportObjects {

    /**
     * Команда import.
     * добавляет в коллекцию все данные из файла.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @version 3.0
     */
    public void importAllFromFile(TreeView<Container> peopleTree) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            try {
                throw new NoFileSelectedException("Произошла ошибка, не был выбран файл\n ");
            } catch (NoFileSelectedException noFileSelected) {
                new ShowAlert(Alert.AlertType.ERROR, "Error", noFileSelected.getMessage());
            }
        } else {
            readFromFile(peopleTree, selectedFile);
        }
    }

    private void readFromFile(TreeView<Container> peopleTree, File selectedFile) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());
        Gson gson = builder.create();

        try (FileReader reader = new FileReader(selectedFile)) {
            int c;
            StringBuilder string = new StringBuilder();
            while ((c = reader.read()) != -1) {
                string.append((char) c);
            }

            Type typeMap = new TypeToken<Map<String, People>>() {
            }.getType();

            Map<String, Man> newData = gson.fromJson(string.toString(), typeMap);
            for (Man people : newData.values()) {
                if (people == null || people.getAge() < 0 || !people.setName(people.getName())) {
                    throw new JsonSyntaxException("Data form file isn't correct");
                }
            }

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "IMPORT_ALL_FROM_FILE");
            MessageFromClient messageFromClient = clientLoad.readData();

            if (!clientLoad.getConnection()) {
                new ShowAlert(Alert.AlertType.ERROR, "Error", messageFromClient.getMsg());
                return;
            }

            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
            peopleTree.setRoot(MainWindow.getTreeForPeople());
            if (!messageFromClient.getClientCollectionState()) {
                new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
            }
            new ShowAlert(Alert.AlertType.INFORMATION, "Done", messageFromClient.getModifiedRow() + " объекта считано с файла:" + "\n" + selectedFile.getName());
            peopleTree.setRoot(MainWindow.getTreeForPeople());

        } catch (JsonSyntaxException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Не удалось распознать объект, \nпроверьте корректность данных");
        } catch (IOException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Произошла ошибка при чтении файла");
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Произошла ошибка, возможно файл пуст");
        }
    }

}
