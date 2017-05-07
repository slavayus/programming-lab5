package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import deprecated.Food;
import old.school.*;
import deprecated.Place;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by slavik on 19.02.17.
 */
public final class Storage implements Runnable {
    private Map<String, Man> family = new ConcurrentHashMap<>();
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

    public void run() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());
        Gson gson = builder.create();

        Properties runProperties = new Properties();
        try {
            runProperties.load(Storage.class.getResourceAsStream("/properties/run.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (PrintWriter writeLog =new PrintWriter(runProperties.getProperty("file.err"))){
        StringBuilder data = new StringBuilder();
        try (FileReader reader = new FileReader("objects")) {
            int c;
            while ((c = reader.read()) != -1) {
                data.append((char) c);
            }
            Type typeMap = new TypeToken<Map<String, People>>() {
            }.getType();
            Map<String, People> map = gson.fromJson(data.toString(), typeMap);
            if (map == null)
                throw new NullPointerException();
            family.clear();
            family.putAll(map);
        } catch (Exception ex) {
//                writeLog.write(ex.getMessage()+"\n");
//                writeLog.close();
        }
//        } catch (FileNotFoundException e) {
//            doNothing();
//    }
    }



    public Map<Integer,People> getFamilyOfChild(){
        return familyOfChild;
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

    public  List<Place> getPlaces() {
        return places;
    }

    public static void setInstanceOf(Storage storage) {
        instanceOf = storage;
    }

    public void setFamily(Map<String, Man> family) {
        this.family = family;
    }
}