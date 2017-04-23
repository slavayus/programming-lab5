package GUI;

import deprecated.People;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by slavik on 25.02.17.
 */
public class ParseCSV {

    private final static ParseCSV instanceOf = new ParseCSV();
    private PrintWriter printWriter = new PrintWriter(System.out, true);
    private static final Storage storage;

    static {
        storage = new Storage();
    }


    public static ParseCSV getInstanceOf() {
        return instanceOf;
    }

    public boolean readFromFile(String path) {
        int count = 1;
        int numberOfReadLine = 0;
        try (FileReader fileReader = new FileReader(path)) {
            int c;
            String object = "";
            while ((c = fileReader.read()) != -1) {
                if ((char) c == '\n') {
                    if (fromCSV(object, count)) {
                        numberOfReadLine++;
                    }
                    count++;
                    object = "";
                } else {
                    object = object + (char) c;
                }
            }

            printWriter.printf("Было считано %d строк из файла %s\n", numberOfReadLine,path);
            Storage.setInstanceOf(storage);
            return true;
        } catch (FileNotFoundException e) {
            printWriter.println("Файл не найден.");
            return false;
        }catch (IOException e) {
            printWriter.println("При чтении файла произошла ошибка. Проверьте содержимое файла.");
            return false;
        }
    }


    /**
     * Парсит CSV.
     * @param csv ожидается строка формата CSV.
     *            Пример входных данных: Имя;Возраст;[Miss/Bother/Chat;String[]]
     *@param count Номер строки из файла.
     *@return boolean Сигнал об успешном парсинге строки.
     */
    private boolean fromCSV(String csv, int count) {
        String[] object = csv.split(";");
        People people = new People();
        Method method = null;
        boolean state = false;

        try {
            people = new People(object[0]);
            people.setAge((byte) Integer.parseInt(object[1]));
            state = true;
        } catch (IndexOutOfBoundsException ex) {
            printWriter.printf("Не удалось распознать объект в строке %d, у человека должно быть хотябы имя и возраст \n", count);
        } catch (NumberFormatException ex) {
            printWriter.printf("Проверьте %d строку, возраст не может быть символом/строкой \n", count);
        }

        if (state)
            for (int i = 2; i < object.length; ) {
                try {
                    Class cl = Class.forName(object[i] + "able");
                    method = people.getClass().getDeclaredMethod(("set" + object[i]), cl);
                    if (i + 1 < object.length) {
                        method.invoke(people, new People(object[i + 1]));
                    }
                    i += 2;
                } catch (ClassNotFoundException e) {
                    try {
                        method.invoke(people, new People(object[i]));
                    } catch (Exception ex) {
                        printWriter.printf("В строке %d ошибка %s, такой коллекции нет \n", count, object[i]);
                    }
                    ++i;
                } catch (NoSuchMethodException e) {
                    printWriter.printf("В строке %d ошибка в объявлении метода %s \n", count, object[i]);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    printWriter.printf("В строке %d ошибка в вызове метода %s\n", count, object[i]);
                }
            }
        if (state)
            storage.getFamily().put(String.valueOf(storage.getFamily().size()), people);
        return state;
    }
}