package com.quizapp.dbtest;

import java.sql.*;

public class PrintUsers {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\User\\Downloads\\sqlite-tools-win-x64-3490100\\quiz_app.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, role, password FROM users");

            System.out.println("=== Users in Database ===");
            while (rs.next()) {
                String username = rs.getString("username");
                String role = rs.getString("role");
                String password = rs.getString("password");

                System.out.println("User: " + username + ", Role: " + role + ", Password: " + password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
