package com.quizapp.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestSQLite {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\User\\Downloads\\sqlite-tools-win-x64-3490100/quiz_app.db";  // Update path as needed

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite!");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
