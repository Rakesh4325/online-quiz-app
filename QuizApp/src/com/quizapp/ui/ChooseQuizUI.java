
package com.quizapp.ui;

import com.quizapp.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChooseQuizUI {
    public static Scene getChooseQuizScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label title = new Label("Choose a Quiz:");
        ComboBox<QuizItem> quizCombo = new ComboBox<>();
        Button startButton = new Button("Start Quiz");
        Button backButton = new Button("Back to Dashboard");

        ObservableList<QuizItem> quizList = FXCollections.observableArrayList();
        quizCombo.setItems(quizList);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\\\\\\\\\\\\\Users\\\\\\\\\\\\\\\\User\\\\\\\\\\\\\\\\Downloads\\\\\\\\\\\\\\\\sqlite-tools-win-x64-3490100/quiz_app.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, title FROM quizzes");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String titleText = rs.getString("title");
                quizList.add(new QuizItem(id, titleText));
            }
        } catch (Exception e) {
            quizList.clear();
            quizList.add(new QuizItem(-1, "Failed to load quizzes: " + e.getMessage()));
        }

        startButton.setOnAction(e -> {
            QuizItem selected = quizCombo.getValue();
            if (selected != null && selected.id > 0) {
                SceneManager.switchScene(TakeQuizUI.getQuizScene(selected.id));
            }
        });

        backButton.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));

        layout.getChildren().addAll(title, quizCombo, startButton, backButton);
        return new Scene(layout, 400, 250);
    }

    static class QuizItem {
        int id;
        String title;

        QuizItem(int id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
