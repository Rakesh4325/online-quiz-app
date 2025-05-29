// === File: ViewScoresUI.java ===
package com.quizapp.ui;

import com.quizapp.util.AppContext;
import com.quizapp.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewScoresUI {
    public static Scene getScoreScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("Past Quiz Scores for: " + AppContext.getCurrentUser());
        TextArea scoreArea = new TextArea();
        scoreArea.setEditable(false);
        Button backButton = new Button("Back to Dashboard");

        layout.getChildren().addAll(title, scoreArea, backButton);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\\\\\Users\\\\\\\\User\\\\\\\\Downloads\\\\\\\\sqlite-tools-win-x64-3490100/quiz_app.db")) {
            PreparedStatement userStmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            userStmt.setString(1, AppContext.getCurrentUser());
            ResultSet userRs = userStmt.executeQuery();
            if (userRs.next()) {
                int userId = userRs.getInt("id");

                String query = "SELECT r.quiz_id, q.title, r.score, r.date_taken FROM results r " +
                               "JOIN quizzes q ON r.quiz_id = q.id WHERE r.user_id = ? ORDER BY r.date_taken DESC";

                PreparedStatement resultStmt = conn.prepareStatement(query);
                resultStmt.setInt(1, userId);
                ResultSet results = resultStmt.executeQuery();

                StringBuilder sb = new StringBuilder();
                while (results.next()) {
                    String quizTitle = results.getString("title");
                    int score = results.getInt("score");
                    String date = results.getString("date_taken");
                    sb.append("Quiz: ").append(quizTitle)
                      .append(" | Score: ").append(score)
                      .append(" | Date: ").append(date)
                      .append("\n");
                }

                scoreArea.setText(sb.toString());
            }
        } catch (Exception e) {
            scoreArea.setText("Error loading scores: " + e.getMessage());
        }

        backButton.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));

        return new Scene(layout, 450, 300);
    }
}
