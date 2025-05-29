package com.quizapp.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\User\\Downloads\\sqlite-tools-win-x64-3490100/quiz_app.db";

    public static void main(String[] args) {
        createTables();
    }

    public static void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

           stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS quizzes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS questions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    quiz_id INTEGER NOT NULL,
                    question_text TEXT NOT NULL,
                    correct_option TEXT NOT NULL,
                    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
                );
            """);

           stmt.execute("""
                CREATE TABLE IF NOT EXISTS options (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    question_id INTEGER NOT NULL,
                    option_text TEXT NOT NULL,
                    FOREIGN KEY (question_id) REFERENCES questions(id)
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS results (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    quiz_id INTEGER NOT NULL,
                    score INTEGER NOT NULL,
                    date_taken TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
                );
            """);

            System.out.println("All tables created (if not existing).");

        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
