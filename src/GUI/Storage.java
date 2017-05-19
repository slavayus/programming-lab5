package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commands.ShowAlert;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import old.school.*;
import deprecated.Place;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

/**
 * Created by slavik on 19.02.17.
 */
public final class Storage {
    private Map<String, Man> family = new LinkedHashMap<>();
    private final int аllPlaces = 15;
    private Map<Integer, People> familyOfChild = new LinkedHashMap<>();
    private List<Place> places = new ArrayList<>();
    private List<People> atTable = new ArrayList<>();
    private static Storage instanceOf;

    static {
        instanceOf = new Storage();
    }

    private Storage() {
    }

    void readFromDB() {
        MessageFromClient messageFromClient;
        try {
            Map<String, Man> newData = new HashMap<>();
            ClientLoad clientLoad = new ClientLoad();
            clientLoad.send(newData, "READ");
            messageFromClient = clientLoad.readData();
            Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
        }
    }


    public Map<String, Man> getFamily() {
        return family;
    }


    public int getАllPlaces() {
        return аllPlaces;
    }

    public static Storage getInstanceOf() {
        return instanceOf;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public static void setInstanceOf(Storage storage) {
        instanceOf = storage;
    }

    public void setFamily(Map<String, Man> family) {
        this.family = family;
    }
}