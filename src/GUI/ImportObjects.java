package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import deprecated.People;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by slavik on 08.04.17.
 */
public class ImportObjects{


    /**
     * Команда import.
     * добавляет в коллекцию все данные из файла.
     *
     * @param path Ожидатеся имя файла или путь к файлу, содержащий коллекцию {@link Storage#family}
     * @version 2.0
     */
    public void importAllFromFile(TreeView<String> peopleTree) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile==null){
            try {
                throw new NoFileSelected("Произошла ошибка, не был выбран файл\n ");
            } catch (NoFileSelected noFileSelected) {
                new ReportWindow("Error", noFileSelected.getMessage()).run();
            }
        }else {
            readFromFile(selectedFile, peopleTree);
        }
    }

    private void readFromFile(File selectedFile, TreeView<String> peopleTree){
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

            int size = Storage.getInstanceOf().getFamily().size();
            Type typeMap = new TypeToken<Map<String, People>>() {
            }.getType();
            Storage.getInstanceOf().getFamily().putAll(gson.fromJson(string.toString(), typeMap));
            new ReportWindow("Done", Storage.getInstanceOf().getFamily().size() - size + " объекта считано с файла:" + "\n" + selectedFile.getName()).run();
            peopleTree.setRoot(MainWindow.getTreeForPeople());
        } catch (JsonSyntaxException e) {
            new ReportWindow("Error", "Не удалось распознать объект, \nпроверьте корректность данных").run();
        } catch (IOException ex) {
            new ReportWindow("Error", "Произошла ошибка при чтении файла").run();
        } catch (NullPointerException ex) {
            new ReportWindow("Error", "Произошла ошибка, возможно файл пуст\n ").run();
        }
    }
}
