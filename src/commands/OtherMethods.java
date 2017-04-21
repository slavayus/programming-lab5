package commands;

import GUI.Container;
import GUI.MainWindow;
import GUI.Storage;
import deprecated.People;
import deprecated.Place;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

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
    public void clear(TreeView<Container> peopleTree) {
        int size = Storage.getInstanceOf().getFamily().size();
        Storage.getInstanceOf().getFamily().clear();
        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Коллекция очищена. \nУдалено " + size + " объектов");
        peopleTree.setRoot(MainWindow.getTreeForPeople());
    }

    /**
     * Команда save.
     * Сохраняет весь объект типа {@link Storage} в файл.
     *
     * @version 1.0
     */
    public void save() {
        new Thread(new SaveDataToFile()).start();
        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Коллекция 'family' будет сохранена в файл: \nobjects");
    }

    public void loadDefaultObjects(TreeView<Container> peopleTree) {
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

        new ShowAlert(Alert.AlertType.INFORMATION, "Done","\nДанные загружены \n");
        peopleTree.setRoot(MainWindow.getTreeForPeople());
    }
}
