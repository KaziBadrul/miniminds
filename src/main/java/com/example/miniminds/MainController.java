package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    private void showDashboard() {
        contentArea.getChildren().clear();

        // Games Overview
        // Math Game ✅
        // Letter to Image Game ✅
        // Memory Card Game (Under Development) 🏗️ ARIQ
        // Odd One Out Game ✅
        // Mini Quizzes (Under Development) 🏗️ ARIQ
        // Pop the Balloon Game (Need Design) 👗 ADRIT
        // Timed Challenges (Under Development) 🏗️ ADRIT

        // Other Features
        // Progress Tracking
        // Rewards System

        // Title text
        Text title = new Text("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Math Quiz
        Button mathQuizBtn = new Button("Start Math Quiz");
        mathQuizBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        mathQuizBtn.setOnAction(e -> openGameWindow("math-quiz.fxml", "Math Quiz", 600, 400));

        Button memoryGameBtn = new Button("Play Memory Game");
        memoryGameBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        memoryGameBtn.setOnAction(e -> openGameWindow("memory-game.fxml", "Memory Game", 800, 600));

        Button letterToImageBtn = new Button("Play Letter-To-Image");
        letterToImageBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        letterToImageBtn.setOnAction(e -> openGameWindow("letter-to-image.fxml", "Letter To Image", 800, 600));

        Button oddOneOutBtn = new Button("Play Odd One Out");
        oddOneOutBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        oddOneOutBtn.setOnAction(e -> openGameWindow("odd-one-out.fxml", "Odd One Out", 700, 500));

        Button quizzesBtn = new Button("Mini Quizzes");
        quizzesBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        quizzesBtn.setOnAction(e -> openGameWindow("mini-quiz.fxml", "Mini Quizzes", 700, 550));

        Button balloonBtn = new Button("Pop the Balloon");
        balloonBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        balloonBtn.setOnAction(e -> openGameWindow("pop-balloon.fxml", "Pop the Balloon", 700, 500));

        Button timedBtn = new Button("Timed Challenge");
        timedBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        timedBtn.setOnAction(e -> openGameWindow("timed-challenge.fxml", "Timed Challenge", 700, 500));

        VBox dashboardLayout = new VBox(20, title, mathQuizBtn, memoryGameBtn, letterToImageBtn, oddOneOutBtn, quizzesBtn, balloonBtn, timedBtn);
        dashboardLayout.setAlignment(Pos.CENTER);


        contentArea.getChildren().add(dashboardLayout);

    }

    @FXML
    private void showProfile() {
        contentArea.getChildren().clear();

        User user = DatabaseHelper.getUserByEmail(Session.getCurrentUserEmail());

        VBox profileBox = new VBox(20);
        profileBox.setStyle("-fx-padding: 20; -fx-background-color: #f0f8ff;");

        // ===== User Info =====
        ImageView avatar = new ImageView();
        String avatarPath = user.getAge() < 18 ? "/com/example/miniminds/images/kid.png"
                : "/com/example/miniminds/images/adult.png";
        avatar.setImage(new Image(getClass().getResourceAsStream(avatarPath)));
        avatar.setFitHeight(120);
        avatar.setFitWidth(120);
        avatar.setPreserveRatio(true);

        Text nameText = new Text("Name: " + user.getName());
        Text emailText = new Text("Email: " + user.getEmail());
        Text ageText = new Text("Age: " + user.getAge());

        VBox userInfoBox = new VBox(5, nameText, emailText, ageText);
        userInfoBox.setStyle("-fx-padding: 10;");

        profileBox.getChildren().addAll(avatar, userInfoBox, new Text("Game Performance:"));

        // ===== Game Performance Bars =====
        profileBox.getChildren().addAll(
                createGameLevelBar("Math Quiz", user.getMath()),
                createGameLevelBar("Letter-to-Image", user.getSpelling()),
                createGameLevelBar("Memory Card", user.getMemory()),
                createGameLevelBar("Odd One Out", user.getIq()),
                createGameLevelBar("Number Game", user.getNumbers()),
                createGameLevelBar("Timed Challenge", user.getTimed())
        );

        // ===== Average Level =====
        double avgLevel = user.getAverageLevel();
        Text avgText = new Text("Average Level: " + String.format("%.1f", avgLevel));
        ProgressBar avgBar = new ProgressBar(avgLevel / 10.0); // normalize 0–1
        avgBar.setPrefWidth(300);
        avgBar.setStyle("-fx-accent: #2196f3; -fx-pref-height: 20px;"); // blue bar

        VBox avgBox = new VBox(5, new Text("Average Level:"), avgText, avgBar);
        avgBox.setStyle("-fx-padding: 10;");

        profileBox.getChildren().add(avgBox);

        contentArea.getChildren().add(profileBox);
    }

    // ===== Helper to create game bars =====
    private VBox createGameLevelBar(String gameName, int score) {
        VBox box = new VBox(5);

        // Game label
        Text label = new Text(gameName + " (Score: " + score + ")");

        // Level calculation
        int currentLevel = score / 20;
        double progress = (score % 20) / 20.0; // progress toward next level (0–1)

        // Progress bar
        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(300);
        bar.setStyle("-fx-accent: #4caf50; -fx-pref-height: 20px;"); // green

        // Level text
        Text levelText = new Text("Level " + currentLevel);

        box.getChildren().addAll(label, bar, levelText);
        return box;
    }


    @FXML
    private void showSettings() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Text("Settings content goes here."));
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        System.out.println("User logged out.");
    }

    private void openGameWindow(String fxmlFile, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent gameRoot = loader.load();

            // Loading email
            Object controller = loader.getController();
            if (controller instanceof MathQuizController mathController) {
                mathController.setCurrentUserEmail(Session.getCurrentUserEmail());
            } else if (controller instanceof OddOneOutController oddController) {
                oddController.setCurrentUserEmail(Session.getCurrentUserEmail());
            } else if (controller instanceof LetterToImageController letterController) {
                letterController.setCurrentUserEmail(Session.getCurrentUserEmail());
            } // Add other controllers as needed

            // Scene with CSS
            Scene gameScene = new Scene(gameRoot, width, height);
            gameScene.getStylesheets().add(getClass().getResource("styleGames.css").toExternalForm());

            // Create Stage and show
            Stage gameStage = new Stage();
            gameStage.setTitle("MiniMinds - " + title);
            gameStage.setScene(gameScene);
            gameStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}