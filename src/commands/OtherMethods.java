package commands;

import GUI.MainWindow;
import GUI.ReportWindow;
import GUI.Storage;
import deprecated.People;
import deprecated.Place;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by slavik on 05.04.17.
 */
public class OtherMethods {
    private Stage dataStage = null;


    /**
     * Команда clear.
     * Очищает коллекцию.
     *
     * @version 1.0
     * @since 1.0
     */
    public void clear(TreeView<String> peopleTree) {
        int size = Storage.getInstanceOf().getFamily().size();
        Storage.getInstanceOf().getFamily().clear();
        new ReportWindow("Done", "Коллекция очищена. \nУдалено " + size + " объектов").run();
        peopleTree.setRoot(MainWindow.getTreeForPeople());
    }

    /**
     * Команда save.
     * Сохраняет весь объект типа {@link Storage} в файл.
     *
     * @version 1.0
     */
    public void save(TreeView<String> peopleTree) {
        SaveDataToFile saveDataToFile = new SaveDataToFile();
        new Thread(saveDataToFile).start();
        if (saveDataToFile.getException() == null) {
            new ReportWindow("Done", "Коллекция 'family' была сохранена в файл: \nobjects").run();
        } else {
            new ReportWindow("Error", "Не хватает прав на запись в файл: \nobjects").run();
        }
    }

    public void loadDefaultObjects(TreeView<String> peopleTree) {
        People x = new People();

        for (int i = 0; i < Storage.getInstanceOf().getАllPlaces(); i++) {
            Storage.getInstanceOf().getPlaces().add(new Place());
        }

        x.setName("Малыш");
        x.setAge((byte) 7);
        Storage.getInstanceOf().getPlaces().get(0).setFull(x);
        Storage.getInstanceOf().getFamily().put("0", x);

        x = new People();
        x.setName("мама");
        x.setAge((byte) 31);
        Storage.getInstanceOf().getPlaces().get(1).setFull(x);
        Storage.getInstanceOf().getFamily().put("1", x);
        Storage.getInstanceOf().getFamilyOfChild().put(0, x);

        x = new People();
        x.setName("Папа");
        x.setAge((byte) 32);
        Storage.getInstanceOf().getPlaces().get(2).setFull(x);
        Storage.getInstanceOf().getFamily().put("2", x);
        Storage.getInstanceOf().getFamilyOfChild().put(1, x);

        x = new People();
        x.setName("Босс");
        x.setAge((byte) 23);
        Storage.getInstanceOf().getPlaces().get(3).setFull(x);
        Storage.getInstanceOf().getFamily().put("3", x);
        Storage.getInstanceOf().getFamilyOfChild().put(2, x);

        x = new People();
        x.setName("Бетан");
        x.setAge((byte) 26);
        Storage.getInstanceOf().getPlaces().get(4).setFull(x);
        Storage.getInstanceOf().getFamily().put("4", x);
        Storage.getInstanceOf().getFamilyOfChild().put(3, x);

        x = new People();
        x.setName("Бок");
        x.setAge((byte) 38);
        Storage.getInstanceOf().getPlaces().get(5).setFull(x);
        Storage.getInstanceOf().getFamily().put("5", x);

        x = new People();
        x.setName("Фрид");
        x.setAge((byte) 45);
        Storage.getInstanceOf().getPlaces().get(6).setFull(x);
        Storage.getInstanceOf().getFamily().put("6", x);

        new ReportWindow("Done","\nДанные загружены \n").run();
        peopleTree.setRoot(MainWindow.getTreeForPeople());
    }
}
