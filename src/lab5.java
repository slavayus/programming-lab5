import GUI.Storage;
import GUI.GUI;
/**
 * Created by slavik on 30.10.16.
 */

public class lab5 {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Storage.getInstanceOf().saveSorage();
                } catch (NullPointerException ex) {
                    System.out.println("Коллекция пуста, нечего сохранять в файл.");
                }
            }
        });

        new Thread(new GUI(args.length==0?null:args[0])).start();
    }
}