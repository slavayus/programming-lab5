import GUI.LoginWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by slavik on 30.10.16.
 */

public class lab5 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        new LoginWindow(primaryStage).run();
    }

}
