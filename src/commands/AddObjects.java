package commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

import GUI.*;
import com.google.gson.reflect.TypeToken;
import deprecated.People;
import old.school.Botherable;
import old.school.Chatable;
import old.school.InterfaceAdapter;
import old.school.Missable;

/**
 * Created by slavik on 22.02.17.
 */

public class AddObjects {
    private static Gson gson = new GsonBuilder().create();
    private static PrintWriter printWriter = new PrintWriter(System.out, true);

    /**
     * Команда: add_if_max.
     * Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
     *
     * @param object Экземплят типа {@link People} для добавления в коллекцию.
     * @version 2.0
     */
    public static void addIfMax() {
        //need to correct
        String object = "";
        try {
            People people = gson.fromJson(object, People.class);
            if (people == null) {
                throw new NullPointerException();
            }
            boolean flag = true;
            for (People peopleCollection : Storage.getInstanceOf().getFamily().values()) {
                if (peopleCollection.getAge() > people.getAge()) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                Storage.getInstanceOf().getFamily().put(String.valueOf(Storage.getInstanceOf().getFamily().size()), people);
                printWriter.println("Объект успешно добавлен");
            } else {
                System.out.println("Объект не добавлен");
            }
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }
    }

    /**
     * Команда add_if_min.
     * Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     *
     * @param object Ожидается конкретный экземпляр класса {@link People}
     * @version 2.0
     */
    public static void addIfMin() {
        //need to correct
        String object = "";
        try {
            People people = gson.fromJson(object, People.class);
            if (people == null) {
                throw new NullPointerException();
            }
            boolean flag = true;
            for (People peopleCollection : Storage.getInstanceOf().getFamily().values()) {
                if (peopleCollection.getAge() < people.getAge()) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                Storage.getInstanceOf().getFamily().put(String.valueOf(Storage.getInstanceOf().getFamily().size()), people);
                printWriter.println("Объект успешно добавлен");
            } else {
                System.out.println("Объект не добавлен");
            }
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }
    }

    /**
     * Команда import.
     * добавляет в коллекцию все данные из файла.
     *
     * @param path Ожидатеся имя файла или путь к файлу, содержащий коллекцию {@link Storage#family}
     * @version 2.0
     */
    public static void importAllFromFile() {
        //need to correct
        String path = "";
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Missable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Chatable.class, new InterfaceAdapter<People>());
        builder.registerTypeAdapter(Botherable.class, new InterfaceAdapter<People>());
        Gson gson = builder.create();
        boolean flag = true;
        try {
            int first = path.indexOf('{');
            if (first < 0)
                throw new NullPointerException();
            int last = ++first;
            while (path.charAt(last) != '}') {
                last++;
            }
            path = path.substring(first, last);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            printWriter.println("Ошибка в имени файла");
            flag = false;
        }

        if (flag) {
            try (FileReader reader = new FileReader(path)) {
                int c;
                String string = "";
                while ((c = reader.read()) != -1) {
                    string = string + (char) c;
                }

                int size = Storage.getInstanceOf().getFamily().size();
                Type typeMap = new TypeToken<Map<String, People>>() {
                }.getType();
                Storage.getInstanceOf().getFamily().putAll(gson.fromJson(string, typeMap));
                printWriter.printf("%d объекта считано с файла 'objects'\n", Storage.getInstanceOf().getFamily().size() - size);
            } catch (JsonSyntaxException e) {
                printWriter.println("Не удалось распознать объект, проверьте корректность данных");
                printWriter.println(e.getCause());
            } catch (FileNotFoundException ex) {
                System.out.println("Файл не найден");
            } catch (IOException | IndexOutOfBoundsException ex) {
                printWriter.println("Произошла ошибка при чтении файла");
            } catch (NullPointerException ex) {
                printWriter.println("Произошла ошибка, возможно файл пуст");
            }
        }
    }

    /**
     * Команда insert.
     * Добавляет новый элемент с заданным ключом.
     *
     * @param string Экземплят типа {@link People} для добавления в коллекцию.
     * @version 2.0
     */
    public static void insertNewObject() {
        //need to correct
        String string = "";
        boolean flag = true;
        String object = null,key = null;
        try {
            key = string.substring(string.indexOf('{') + 1, string.indexOf('}'));
            int first = string.indexOf('}') + 2;
            int last = first;
            while (string.charAt(last) != '}') {
                last++;
            }
            object = string.substring(first-1, last+1);
            object.trim();
        }catch (IndexOutOfBoundsException ex){
            System.out.println("Не правильно введены данные об объекте");
            flag = false;
        }catch (NullPointerException ex){
            System.out.println("Введите данные об объекте");
            flag = false;
        }

        if(flag){
            try {
                People people = gson.fromJson(object, People.class);
                if (people == null) {
                    throw new NullPointerException();
                }
                Storage.getInstanceOf().getFamily().put(key, people);
                printWriter.println("Объект успешно добавлен");
            }catch (NullPointerException ex){
                printWriter.println("Не ввели данные об объекте");
            } catch (JsonSyntaxException ex) {
                printWriter.println("Не удалось распознать объект, проверьте корректность данных");
                printWriter.println(ex.getCause());
            }
        }

    }
}