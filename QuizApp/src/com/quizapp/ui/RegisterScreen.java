package com.quizapp.ui;

import com.quizapp.security.PasswordUtil;
import com.quizapp.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterScreen {

	private static final String DB_URL = "jdbc:sqlite:C:\\\\Users\\\\User\\\\Downloads\\\\sqlite-tools-win-x64-3490100/quiz_app.db";

    public static Scene getRegisterScene() {
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label confirmLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("❌ All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("❌ Passwords do not match.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String checkSQL = "SELECT id FROM users WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    messageLabel.setText("❌ Username already exists.");
                    return;
                }

                String hashed = PasswordUtil.hashPassword(password);
                String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                insertStmt.setString(1, username);
                insertStmt.setString(2, hashed);
                insertStmt.executeUpdate();

                messageLabel.setText("✅ Registration successful!");
            } catch (Exception ex) {
                messageLabel.setText("DB Error: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10,
                userLabel, usernameField,
                passLabel, passwordField,
                confirmLabel, confirmPasswordField,
                registerButton, messageLabel);

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        return new Scene(layout, 300, 350);
    }
}