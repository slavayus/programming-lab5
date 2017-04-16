package Register;

import commands.ShowAlert;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by slavik on 13.04.17.
 */
public class RegisterAccount extends RegisterWindow implements Registerable {
    private boolean flagLogin = false;
    private boolean flagPassword = false;


    @Override
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
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(registerButtonType);
        loginButton.setDisable(true);


        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue.trim().isEmpty() && !flagPassword);
            flagLogin = !newValue.trim().isEmpty();
        });
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue.trim().isEmpty() && !flagLogin);
            flagPassword = !newValue.trim().isEmpty();
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
                new ShowAlert(Alert.AlertType.ERROR, "Error", "Заполните все поля");
                register();
            } else {
                if (hasAccount(fileNameLimitedEdition)) {
                    data.clear();
                    dialog.close();
                    new ShowAlert(Alert.AlertType.ERROR, "Error", "Пользователь с такими данными \nуже существует");
                    register();
                } else {
                    if (hasAccount(fileNameFullVersion)) {
                        data.clear();
                        dialog.close();
                        new ShowAlert(Alert.AlertType.ERROR, "Error", "Пользователь с такими данными \nуже существует");
                        register();
                    } else {
                        saveToFile(fileNameLimitedEdition);
                        new ShowAlert(Alert.AlertType.INFORMATION, "Done", "Пользователь добавлен");
                    }
                }
            }
        });
    }

}
