package commands;

import GUI.Storage;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by slavik on 05.04.17.
 */
public class OtherMethods {
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

    /**
     * Команда: info.
     * Выводит основную информацию о классе Storage.
     *
     * @param currentDate На вход ожидается дата заполнения коллекции {@link Storage#family}
     * @since 1.0
     */
    public static void writeInfo(Date currentDate) {
        PrintWriter printWriter = new PrintWriter(System.out);
        Class cl = Storage.getInstanceOf().getFamily().getClass();
        printWriter.println("Имя коллекции - " + cl.getCanonicalName());
        printWriter.println("Дата инициализации - " + currentDate);
        printWriter.println("Количество элемнтов - " + Storage.getInstanceOf().getFamily().size());
        printWriter.println("Пакет - " + cl.getPackage());
        printWriter.println("Имя родительсокго класса - " + cl.getSuperclass().getName());
        printWriter.println("Интерфейсы: ");
        Class[] interfaces = cl.getInterfaces();
        for (Class c : interfaces) {
            printWriter.println(c.getName());
        }
    }
}
