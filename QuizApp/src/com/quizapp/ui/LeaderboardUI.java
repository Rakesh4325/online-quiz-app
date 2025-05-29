// === File: LeaderboardUI.java ===
package com.quizapp.ui;
import com.quizapp.util.SceneManager;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class LeaderboardUI {

    public static Scene getLeaderboardScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label header = new Label("🏆 Leaderboard - Top Scores");
        ComboBox<String> quizSelector = new ComboBox<>();
        TextArea scoreArea = new TextArea();
        scoreArea.setEditable(false);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100\\\\quiz_app.db")) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id, title FROM quizzes");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                quizSelector.getItems().add(id + ": " + title);
            }
        } catch (Exception e) {
            scoreArea.setText("Error loading quizzes: " + e.getMessage());
        }

        quizSelector.setOnAction(e -> {
            scoreArea.clear();
            String selected = quizSelector.getValue();
            if (selected == null) return;

            int quizId = Integer.parseInt(selected.split(":")[0]);
            String query = """
            	    SELECT users.username, results.score
            	    FROM results
            	    JOIN users ON users.id = results.user_id
            	    WHERE results.quiz_id = ?
            	    ORDER BY results.score DESC
            	    LIMIT 10
            	""";


            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100\\\\quiz_app.db")) {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, quizId);
                ResultSet rs = stmt.executeQuery();

                StringBuilder sb = new StringBuilder("Top 10 Scores:\n");
                int rank = 1;
                while (rs.next()) {
                    sb.append(rank++)
                      .append(". ")
                      .append(rs.getString("username"))
                      .append(" - ")
                      .append(rs.getInt("score"))
                      .append("\n");
                }
                scoreArea.setText(sb.toString());
            } catch (Exception ex) {
                scoreArea.setText("Error loading scores: " + ex.getMessage());
            }
        });
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));

        layout.getChildren().addAll(header, quizSelector, scoreArea, backBtn);
        return new Scene(layout, 400, 400);
    }
}
