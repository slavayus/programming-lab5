package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import deprecated.Food;
import deprecated.People;
import deprecated.Place;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by slavik on 19.02.17.
 */
public class Storage {
    private Map<String, People> family = new LinkedHashMap<>();
    private final int аllPlaces = 15;
    private Map<Integer, People> familyOfChild = new LinkedHashMap<>();
    private List<Place> places = new ArrayList<>();
    private List<People> atTable = new ArrayList<>();
    private static Storage instanceOf;

    public Storage() {
    }

    void loadFromFile() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());
        Gson gson = builder.create();


        String string = "";
        try (FileReader reader = new FileReader("objects")) {
            int c;
            while ((c = reader.read()) != -1) {
                string = string + (char) c;
            }
            Type typeMap = new TypeToken<Map<String, People>>() {
            }.getType();
            Map<String,People> map = gson.fromJson(string,typeMap);
            if (map==null)
                throw new NullPointerException();
            family.clear();
            family.putAll(map);
            System.out.printf("%d объекта считано с файла 'objects'\n", family.size());
        } catch (JsonSyntaxException e) {
            System.out.println("Не удалось распознать объект, проверьте корректность данных");
            System.out.println(e.getCause());
        } catch(FileNotFoundException ex) {
            System.out.println("Файл не найден");
        }catch (IOException ex) {
            System.out.println("Произошла ошибка при чтении файла");
        }catch (NullPointerException ex){
            System.out.println("Произошла ошибка, возможно файл пуст");
        }
    }


    void loadDefaultObjects() {
        People x = new People();

        for (int i = 0; i < аllPlaces; i++) {
            places.add(new Place());
        }

        x.setName("Малыш");
        x.setAge(7);
        places.get(0).setFull(x);
        family.put("0", x);

        x = new People();
        x.setName("мама");
        x.setAge(31);
        places.get(1).setFull(x);
        family.put("1", x);
        familyOfChild.put(0, x);

        x = new People();
        x.setName("Папа");
        x.setAge(32);
        places.get(2).setFull(x);
        family.put("2", x);
        familyOfChild.put(1, x);

        x = new People();
        x.setName("Босс");
        x.setAge(23);
        places.get(3).setFull(x);
        family.put("3", x);
        familyOfChild.put(2, x);

        x = new People();
        x.setName("Бетан");
        x.setAge(26);
        places.get(4).setFull(x);
        family.put("4", x);
        familyOfChild.put(3, x);

        x = new People();
        x.setName("Бок");
        x.setAge(38);
        places.get(5).setFull(x);
        family.put("5", x);

        x = new People();
        x.setName("Фрид");
        x.setAge(45);
        places.get(6).setFull(x);
        family.put("6", x);

        System.out.println("Данные загружены");
//        atTable.add(family.get(0));

//        atTable.add(family.get(5));
//        atTable.add(family.get(1));
//        atTable.add(family.get(2));
//        atTable.add(family.get(4));
//        atTable.add(family.get(3));


//        Map<Integer, People> familyClone = new LinkedHashMap<>(family);
//
//        for (int i = 0; i < atTable.size(); i++) {
//            familyClone.remove(i);
//        }
//        familyClone.remove(6);
//        for (People people : familyClone.values()) {
//            family.get(0).setMiss(people);
//        }
//
//        x = family.get(0);
//        x.setBother(family.get(6));
//
//        x = family.get(5);
//        x.setChat(family.get(6));
//
////        writeMessage();
    }


    /**
     * Команда save.
     * Сохраняет весь объект типа {@link Storage} в файл.
     *
     * @version 1.0
     */
    public void saveStorage() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());

        Gson gson = builder.create();

        try (PrintWriter printWriter = new PrintWriter("objects")) {
            printWriter.println(gson.toJson(Storage.getInstanceOf().getFamily()));
            System.out.println("Коллекция 'family' была сохранена в файл 'objects'");
        }catch (FileNotFoundException ex ){
            System.out.println("Не хватает прав на запись в файл 'objects'");
        }catch (Exception e) {
            PrintWriter printWriter = new PrintWriter(System.out,true);
            printWriter.println("При сериализации произошла ошибка");
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