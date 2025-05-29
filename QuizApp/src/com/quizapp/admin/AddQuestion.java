package com.quizapp.admin;

import java.sql.*;
import java.util.Scanner;

public class AddQuestion {
	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or match the same used in registration

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            Scanner scanner = new Scanner(System.in)
        ) {
         
            Statement stmt = conn.createStatement();
            ResultSet quizzes = stmt.executeQuery("SELECT * FROM quizzes");

            System.out.println("Available Quizzes:");
            while (quizzes.next()) {
                System.out.println(quizzes.getInt("id") + ": " + quizzes.getString("title"));
            }

            System.out.print("Enter quiz ID to add a question: ");
            int quizId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter question text: ");
            String questionText = scanner.nextLine();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter option " + (i + 1) + ": ");
                options[i] = scanner.nextLine();
            }
            System.out.print("Enter number of correct option (1–4): ");
            int correctOptionNumber = Integer.parseInt(scanner.nextLine());
            String correctOption = options[correctOptionNumber - 1];
            String insertQuestion = "INSERT INTO questions (quiz_id, question_text, correct_option) VALUES (?, ?, ?)";
            PreparedStatement questionStmt = conn.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS);
            questionStmt.setInt(1, quizId);
            questionStmt.setString(2, questionText);
            questionStmt.setString(3, correctOption);
            questionStmt.executeUpdate();

            ResultSet rs = questionStmt.getGeneratedKeys();
            int questionId = -1;
            if (rs.next()) {
                questionId = rs.getInt(1);
            }
            String insertOption = "INSERT INTO options (question_id, option_text) VALUES (?, ?)";
            PreparedStatement optionStmt = conn.prepareStatement(insertOption);

            for (String option : options) {
                optionStmt.setInt(1, questionId);
                optionStmt.setString(2, option);
                optionStmt.executeUpdate();
            }

            System.out.println("Question and options added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
