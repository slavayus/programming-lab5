package GUI;

import old.school.ParseCSV;

import static commands.RemoveObject.*;
import static commands.AddObjects.*;
import static commands.OtherMethods.*;
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
//                    removeGreaterKey();
                    break;
                }
                case "add_if_max": {
                    addIfMax();
                    break;
                }
                case "save": {
                    Storage.getInstanceOf().saveStorage();
                    break;
                }
                case "remove_lower": {
                    break;
                }
                case "insert": {
                    insertNewObject();
                    break;
                }
                case "remove_greater": {
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
                    break;
                }
                case "remove": {
                    break;
                }
                case "import": {
                    importAllFromFile();
                    break;
                }
                case "clear": {
                    clear();
                    break;
                }
                case "add_if_min": {
                    addIfMin();
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


//            ResultSet resultSet = new ConnectDB().readDataFromDB();
//            while (resultSet.next()) {
//                System.out.print(resultSet.getString(1));
//                for (int i = resultSet.getString(1).length(); i < 15; i++)
//                    System.out.print(" ");
//                System.out.println(resultSet.getString(2));
//            }
//        } catch (SQLException e) {
//            System.out.println("Соединение с базой разорвано.");
//            Thread.interrupted();
//        }


    }
}