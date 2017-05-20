package commands;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.PropertyResourceBundle;

/**
 * Created by slavik on 13.04.17.
 */
public class ShowAlert {
    private Alert.AlertType alertType;
    private String title;
    private String message;

    public ShowAlert(Alert.AlertType alertType, String title, String message, PropertyResourceBundle bundle) {
        this.alertType = alertType;
        this.title = title;
        this.message = message;
        showAlert(bundle);
    }

    private void showAlert(PropertyResourceBundle bundle){
        Alert alert =new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);
        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText(bundle.getString("message.button.OK"));

        alert.setContentText(message);
        alert.showAndWait();
    }
}
