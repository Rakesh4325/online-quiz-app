package com.quizapp.auth;

import com.quizapp.security.PasswordUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UserRegistration {
	private static final String DB_URL = "jdbc:sqlite:C:\\Users\\User\\Downloads\\sqlite-tools-win-x64-3490100/quiz_app.db";

    public static void main(String[] args) {
        String username = "testuser";
        String rawPassword = "mypassword123";

        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();

            System.out.println("User registered successfully.");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }
}
