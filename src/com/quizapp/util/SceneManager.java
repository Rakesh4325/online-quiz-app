package com.quizapp.util;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }
}