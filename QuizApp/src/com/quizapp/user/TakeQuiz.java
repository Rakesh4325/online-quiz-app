package com.quizapp.user;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class TakeQuiz {
	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Step 1: Select quiz
            Statement stmt = conn.createStatement();
            ResultSet quizzes = stmt.executeQuery("SELECT * FROM quizzes");

            System.out.println("Available Quizzes:");
            while (quizzes.next()) {
                System.out.println(quizzes.getInt("id") + ": " + quizzes.getString("title"));
            }

            System.out.print("Enter quiz ID to take: ");
            int quizId = Integer.parseInt(scanner.nextLine());

            // Step 2: Get questions for the quiz
            String getQuestionsSQL = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement questionStmt = conn.prepareStatement(getQuestionsSQL);
            questionStmt.setInt(1, quizId);
            ResultSet questions = questionStmt.executeQuery();

            int score = 0;
            int total = 0;

            while (questions.next()) {
                int questionId = questions.getInt("id");
                String questionText = questions.getString("question_text");
                String correctOption = questions.getString("correct_option");

                System.out.println("\n" + questionText);

                // Get options
                String getOptionsSQL = "SELECT * FROM options WHERE question_id = ?";
                PreparedStatement optionStmt = conn.prepareStatement(getOptionsSQL);
                optionStmt.setInt(1, questionId);
                ResultSet options = optionStmt.executeQuery();

                int optionNumber = 1;
                String[] optionArray = new String[4];

                while (options.next()) {
                    String optionText = options.getString("option_text");
                    optionArray[optionNumber - 1] = optionText;
                    System.out.println(optionNumber + ". " + optionText);
                    optionNumber++;
                }

                System.out.print("Your answer (1-4): ");
                int userChoice = Integer.parseInt(scanner.nextLine());
                String chosenOption = optionArray[userChoice - 1];

                if (chosenOption.equals(correctOption)) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Incorrect! Correct answer: " + correctOption);
                }

                total++;
            }

            // Step 3: Final score
            System.out.println("\nQuiz complete!");
            System.out.println("Your score: " + score + "/" + total);

            // Optional: Store result
            System.out.print("Enter your username to save result: ");
            String username = scanner.nextLine();

            // Get user ID
            String userIdSQL = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userIdSQL);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();

            if (userResult.next()) {
                int userId = userResult.getInt("id");

                String insertResultSQL = "INSERT INTO results (user_id, quiz_id, score, date_taken) VALUES (?, ?, ?, ?)";
                PreparedStatement resultStmt = conn.prepareStatement(insertResultSQL);
                resultStmt.setInt(1, userId);
                resultStmt.setInt(2, quizId);
                resultStmt.setInt(3, score);
                resultStmt.setString(4, LocalDateTime.now().toString());
                resultStmt.executeUpdate();

                System.out.println("Result saved!");
            } else {
                System.out.println("User not found. Score not saved.");
            }

        } catch (Exception e) {
            System.out.println("Error taking quiz: " + e.getMessage());
        }
    }
}

