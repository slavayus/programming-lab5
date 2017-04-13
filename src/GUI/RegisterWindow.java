package GUI;

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

/**
 * Created by slavik on 02.04.17.
 */
public class RegisterWindow implements Runnable {

    private PasswordField userPasswordField;
    private Stage primaryStage;
    private VBox rootVBox = new VBox();
    private HBox rootHBox = new HBox();

    public RegisterWindow(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void run() {
        rootHBox.setAlignment(Pos.CENTER);
        rootHBox.setStyle("-fx-background-color: #3FF4CB;");


        rootVBox.setStyle("-fx-background-color: #3FF4CB;");
        rootVBox.setAlignment(Pos.CENTER_LEFT);

        rootHBox.getChildren().add(rootVBox);
        rootVBox.getChildren().addAll(showWelcomeHBox(), showUserNameHBox(), showUserPasswordHBox(), showSubmitButtonHBox());

        primaryStage.setTitle("Work with Collection");
        primaryStage.setMinHeight(290);
        primaryStage.setMinWidth(340);
        primaryStage.setScene(new Scene(rootHBox, 360, 280));
        primaryStage.show();
    }

    private HBox showSubmitButtonHBox() {
        HBox submitButtonHBox = new HBox();
        submitButtonHBox.setPadding(new Insets(0, 10, 20, 35));
        submitButtonHBox.setSpacing(100);

        Button submitButton = new Button("Submit");
        submitButtonHBox.getChildren().addAll(showColorPicker(),submitButton);

        submitButtonListener(submitButton);

        return submitButtonHBox;
    }

    private void submitButtonListener(Button submitButton) {
        submitButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                primaryStage.close();
                new DialogWindow().run();
            }
        });

        submitButton.setOnMouseClicked(event ->{
            primaryStage.close();
            new DialogWindow().run();
                });
    }

    private HBox showUserPasswordHBox() {
        HBox userPasswordHBox = new HBox();
        userPasswordHBox.setPadding(new Insets(10, 10, 15, 35));
        userPasswordHBox.setSpacing(40);

        Label userPassword = new Label("Password:");
        userPassword.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        userPasswordField = new PasswordField();
        userPasswordField.setPromptText("Enter your Password");

        userPasswordHBox.getChildren().addAll(userPassword, userPasswordField);

        return userPasswordHBox;
    }

    private HBox showUserNameHBox() {
        HBox userNameHBox = new HBox();
        userNameHBox.setPadding(new Insets(10, 10, 20, 35));
        userNameHBox.setSpacing(28);

        Label userName = new Label("User Name:");
        userName.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));

        TextField userNameTextField = new TextField();
        userNameTextField.setPromptText("Enter your Name");

        userNameHBox.getChildren().addAll(userName, userNameTextField);

        return userNameHBox;
    }

    private HBox showWelcomeHBox() {
        HBox welcomeHBox = new HBox();
        welcomeHBox.setPadding(new Insets(20, 0, 15, 35));
        welcomeHBox.setSpacing(30);

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setFont(Font.font("Liberation Serif", FontWeight.LIGHT, 34));
        welcomeLabel.setPadding(new Insets(20,0,0,0));


        Image image = new Image("./images/register/signup.jpg");
        ImageView imageView = new ImageView(image);

        welcomeHBox.getChildren().addAll(welcomeLabel,imageView);
        return welcomeHBox;
    }

    private ColorPicker showColorPicker(){
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
