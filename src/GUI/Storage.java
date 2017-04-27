package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import deprecated.Food;
import deprecated.People;
import deprecated.Place;
import javafx.scene.control.Alert;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by slavik on 19.02.17.
 */
public final class Storage implements Runnable {
    private Map<String, People> family = new ConcurrentHashMap<>();
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

        try (PrintWriter writeLog =new PrintWriter(runProperties.getProperty("file.err"))){
            StringBuilder data = new StringBuilder();
            try (FileReader reader = new FileReader("objects")) {
                int c;
                while ((c = reader.read()) != -1) {
                    data.append((char)c);
                }
                Type typeMap = new TypeToken<Map<String, People>>() {
                }.getType();
                Map<String,People> map = gson.fromJson(data.toString(),typeMap);
                if (map==null)
                    throw new NullPointerException();
                family.clear();
                family.putAll(map);
            } catch (Exception ex) {
                writeLog.write(ex.getMessage()+"\n");
//                writeLog.flush();
                writeLog.close();
            }
        } catch (FileNotFoundException e) {
            //doNothing();
        }
    }

    public void writeMessage() {

        //Вывод состояния стола.
        Food p = new Food("ОБЕД");
        //от этого зависит вывод о состоянии стола
        p.setState();
        String state = p.getState();
        System.out.print(p.getTypeFood() + ' ' + state + ", потому что " + p.getStateTable() + ". ");


        People x = family.get(0);

        //Вывод про малыша, о ком скучал.
        try {
            String s = "";
            if (x.getLengthListMiss() != 0) {
                s = x.getName() + " скучал ";
                for (int i = 0; i < x.getLengthListMiss() - 1; i++) {
                    s = s + "по " + x.getMiss(i).getName() + ", ";
                }

                s += x.getMiss(x.getLengthListMiss() - 1).getName();
                if (x.getLengthListMiss() == familyOfChild.size()) {
                    s = s + " и по всем вместе. ";
                } else {
                    s += ". ";
                }
            }
            System.out.print(s);
        } catch (Exception ex) {
        }
//        Вывод про Бок.
        try {
            String s;
            x = family.get(5);
            s = x.getName() + " болтала о ";
            for (int i = 0; i < x.getLengthListChat() - 1; i++) {
                s = s + x.getChat(i).getName() + " ";
            }
            s = s + x.getChat(x.getLengthListChat() - 1).getName();
            System.out.print(s);
//          если ввести другое число, то можно сломать
//            x.getChat(20);
        } catch (Exception ex) {
        }
        //Вывод о Фриде
        try {
            //можно поменять 1 на 2 и сломать программу.
            System.out.print(". " + family.get(0).getBother(family.get(0).getLengthListBother() - 1).getName() + " надоела " + family.get(0).getName() + ".");
        } catch (Exception ex) {
        }
        System.out.println();
    }


    public Map<Integer,People> getFamilyOfChild(){
        return familyOfChild;
    }

    public Map<String, People> getFamily() {
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

}