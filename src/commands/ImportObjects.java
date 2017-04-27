package commands;

import GUI.Container;
import GUI.NoFileSelectedExceprion;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

import java.io.File;

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
                throw new NoFileSelectedExceprion("Произошла ошибка, не был выбран файл\n ");
            } catch (NoFileSelectedExceprion noFileSelected) {
                new ShowAlert(Alert.AlertType.ERROR, "Error", noFileSelected.getMessage());
            }
        } else {
            ReadFromFile readFromFile = new ReadFromFile();
            readFromFile.fieldData(selectedFile, peopleTree);
            Thread thread = new Thread(readFromFile);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            new Thread(readFromFile).start();
        }
    }
}
