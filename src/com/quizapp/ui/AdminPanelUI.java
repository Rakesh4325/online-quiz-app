
package com.quizapp.ui;

import com.quizapp.util.AppContext;
import com.quizapp.util.SceneManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPanelUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(getAdminPanelScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getAdminPanelScene() {
        if (!"admin".equals(AppContext.getRole())) {
            Label warning = new Label("Access denied. Admins only.");
            Button backBtn = new Button("Back to Dashboard");
            backBtn.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));
            VBox layout = new VBox(20, warning, backBtn);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-alignment: center;");
            return new Scene(layout, 350, 150);
        }

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label header = new Label("Admin Panel - Create Quiz and Add Questions");

        TextField quizTitleField = new TextField();
        quizTitleField.setPromptText("Enter quiz title");
        Button createQuizBtn = new Button("Create Quiz");

        TextField questionField = new TextField();
        questionField.setPromptText("Enter question text");

        TextField option1 = new TextField("Option A");
        TextField option2 = new TextField("Option B");
        TextField option3 = new TextField("Option C");
        TextField option4 = new TextField("Option D");

        ComboBox<String> correctOption = new ComboBox<>();
        correctOption.getItems().addAll("Option A", "Option B", "Option C", "Option D");

        Button addQuestionBtn = new Button("Add Question");
        Label resultLabel = new Label();

        final int[] createdQuizId = {-1};

        createQuizBtn.setOnAction(e -> {
            String title = quizTitleField.getText().trim();
            if (title.isEmpty()) {
                resultLabel.setText("Quiz title cannot be empty.");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100\\\\quiz_app.db")) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO quizzes (title) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, title);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    createdQuizId[0] = rs.getInt(1);
                    resultLabel.setText("✅ Quiz created. Now add questions.");
                }
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
            }
        });

        addQuestionBtn.setOnAction(e -> {
            if (createdQuizId[0] == -1) {
                resultLabel.setText("Create a quiz first.");
                return;
            }
            String qText = questionField.getText().trim();
            String cOpt = correctOption.getValue();
            if (qText.isEmpty() || cOpt == null) {
                resultLabel.setText("Fill all fields and select correct option.");
                return;
            }

            String[] options = {option1.getText().trim(), option2.getText().trim(), option3.getText().trim(), option4.getText().trim()};
            String correct = switch (cOpt) {
                case "Option A" -> options[0];
                case "Option B" -> options[1];
                case "Option C" -> options[2];
                case "Option D" -> options[3];
                default -> "";
            };

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100\\\\quiz_app.db")) {
                PreparedStatement qStmt = conn.prepareStatement("INSERT INTO questions (quiz_id, question_text, correct_option) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                qStmt.setInt(1, createdQuizId[0]);
                qStmt.setString(2, qText);
                qStmt.setString(3, correct);
                qStmt.executeUpdate();

                ResultSet rs = qStmt.getGeneratedKeys();
                if (rs.next()) {
                    int questionId = rs.getInt(1);
                    PreparedStatement oStmt = conn.prepareStatement("INSERT INTO options (question_id, option_text) VALUES (?, ?)");
                    for (String opt : options) {
                        oStmt.setInt(1, questionId);
                        oStmt.setString(2, opt);
                        oStmt.addBatch();
                    }
                    oStmt.executeBatch();
                }

                resultLabel.setText("✅ Question added.");
                questionField.clear();
                option1.clear();
                option2.clear();
                option3.clear();
                option4.clear();
                correctOption.getSelectionModel().clearSelection();

            } catch (Exception ex) {
                resultLabel.setText("Error adding question: " + ex.getMessage());
            }
        });

        Button back = new Button("Back to Dashboard");
        back.setOnAction(e -> SceneManager.switchScene(UserDashboard.getDashboardScene()));

        layout.getChildren().addAll(
                header,
                quizTitleField, createQuizBtn,
                questionField, option1, option2, option3, option4,
                new Label("Correct Option:"), correctOption,
                addQuestionBtn, resultLabel, back);

        return new Scene(layout, 450, 500);
    }
}
