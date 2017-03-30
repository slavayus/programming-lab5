import GUI.Storage;
import GUI.GUI;
import GUI.Stor;
import deprecated.People;

/**
 * Created by slavik on 30.10.16.
 */

public class lab5 {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Storage.getInstanceOf().saveStorage();
                } catch (NullPointerException ex) {
                    System.out.println("Коллекция пуста, нечего сохранять в файл.");
                }
            }
        });

        new Thread(new GUI(args.length==0?null:args[0])).start();
    }

    public void testStore(){
        Stor<People> stor = new Stor<People>();
        stor.add(new People());
        stor.remove(new People());
        stor.get(new People());
        for(People p : stor) {

        }
    }

}