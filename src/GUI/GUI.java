package GUI;

import static commands.RemoveObject.*;
import static commands.AddObjects.*;
import java.io.*;
import java.util.*;

/**
 * Created by slavik on 19.02.17.
 *
 * @version 2.0
 * @since 1.0
 */
public class GUI implements Runnable {
    private String command, object;
    private Storage storage = new Storage();
    private boolean exit = false;
    private String file = null;
    private PrintWriter printWriter = new PrintWriter(System.out, true);

    public GUI(String file) {
        this.file = file;
    }

    @Override
    public void run() {
        if (file != null) {
            if (ParseCSV.getInstanceOf().readFromFile(file)) {
                storage = Storage.getInstanceOf();
                commandExecuting();
            }else dialog();
        } else{
//            System.out.println("Файл не найден");
            dialog();
        }
    }

    private void dialog() {
        printWriter.println("Загрузить дефолтовые данные или из файла?");
        Scanner sc = new Scanner(System.in);
        while (!exit) {
            printWriter.println("1 - дефолтовые");
            printWriter.println("2 - из файла");
            try {
                switch (sc.nextLine()) {
                    case "1": {
                        storage.loadDefaultObjects();
                        Storage.setInstanceOf(storage);
                        commandExecuting();
                        break;
                    }
                    case "2": {
                        printWriter.println("Введите путь к файлу");
                        if (ParseCSV.getInstanceOf().readFromFile(sc.nextLine())) {
                            storage = Storage.getInstanceOf();
                            commandExecuting();
                        }
                        break;
                    }
                    default:
                        printWriter.println("Введите корректные данные.");
                }
            } catch (NullPointerException ex) {
                printWriter.println("Приложение остановлено. Ожидалась команда .");
            } catch (NoSuchElementException | IllegalStateException e) {
                printWriter.println("Приложение остановлено. Ожидалась команда.");
                break;
            }
        }
    }


    //  Выборка команд
    private void commandExecuting() {
        Date currentDate = new Date();
        System.out.println("Команда -help для получения справки");
        System.out.println("Ожидание команды");
        Scanner sc = new Scanner(System.in);
        command = sc.nextLine();

        while (command!=null) {

            parseCommand(command);

            switch (command) {
                case "-help": {
                    helpForUser();
                    break;
                }
                case "remove_greater_key": {
                    removeGreaterKey(object);
                    break;
                }
                case "add_if_max": {
                    addIfMax(object);
                    break;
                }
                case "save": {
                    Storage.getInstanceOf().saveStorage();
                    break;
                }
                case "remove_lower": {
                    removeLower(object);
                    break;
                }
                case "insert": {
                    insetNewObject(object);
                    break;
                }
                case "remove_greater": {
                    removeGreater(object);
                    break;
                }
                case "load": {
                    Storage.getInstanceOf().loadFromFile();
                    break;
                }
                case "info": {
                    writeInfo(currentDate);
                    break;
                }
                case "remove_all": {
                    removeAll(object);
                    break;
                }
                case "remove": {
                    removeWithKey(object);
                    break;
                }
                case "import": {
                    importAllFromFile(object);
                    break;
                }
                case "clear": {
                    clear();
                    break;
                }
                case "add_if_min": {
                    addIfMin(object);
                    break;
                }
                case "show_state": {
                    printWriter.println(Storage.getInstanceOf().getFamily());
                    break;
                }
                case "exit": {
                    exit = true;
                    break;
                }
                default:
                    printWriter.println("Нет такой команды");
            }
            if (exit) {
                break;
            } else {
                printWriter.println("Ожидание команды");
                command = sc.nextLine();
            }
        }
    }


    //Parse входных данных
    private void parseCommand(String command) {
        command = command.trim();
        try {
            this.command = command.substring(0, command.indexOf(' '));
            this.object = command.substring(command.indexOf(' '), command.length()).trim();
        } catch (IndexOutOfBoundsException ex) {
            this.command = command;
            this.object = null;
        }
    }

    /**
     * Команда: -help.
     * Помощь юзеру. Вывод реализованных функций с кратким описанием.
     * При вызове метода происходит считывание инфорамации из базы данных.
     *
     * @since 1.0
     */
    private void helpForUser() {
        try (FileReader fileReader = new FileReader("help")) {
            int c = fileReader.read();
            while (c != -1) {
                System.out.print((char) c);
                c = fileReader.read();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Проверьте корректность файла");
        }
    }

    /**
     * Команда: info.
     * Выводит основную информацию о классе Storage.
     *
     * @param currentDate На вход ожидается дата заполнения коллекции {@link Storage#family}
     * @since 1.0
     */
    private void writeInfo(Date currentDate) {
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