package com.quizapp.admin;

import java.sql.*;

public class Leaderboard {
	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = """
                SELECT u.username, q.title, MAX(r.score) AS top_score
                FROM results r
                JOIN users u ON r.user_id = u.id
                JOIN quizzes q ON r.quiz_id = q.id
                GROUP BY r.quiz_id, r.user_id
                ORDER BY top_score DESC
                LIMIT 10
            """;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("🏆 Leaderboard (Top 10):");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("username")
                        + " | Quiz: " + rs.getString("title")
                        + " | Score: " + rs.getInt("top_score"));
            }

        } catch (Exception e) {
            System.out.println("Error generating leaderboard: " + e.getMessage());
        }
    }
}
