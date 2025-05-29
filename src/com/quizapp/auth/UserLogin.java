package com.quizapp.auth;

import com.quizapp.security.PasswordUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserLogin {
    private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db"; // Or match the same used in registration

    public static void main(String[] args) {
        String inputUsername = "testuser";
        String inputPassword = "mypassword123";

        String hashedInput = PasswordUtil.hashPassword(inputPassword);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inputUsername);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                if (storedHashedPassword.equals(hashedInput)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Incorrect password.");
                }
            } else {
                System.out.println("User not found.");
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
}
