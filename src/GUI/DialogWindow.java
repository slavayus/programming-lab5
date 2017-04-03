package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Created by slavik on 03.04.17.
 */
public class DialogWindow implements Runnable {

    private Stage dialogStage = new Stage();
    private Button buttonOK = new Button("OK");


    @Override
    public void run() {
        buttonOKListener();
        dialogStage.centerOnScreen();
        dialogStage.setScene(new Scene(new VBox(getMessageLabel(), getButtonOK()), 300, 100));
        dialogStage.toFront();
        dialogStage.setMaximized(false);
        dialogStage.setResizable(false);
        dialogStage.show();
    }


    private void buttonOKListener() {
        buttonOK.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                dialogStage.close();
                new MainWindow().run();
            }
        });

        buttonOK.setOnMouseClicked(event -> {
            dialogStage.close();
            new MainWindow().run();
        });
    }

    private Label getMessageLabel() {
        Label messageLabel = new Label("Да шучу я, нет никакой авторизации.");
        messageLabel.setFont(Font.font("Liberation Serif", FontWeight.LIGHT, 16));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setPadding(new Insets(32, 0, 0, 20));

        return messageLabel;
    }

    private HBox getButtonOK() {
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(10, 18, 0, 245));

        return buttonOKHBox;
    }

}
