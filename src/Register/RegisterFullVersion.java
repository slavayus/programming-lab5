package Register;

import commands.ShowAlert;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import old.school.Man;
import old.school.User;

import java.io.IOException;
import java.util.*;

/**
 * Created by slavik on 13.04.17.
 */
public class RegisterFullVersion implements Registerable {
    private List<String> data = new ArrayList<>();

    private boolean flagLogin = false;
    private boolean flagMail = false;


    public void register() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Register Dialog");

        // Set the button types.
        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        TextField password = new TextField();
        password.setPromptText("@mail");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("E-mail:"), 0, 1);
        grid.add(password, 1, 1);
        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(registerButtonType);
        loginButton.setDisable(true);


        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue.trim().isEmpty() && !flagMail);
            flagLogin = !newValue.trim().isEmpty();
        });
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue.trim().isEmpty() && !flagLogin);
            flagMail = !newValue.trim().isEmpty();
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            data.add(usernamePassword.getKey());
            data.add(usernamePassword.getValue());
            if (data.get(0).isEmpty() || data.get(1).isEmpty()) {
                data.clear();
                dialog.close();
//                new ShowAlert(Alert.AlertType.ERROR, "Error", "Заполните все поля", bundle);
                RegisterFullVersion.this.register();
            } else {

                try {
                    Map<String, Man> newData = new LinkedHashMap<>();
                    newData.put(data.get(1), new User(data.get(0), 2));

                    ClientLoad clientLoad = new ClientLoad();
                    clientLoad.send(newData, "REGISTER_FULL");
                    MessageFromClient messageFromClient = clientLoad.readData();

                    switch (messageFromClient.getModifiedRow()) {
                        case 0:
                            data.clear();
                            dialog.close();
//                            new ShowAlert(Alert.AlertType.INFORMATION, "Error", "\n" + messageFromClient.getMsg(), bundle);
                            RegisterFullVersion.this.register();
                            break;
                        case 1:
//                            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "\n" + messageFromClient.getMsg(), bundle);
                            break;
                    }
                } catch (IOException e) {
//                    new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server", bundle);
                }
            }
        });
    }

}
