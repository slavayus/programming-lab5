package commands;
import GUI.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import deprecated.People;

import java.io.PrintWriter;

/**
 * Created by slavik on 21.02.17.
 */
public class RemoveObject {
    private static Gson gson = new GsonBuilder().create();
    private static PrintWriter printWriter = new PrintWriter(System.out, true);

    /**
     * Команда: remove_greater_key.
     * Удаляет из коллекции все элементы, ключ которых превышает заданный.
     *
     * @param key Ключ определенного объекта, который лежит в коллекции {@link Storage#family}.
     *            Ожидается формат {String}
     * @version 2.0
     */
    public static void removeGreaterKey(String key) {
        try {
            int size = Storage.getInstanceOf().getFamily().size();

            int first = key.indexOf('{');
            if (first < 0)
                throw new IndexOutOfBoundsException();

            int last = ++first;
            while (key.charAt(last) != '}') {
                last++;
            }

            final String substring = key.substring(first, last);
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getKey().compareTo(substring) > 0);
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (IndexOutOfBoundsException e) {
            printWriter.println("Укажите корректный ключ");
        } catch (NullPointerException ex) {
            printWriter.println("Ужите ключ");
        }
    }

    /**
     * Команда remove.
     * Удаляет элемент из коллекции по его ключу.
     *
     * @param key  Ключ - строковая переменная определенного объекта, который лежит в коллекции {@link Storage#family}
     * @version 2.0
     */
    public static void removeWithKey(String key) {
        try {
            int size = Storage.getInstanceOf().getFamily().size();

            int first = key.indexOf('{');
            if (first < 0)
                throw new IndexOutOfBoundsException();

            int last = ++first;
            while (key.charAt(last) != '}') {
                last++;
            }

            final String substring = key.substring(first, last);
            Storage.getInstanceOf().getFamily().remove(substring);
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (IndexOutOfBoundsException e) {
            printWriter.println("Укажите корректный ключ");
        } catch (NullPointerException ex) {
            printWriter.println("Ужите ключ");
        }
    }

    /**
     * Команда remove_greater.
     * Удаляет из коллекции все элементы, превышающие заданный.
     *
     * @param object Ожидается строка формата json для преобразования в объект {@link People}.
     * @version 2.0
     * @since 1.0
     */
    public static void removeGreater(String object) {

        try {
            int size = Storage.getInstanceOf().getFamily().size();
            People people = gson.fromJson(object, People.class);
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge() > people.getAge());
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }

    }

    /**
     * Команда remove_all.
     * Удалят из коллекции все элементы, эквивалентные заданному.
     *
     * @param object Ожидается строка формата json для преобразования в объект {@link People}
     * @version 2.0
     * @since 1.0
     */
    public  static void removeAll(String object) {
        try {
            int size = Storage.getInstanceOf().getFamily().size();
            People people = gson.fromJson(object, People.class);
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge()==people.getAge());
            printWriter.printf("Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (JsonSyntaxException ex) {
            printWriter.println("Не удалось распознать объект, проверьте корректность данных");
            printWriter.println(ex.getCause());
        } catch (NullPointerException ex) {
            printWriter.println("Не ввели данные об объекте");
        }
    }

    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param object Строковая переменная.
     *               Можно передать строку в формате json для парсинга в конкретный экзеплят {@link People}.
     *               Так же можно передать ключ для коллекции {@link Storage#family}.
     * @version 1.0
     * @see RemoveObject#removeLowerKey(String)
     * @see RemoveObject#removeLowerObject(String)
     */
    public static void removeLower(String object) {
        if (!removeLowerObject(object)) {
            removeLowerKey(object);
        }
    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, меньшие, чем заданный.
     *
     * @param object Ожидается строка формата json для преобразования в конкретный экземпляр класса {@link People}
     * @version 2.0
     * @since 1.0
     * @return boolean Сигнал об успешном распозновании объекта.
     */
    private static boolean removeLowerObject(String object) {
        try {
            People people = gson.fromJson(object, People.class);
            int size = Storage.getInstanceOf().getFamily().size();
            if (people == null)
                throw new NullPointerException();
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getValue().getAge() < people.getAge());

            printWriter.printf("Удаление по объекту. Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * Команда remove_lower.
     * Удаляет из коллекции все элементы, ключ которых меньше, чем заданный.
     *
     * @param key Ожидается ключ для коллекции {@link Storage#family}.
     * @version 1.0
     */
    private static void removeLowerKey(String key) {
        try {
            int size = Storage.getInstanceOf().getFamily().size();

            int first = key.indexOf('{');
            if (first < 0)
                throw new IndexOutOfBoundsException();

            int last = ++first;
            while (key.charAt(last) != '}') {
                last++;
            }

            final String substring = key.substring(first, last);
            Storage.getInstanceOf().getFamily().entrySet().removeIf(entry -> entry.getKey().compareTo(substring) < 0);
            printWriter.printf("Удаление по ключу. Операция выполнена успешно. Удалено %d объекта\n", size - Storage.getInstanceOf().getFamily().size());
        } catch (IndexOutOfBoundsException e) {
            printWriter.println("Укажите корректный ключ");
        } catch (NullPointerException ex) {
            printWriter.println("Ужите ключ");
        }
    }

    /**
     * Команда clear.
     * Очищает коллекцию.
     *  @version 1.0
     *  @since 1.0
     */
    public static void clear() {
        int size = Storage.getInstanceOf().getFamily().size();
        Storage.getInstanceOf().getFamily().clear();
        System.out.printf("Коллекция очищена. Удалено %d объектов\n",size);
    }
}
