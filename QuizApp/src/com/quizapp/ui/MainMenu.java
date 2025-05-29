package com.quizapp.ui;

import com.quizapp.util.SceneManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static Scene getMainMenuScene() {
        Button login = new Button("Login");
        Button register = new Button("Register");
        Button exit = new Button("Exit");

        login.setOnAction(e -> SceneManager.switchScene(LoginScreen.getLoginScene()));
        register.setOnAction(e -> SceneManager.switchScene(RegisterScreen.getRegisterScene()));
        exit.setOnAction(e -> SceneManager.getStage().close());

        VBox layout = new VBox(15, login, register, exit);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-alignment: center;");

        return new Scene(layout, 300, 250);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        primaryStage.setTitle("Online Quiz App");
        primaryStage.setScene(getMainMenuScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}