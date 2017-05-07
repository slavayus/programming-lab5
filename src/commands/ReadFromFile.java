package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import old.school.People;
import javafx.scene.control.TreeView;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

/**
 * Created by slavik on 27.04.17.
 */
public class ReadFromFile implements Runnable {
    private TreeView<Container> peopleTree;
    private File selectedFile;

    public synchronized void fieldData(File selectedFile, TreeView<Container> peopleTree) {
        this.peopleTree = peopleTree;
        this.selectedFile = selectedFile;
    }

    @Override
    public synchronized void run() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());
        Gson gson = builder.create();

        Properties runProperties = getProperties(ReadFromFile.class.getResourceAsStream("/properties/run.properties"));
        try (PrintWriter errorWriter = new PrintWriter(runProperties.getProperty("file.err"))) {
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
                errorWriter.println(Storage.getInstanceOf().getFamily().size() - size + " объекта считано с файла:" + "\n" + selectedFile.getName());
                peopleTree.setRoot(MainWindow.getTreeForPeople());
            } catch (JsonSyntaxException e) {
                errorWriter.println("Не удалось распознать объект, \nпроверьте корректность данных");
            } catch (IOException ex) {
                errorWriter.println("Произошла ошибка при чтении файла");
            } catch (NullPointerException ex) {
                errorWriter.println("Произошла ошибка, возможно файл пуст\n ");
            }
        }catch (FileNotFoundException e) {
            //do nothing
        }
    }

    private Properties getProperties(InputStream file) {
        Properties properties = new Properties();

        try {
            properties.load(file);
        } catch (IOException e) {
            //do nothing
        }
        return properties;
    }
}
