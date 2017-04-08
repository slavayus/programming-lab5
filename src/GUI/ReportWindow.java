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

import java.io.File;

/**
 * Created by slavik on 08.04.17.
 */
public class ReportWindow implements Runnable, ButtonOKListener {
    private String title;
    private String message;
    private Stage dialogStage = new Stage();
    private Button buttonOK = new Button("OK");


    ReportWindow(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public void run() {
        dialogStage.setTitle(title);
        dialogStage.centerOnScreen();
        dialogStage.setScene(new Scene(new VBox(getMessageLabel(), getButtonOK()), 300, 100));
        dialogStage.toFront();
        dialogStage.setMaximized(false);
        dialogStage.setResizable(false);
        dialogStage.show();
    }

    @Override
    public void buttonOKListener() {
        buttonOK.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                dialogStage.close();
            }
        });

        buttonOK.setOnMouseClicked(event ->dialogStage.close());
    }

    private Label getMessageLabel() {
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Liberation Serif", FontWeight.LIGHT, 14));
//        messageLabel.setTextAlignment(Pos.CENTER);
        messageLabel.setCenterShape(true);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setPadding(new Insets(20, 0, 0, 20));

        return messageLabel;
    }

    private HBox getButtonOK() {
        HBox buttonOKHBox = new HBox(buttonOK);
        buttonOKHBox.setPadding(new Insets(10, 18, 0, 245));
        buttonOKListener();

        return buttonOKHBox;
    }


}
