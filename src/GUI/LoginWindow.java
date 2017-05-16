package GUI;

import Register.RegisterLimitedVersion;
import Register.RegisterFullVersion;
import Register.Registerable;
import commands.ShowAlert;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import old.school.Man;
import old.school.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by slavik on 02.04.17.
 */
public class LoginWindow implements Runnable {

    private PasswordField userPasswordField;
    private TextField userNameTextField;
    private Stage primaryStage;
    private VBox rootVBox = new VBox();
    private HBox rootHBox = new HBox();
    private Label messageLabel = new Label();
    private Version version = Version.LIMITED;

    public LoginWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void run() {
        rootHBox.setAlignment(Pos.CENTER);
        rootHBox.setStyle("-fx-background-color: #3FF4CB;");


        rootVBox.setStyle("-fx-background-color: #3FF4CB;");
        rootVBox.setAlignment(Pos.CENTER_LEFT);

        rootHBox.getChildren().add(rootVBox);
        rootVBox.getChildren().addAll(
                showWelcomeHBox(),
                showUserNameHBox(),
                showUserPasswordHBox(),
                getMessageHBox(),
                showSubmitButtonHBox(),
                getAccount(),
                getFullVersion());

        primaryStage.setTitle("Work with Collection");
        primaryStage.setMinHeight(360);
        primaryStage.setMinWidth(405);
        primaryStage.setScene(new Scene(rootHBox, 405, 360));
        primaryStage.show();
    }

    private HBox showSubmitButtonHBox() {
        HBox submitButtonHBox = new HBox();
        submitButtonHBox.setPadding(new Insets(0, 10, 20, 35));
        submitButtonHBox.setSpacing(100);

        Button submitButton = new Button("Submit");
        submitButtonHBox.getChildren().addAll(showColorPicker(), submitButton);

        submitButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                submitButtonListener();
            }
        });

        submitButton.setOnMouseClicked(event -> submitButtonListener());

        return submitButtonHBox;
    }

    private void submitButtonListener() {
        if (checkInputData()) {
            primaryStage.close();
            if (Version.FULL == version) {
                alertVersion("You have full version");
            } else {
                alertVersion("You have limited version");
            }
            new MainWindow(version).showWindow();
        } else {
            messageLabel.setText("Failed username or password");
        }
    }

    private void alertVersion(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Done");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkInputData() {
        try {
            Map<String, Man> newData = new LinkedHashMap<>();
            newData.put(userPasswordField.getText(), new User(userNameTextField.getText(), 1));

            ClientLoad clientLoad = new ClientLoad();

            if (!clientLoad.createNewConnection()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not connect to server");

                alert.getButtonTypes().clear();
                alert.getButtonTypes().add(ButtonType.OK);
                Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                yesButton.setOnAction(event -> System.out.println(9));

                alert.showAndWait();
                System.exit(0);
            }

            clientLoad.send(newData, "LOGIN");
            MessageFromClient messageFromClient = clientLoad.readData();

            if (messageFromClient.getModifiedRow() == 1) {
                version = Boolean.parseBoolean(messageFromClient.getMsg()) ? Version.FULL : Version.LIMITED;
                return true;
            }
            return false;
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
            return false;
        }
    }

    private HBox showUserPasswordHBox() {
        HBox userPasswordHBox = new HBox();
        userPasswordHBox.setPadding(new Insets(10, 10, 5, 35));
        userPasswordHBox.setSpacing(40);

        Label userPassword = new Label("Password:");
        userPassword.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        userPasswordField = new PasswordField();
        userPasswordField.setPromptText("Enter your Password");
        userPasswordField.setOnKeyPressed(event -> messageLabel.setText(""));
        userPasswordHBox.getChildren().addAll(userPassword, userPasswordField);

        return userPasswordHBox;
    }

    private HBox showUserNameHBox() {
        HBox userNameHBox = new HBox();
        userNameHBox.setPadding(new Insets(5, 10, 0, 35));
        userNameHBox.setSpacing(28);

        Label userName = new Label("User Name:");
        userName.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        userNameTextField = new TextField();
        userNameTextField.setPromptText("Enter your Name");
        userNameTextField.setOnKeyPressed(event -> messageLabel.setText(""));

        userNameHBox.getChildren().addAll(userName, userNameTextField);

        return userNameHBox;
    }

    private HBox showWelcomeHBox() {
        HBox welcomeHBox = new HBox();
        welcomeHBox.setPadding(new Insets(20, 0, 15, 35));
        welcomeHBox.setSpacing(30);

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setFont(Font.font("Liberation Serif", FontWeight.LIGHT, 34));
        welcomeLabel.setPadding(new Insets(20, 0, 0, 0));


        URL resource = LoginWindow.class.getResource("/images/register/signup.jpg");
        String path = resource.toExternalForm();
//        System.out.println(path);
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        welcomeHBox.getChildren().addAll(welcomeLabel, imageView);
        return welcomeHBox;
    }

    private HBox getAccount() {
        HBox accountHBox = new HBox();
        accountHBox.setPadding(new Insets(0, 0, 0, 172));
        accountHBox.setSpacing(100);

        Hyperlink register = new Hyperlink("Don't have an account?");

        register.setOnAction(event -> {
            Registerable registerAccount = new RegisterLimitedVersion();
            registerAccount.register();
        });

        accountHBox.getChildren().add(register);

        return accountHBox;
    }

    private HBox getFullVersion() {
        HBox fullVersionHBox = new HBox();
        fullVersionHBox.setPadding(new Insets(0, 0, 0, 143));
        fullVersionHBox.setSpacing(100);

        Hyperlink register = new Hyperlink("Want to get the full version?");

        register.setOnAction(event -> {
            Registerable registerAccount = new RegisterFullVersion();
            registerAccount.register();
        });

        fullVersionHBox.getChildren().add(register);

        return fullVersionHBox;
    }

    private HBox getMessageHBox() {
        messageLabel.setStyle("-fx-text-fill: red;");

        HBox messageHBox = new HBox(messageLabel);
        messageHBox.setPadding(new Insets(5, 0, 10, 150));
        messageHBox.setSpacing(100);

        return messageHBox;
    }

    private ColorPicker showColorPicker() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            Paint fill = colorPicker.getValue();
            BackgroundFill backgroundFill =
                    new BackgroundFill(fill,
                            CornerRadii.EMPTY,
                            Insets.EMPTY);
            Background background = new Background(backgroundFill);
            rootHBox.setBackground(background);
            rootVBox.setBackground(background);
        });
        return colorPicker;
    }
}
