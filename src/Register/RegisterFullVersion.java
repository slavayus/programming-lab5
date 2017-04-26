package Register;

import commands.ShowAlert;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by slavik on 13.04.17.
 */
public class RegisterFullVersion extends RegisterWindow implements Registerable {
    private Stage dialogStage = new Stage();

    @Override
    public void register() {
        dialogStage.setResizable(false);
        dialogStage.centerOnScreen();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(0, 10, 10, 10));
        Label messageLabel = new Label("Доступ заблокирован");
        Button button = new Button("Пути");
        button.setOnAction(event -> {
            new ShowAlert(Alert.AlertType.INFORMATION, "No", "Не путю");
            dialogStage.close();
            dialogStage = null;
        });
        gridPane.add(messageLabel, 1, 2);
        gridPane.add(button, 3, 3);
        dialogStage.setScene(new Scene(gridPane));
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
