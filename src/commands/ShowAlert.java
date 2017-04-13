package commands;

import javafx.scene.control.Alert;

/**
 * Created by slavik on 13.04.17.
 */
public class ShowAlert {
    private Alert.AlertType alertType;
    private String title;
    private String message;

    public ShowAlert(Alert.AlertType alertType, String title, String message) {
        this.alertType = alertType;
        this.title = title;
        this.message = message;
        showAlert();
    }

    private void showAlert(){
        Alert alert =new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
