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
import javafx.stage.Modality;
import javafx.stage.Stage;
import old.school.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by slavik on 08.04.17.
 */
public class ImportObjects {

    /**
     * Команда import.
     * добавляет в коллекцию все данные из файла.
     *
     * @param peopleTree Ожидается TreeView<Container> для изменения содержимого
     * @param bundle
     * @param ownerStage
     * @version 3.0
     */
    public void importAllFromFile(TreeView<Container> peopleTree, ResourceBundle bundle, Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(ownerStage.getScene().getWindow());

        if (selectedFile == null) {
            try {
                throw new NoFileSelectedException(bundle.getString("message.an.error.occurred.the.file.was.not.selected") + "\n");
            } catch (NoFileSelectedException noFileSelected) {
                new ShowAlert(Alert.AlertType.ERROR, bundle.getString("message.error"), noFileSelected.getMessage(), (PropertyResourceBundle) bundle);
            }
        } else {
            readFromFile(peopleTree, selectedFile, (PropertyResourceBundle) bundle);
        }
    }

    private void readFromFile(TreeView<Container> peopleTree, File selectedFile, PropertyResourceBundle bundle) {
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
                    throw new JsonSyntaxException(bundle.getString("message.data.from.file.is.not.correct"));
                }
            }

            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "IMPORT_ALL_FROM_FILE");
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
                    bundle.getString("message.done"),
                    messageFromClient.getModifiedRow() +
                            bundle.getString("message.object.is.read.from.file") + "\n" +
                            selectedFile.getName(),
                    bundle);
            peopleTree.setRoot(MainWindow.getTreeForPeople());

        } catch (JsonSyntaxException e) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.unable.to.recognize.object") + ", \n" +
                            bundle.getString("message.verify.the.correctness.of.the.data"),
                    bundle);
        } catch (IOException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.an.error.occurred.while.reading.the.file"),
                    bundle);
        } catch (NullPointerException ex) {
            new ShowAlert(Alert.AlertType.ERROR,
                    bundle.getString("message.error"),
                    bundle.getString("message.an.error.occurred.maybe.the.file.is.empty"),
                    bundle);
        }
    }

}
