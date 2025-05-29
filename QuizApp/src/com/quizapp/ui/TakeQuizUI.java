package com.quizapp.ui;

import com.quizapp.util.AppContext;
import com.quizapp.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TakeQuizUI {
    public static Scene getQuizScene(int quizId) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label questionLabel = new Label("Loading question...");
        ToggleGroup optionsGroup = new ToggleGroup();
        VBox optionsBox = new VBox(5);
        Button nextButton = new Button("Next");
        Label feedbackLabel = new Label();

        layout.getChildren().addAll(questionLabel, optionsBox, nextButton, feedbackLabel);

        List<Question> questions = new ArrayList<>();
        final int[] currentIndex = {0};
        final int[] score = {0};

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\\\\\\\\\\\\\Users\\\\\\\\\\\\\\\\User\\\\\\\\\\\\\\\\Downloads\\\\\\\\\\\\\\\\sqlite-tools-win-x64-3490100/quiz_app.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE quiz_id = ?");
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int qid = rs.getInt("id");
                String qtext = rs.getString("question_text");
                String correct = rs.getString("correct_option");

                PreparedStatement optStmt = conn.prepareStatement("SELECT option_text FROM options WHERE question_id = ?");
                optStmt.setInt(1, qid);
                ResultSet opts = optStmt.executeQuery();
                List<String> optionList = new ArrayList<>();
                while (opts.next()) {
                    optionList.add(opts.getString("option_text"));
                }
                questions.add(new Question(qtext, optionList, correct));
            }
        } catch (Exception e) {
            questionLabel.setText("Failed to load questions: " + e.getMessage());
            return new Scene(layout, 400, 300);
        }

        final Runnable[] showQuestion = new Runnable[1];
        showQuestion[0] = () -> {
            if (currentIndex[0] >= questions.size()) {
                questionLabel.setText("Quiz complete! Score: " + score[0] + "/" + questions.size());
                optionsBox.getChildren().clear();
                nextButton.setText("Back to Dashboard");

                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\\\\\\\\\\\\\Users\\\\\\\\\\\\\\\\User\\\\\\\\\\\\\\\\Downloads\\\\\\\\\\\\\\\\sqlite-tools-win-x64-3490100/quiz_app.db")) {
                    PreparedStatement userStmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
                    userStmt.setString(1, AppContext.getCurrentUser());
                    ResultSet userRs = userStmt.executeQuery();
                    if (userRs.next()) {
                        int userId = userRs.getInt("id");
                        PreparedStatement resultStmt = conn.prepareStatement(
                                "INSERT INTO results (user_id, quiz_id, score, date_taken) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
                        resultStmt.setInt(1, userId);
                        resultStmt.setInt(2, quizId);
                        resultStmt.setInt(3, score[0]);
                        resultStmt.executeUpdate();
                    }
                } catch (Exception e) {
                    System.out.println("Failed to save result: " + e.getMessage());
                }

                nextButton.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));
                return;
            }

            Question q = questions.get(currentIndex[0]);
            questionLabel.setText((currentIndex[0] + 1) + ". " + q.text);
            optionsBox.getChildren().clear();
            optionsGroup.getToggles().clear();
            feedbackLabel.setText("");

            for (String opt : q.options) {
                RadioButton rb = new RadioButton(opt);
                rb.setToggleGroup(optionsGroup);
                optionsBox.getChildren().add(rb);
            }

            nextButton.setOnAction(e -> {
                RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
                if (selected == null) {
                    feedbackLabel.setText("Please select an answer.");
                    return;
                }

                if (selected.getText().equals(q.correct)) {
                    score[0]++;
                }

                currentIndex[0]++;
                showQuestion[0].run();
            });
        };

        showQuestion[0].run();
        return new Scene(layout, 500, 350);
    }

    static class Question {
        String text;
        List<String> options;
        String correct;

        Question(String text, List<String> options, String correct) {
            this.text = text;
            this.options = options;
            this.correct = correct;
        }
    }
}