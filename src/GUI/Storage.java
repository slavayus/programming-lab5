package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import old.school.People;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by slavik on 19.02.17.
 */
public final class Storage implements Runnable {
    private Map<String, People> family = new ConcurrentHashMap<>();
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
        } catch (Exception e) {
            //doNothing();
        }
    }


    public Map<String, People> getFamily() {
        return family;
    }


    public static Storage getInstanceOf() {
        return instanceOf;
    }

    public static void setInstanceOf(Storage storage) {
        instanceOf = storage;
    }

}