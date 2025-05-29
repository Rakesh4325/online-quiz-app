package com.quizapp.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddQuiz {
	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or match the same used in registration // Update if needed

    public static void main(String[] args) {
        String quizTitle = "Java Basics";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO quizzes (title) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, quizTitle);
            pstmt.executeUpdate();

            System.out.println("Quiz added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding quiz: " + e.getMessage());
        }
    }
}
