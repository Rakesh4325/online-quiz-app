package com.quizapp.ui;

import com.quizapp.util.AppContext;
import com.quizapp.util.SceneManager;
import com.quizapp.security.PasswordUtil;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen {

    private static final String DB_URL = "jdbc:sqlite:C:\\\\\\\\Users\\\\\\\\User\\\\\\\\Downloads\\\\\\\\sqlite-tools-win-x64-3490100\\\\\\\\quiz_app.db";

    public static Scene getLoginScene() {
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String hashed = PasswordUtil.hashPassword(password);

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT password, role FROM users WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");
                    if (storedHash.equals(hashed)) {
                        AppContext.setCurrentUser(username);
                        AppContext.setRole(rs.getString("role"));
                        SceneManager.switchScene(UserDashboard.getDashboardScene());
                    } else {
                        messageLabel.setText("❌ Incorrect password.");
                    }
                } else {
                    messageLabel.setText("❌ User not found.");
                }
            } catch (Exception ex) {
                messageLabel.setText("DB Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        

        VBox layout = new VBox(10,
                userLabel, usernameField,
                passLabel, passwordField,
                loginButton, messageLabel);

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        return new Scene(layout, 300, 300);
    }
}
