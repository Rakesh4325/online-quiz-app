package com.quizapp.user;

import java.sql.*;
import java.util.Scanner;

public class ViewPastScores {
	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Get user ID
            String userSQL = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userSQL);
            userStmt.setString(1, username);
            ResultSet userResult = userStmt.executeQuery();

            if (!userResult.next()) {
                System.out.println("User not found.");
                return;
            }

            int userId = userResult.getInt("id");
            String resultSQL = """
                SELECT q.title, r.score, r.date_taken
                FROM results r
                JOIN quizzes q ON r.quiz_id = q.id
                WHERE r.user_id = ?
                ORDER BY r.date_taken DESC
            """;

            PreparedStatement resultStmt = conn.prepareStatement(resultSQL);
            resultStmt.setInt(1, userId);
            ResultSet results = resultStmt.executeQuery();

            System.out.println("\nYour Quiz History:");
            while (results.next()) {
                System.out.println("Quiz: " + results.getString("title")
                        + " | Score: " + results.getInt("score")
                        + " | Date: " + results.getString("date_taken"));
            }

        } catch (Exception e) {
            System.out.println("Error retrieving scores: " + e.getMessage());
        }
    }
}
