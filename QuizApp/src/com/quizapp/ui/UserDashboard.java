package com.quizapp.ui;
import javafx.scene.control.Label;


import com.quizapp.util.AppContext;
import com.quizapp.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class UserDashboard {
	public static Scene getDashboardScene() {
	    VBox layout = new VBox(15);
	    layout.setPadding(new Insets(20));

	    Label welcomeLabel = new Label("Welcome, " + AppContext.getCurrentUser());

	    Button takeQuiz = new Button("Take Quiz");
	    takeQuiz.setOnAction(e -> SceneManager.switchScene(ChooseQuizUI.getChooseQuizScene()));

	    Button viewScores = new Button("View Past Scores");
	    viewScores.setOnAction(e -> SceneManager.switchScene(ViewScoresUI.getScoreScene()));

	    Button logoutBtn = new Button("Logout");
	    logoutBtn.setOnAction(e -> {
	        AppContext.clear();
	        SceneManager.switchScene(LoginScreen.getLoginScene());
	    });


	    layout.getChildren().addAll(welcomeLabel, takeQuiz, viewScores, logoutBtn);
	    if ("admin".equals(AppContext.getRole())) {
	        Button adminBtn = new Button("Admin Panel");
	        adminBtn.setOnAction(e -> SceneManager.switchScene(AdminPanelUI.getAdminPanelScene()));
	        layout.getChildren().add(adminBtn);
	    }
	    Button leaderboardBtn = new Button("View Leaderboard");
	    leaderboardBtn.setOnAction(e -> SceneManager.switchScene(LeaderboardUI.getLeaderboardScene()));
	    layout.getChildren().add(leaderboardBtn);


	    return new Scene(layout, 400, 300);
	}

    
}
