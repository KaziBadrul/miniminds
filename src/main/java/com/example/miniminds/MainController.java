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
import java.io.InputStream;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    private void showDashboard() {
        contentArea.getChildren().clear();

        // Games Overview ----------------------------
        // Math Game ✅
        // Letter to Image Game ✅
        // Memory Game ✅
        // Odd One Out Game ✅
        // Mini Quizzes ✅
        // Pop the Balloon Game ✅
        // Timed Challenges (Under Development) 🏗️ ADRIT

        // Other Features ----------------------------------
        // Progress Tracking (Game depends on your level) 🏗️ ADRIT
        // Rewards System (Collect stars/badges) 🏗️ ADRIT

        //TODO ARIQ - Make buttons look better

        // Title text
        Text title = new Text("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Math Quiz
        Button mathQuizBtn = new Button("Start Math Quiz");
//        mathQuizBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        mathQuizBtn.getStyleClass().add("dashboard-buttons");
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
        balloonBtn.setOnAction(e -> openGameWindow("pop-balloon.fxml", "Pop the Balloon", 838, 518));

        Button timedBtn = new Button("Timed Challenge");
        timedBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        timedBtn.setOnAction(e -> openGameWindow("timed-challenge.fxml", "Timed Challenge", 700, 500));

        VBox dashboardLayout = new VBox(20, title, mathQuizBtn, memoryGameBtn, letterToImageBtn, oddOneOutBtn, quizzesBtn, balloonBtn, timedBtn);
        dashboardLayout.setAlignment(Pos.CENTER);


        contentArea.getChildren().add(dashboardLayout);

    }

    //TODO: NO PICTURE IN ODDS ONE OUT
    //TODO: MEMORY GAME
    @FXML
    private void showProfile() {
        contentArea.getChildren().clear();

        User user = DatabaseHelper.getUserByEmail(Session.getCurrentUserEmail());

        VBox profileBox = new VBox(25);
        profileBox.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");

        // ===== User Info =====
        ImageView avatar = new ImageView();
        String avatarPath = user.getAge() < 18 ? "/com/example/miniminds/images/kid.png"
                : "/com/example/miniminds/images/adult.png";
        avatar.setImage(new Image(getClass().getResourceAsStream(avatarPath)));
        avatar.setFitHeight(120);
        avatar.setFitWidth(120);
        avatar.setPreserveRatio(true);
        avatar.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        Text nameText = new Text(user.getName());
        nameText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #333;");
        Text emailText = new Text(user.getEmail());
        emailText.setStyle("-fx-font-size: 16px; -fx-fill: #555;");
        Text ageText = new Text("Age: " + user.getAge());
        ageText.setStyle("-fx-font-size: 16px; -fx-fill: #555;");

        VBox userInfoBox = new VBox(5, nameText, emailText, ageText);
        userInfoBox.setStyle("-fx-padding: 10;");

        profileBox.getChildren().addAll(avatar, userInfoBox, createSectionTitle("Game Performance"));

        // ===== Game Performance Columns =====
        HBox gamesColumns = new HBox(50);
        VBox leftCol = new VBox(20);
        VBox rightCol = new VBox(20);

        String[][] games = {
                {"Math Quiz", String.valueOf(user.getMath()), "/com/example/miniminds/images/math.png"},
                {"Letter-to-Image", String.valueOf(user.getSpelling()), "/com/example/miniminds/images/spelling.png"},
                {"Match the Card", String.valueOf(user.getMemory()), "/com/example/miniminds/images/memory.png"},
                {"Odd One Out", String.valueOf(user.getIq()), "/com/example/miniminds/images/iq.png"},
                {"Pop the Balloon", String.valueOf(user.getNumbers()), "/com/example/miniminds/images/numbers.png"},
                {"Timed Challenge", String.valueOf(user.getTimed()), "/com/example/miniminds/images/timed.png"}
        };

        for (int i = 0; i < games.length; i++) {
            VBox gameBar = createModernGameBar(games[i][0], Integer.parseInt(games[i][1]), games[i][2]);
            if (i < 3) leftCol.getChildren().add(gameBar);
            else rightCol.getChildren().add(gameBar);
        }

        gamesColumns.getChildren().addAll(leftCol, rightCol);
        profileBox.getChildren().add(gamesColumns);

        // ===== Average Level =====
        double avgLevel = user.getAverageLevel();
        Text avgText = new Text(String.format("%.1f", avgLevel));
        avgText.setStyle("-fx-font-size: 18px; -fx-fill: #333;");
        ProgressBar avgBar = new ProgressBar(avgLevel / 10.0);
        avgBar.setPrefWidth(300);
        avgBar.setPrefHeight(20);
        avgBar.setStyle(
                "-fx-accent: linear-gradient(to right, #4caf50, #81c784);" +
                        "-fx-background-insets: 0; -fx-background-radius: 10; -fx-border-radius: 10;"
        );

        VBox avgBox = new VBox(5, createSectionTitle("Average Level"), avgText, avgBar);
        avgBox.setStyle("-fx-padding: 10;");
        profileBox.getChildren().add(avgBox);

        contentArea.getChildren().add(profileBox);
    }

    // ===== Helper for section titles =====
    private Text createSectionTitle(String text) {
        Text t = new Text(text);
        t.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #444;");
        return t;
    }

    // ===== Modern Game Bar with Icon and styled ProgressBar =====
    private VBox createModernGameBar(String gameName, int score, String iconPath) {
        HBox container = new HBox(15);
        container.setStyle("-fx-alignment: center-left;");

        ImageView icon = new ImageView();
        InputStream is = getClass().getResourceAsStream(iconPath);
        if (is != null) {
            icon.setImage(new Image(is));
            icon.setFitHeight(30);
            icon.setFitWidth(30);
            icon.setPreserveRatio(true);
            icon.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 1);");
        }

        VBox infoBox = new VBox(3);
        Text label = new Text(gameName + " (Score: " + score + ")");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #333;");

        int currentLevel = score / 20;
        double progress = (score % 20) / 20.0;
        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(200);
        bar.setPrefHeight(15);

        Text levelText = new Text("Level " + currentLevel);
        levelText.setStyle("-fx-font-size: 14px; -fx-fill: #555;");

        infoBox.getChildren().addAll(label, bar, levelText);
        container.getChildren().addAll(icon, infoBox);

        return new VBox(container);
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
        stage.setFullScreen(true);
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
            } else if (controller instanceof PopBalloonController popController) {
                popController.setCurrentUserEmail(Session.getCurrentUserEmail());
            }  else if (controller instanceof MemoryGameController memoryController) {
                memoryController.setCurrentUserEmail(Session.getCurrentUserEmail());
            }

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