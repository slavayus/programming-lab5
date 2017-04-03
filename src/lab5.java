import GUI.*;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by slavik on 30.10.16.
 */

public class lab5 extends Application {
    public static void main(String[] args) {
        launch(args);
//        new Thread(new GUI(args.length == 0 ? null : args[0])).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        RegisterWindow registerWindow = new RegisterWindow(primaryStage);
        registerWindow.showRegisterWindow();


//        MainWindow workWithCollection = new MainWindow();
//        workWithCollection.getRegisterWindow(primaryStage);

//        workWithCollection.mainWindow();

    }

}
