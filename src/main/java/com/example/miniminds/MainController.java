package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        // Math Game (Under Development) 🏗️
        // Letter to Image Game (Under Development) 🏗️
        // Memory Card Game
        // Odd One Out Game (Under Development) 🏗️
        // Mini Quizzes
        // Pop the Balloon Game
        // Timed Challenges

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
        quizzesBtn.setOnAction(e -> openGameWindow("mini-quiz.fxml", "Mini Quizzes", 650, 450));

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
        contentArea.getChildren().add(new Text("Profile content goes here."));
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
            VBox gameRoot = loader.load();

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