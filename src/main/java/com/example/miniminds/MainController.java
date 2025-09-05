package com.example.miniminds;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.effect.DropShadow;

import java.io.IOException;
import java.io.InputStream;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    private void showDashboard() {
        contentArea.getChildren().clear();

        // Title
        Text title = new Text("Dashboard");
        title.getStyleClass().add("dashboard-title");

        // Math Quiz
        ImageView mathIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/math.png")));
        mathIcon.setFitWidth(50);
        mathIcon.setFitHeight(50);
        Button mathQuizBtn = new Button("Start Math Quiz", mathIcon);
        mathQuizBtn.getStyleClass().addAll("dashboard-buttons", "math-quiz-btn");
        mathQuizBtn.setContentDisplay(ContentDisplay.LEFT);
        mathQuizBtn.setOnAction(e -> openGameWindow("math-quiz.fxml", "Math Quiz", 600, 400));

        // Memory Game
        ImageView memoryIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/memory-game.png")));
        memoryIcon.setFitWidth(50);
        memoryIcon.setFitHeight(50);
        Button memoryGameBtn = new Button("Play Memory Game", memoryIcon);
        memoryGameBtn.getStyleClass().add("dashboard-buttons");
        memoryGameBtn.setContentDisplay(ContentDisplay.LEFT);
        memoryGameBtn.setOnAction(e -> openGameWindow("memory-game.fxml", "Memory Game", 800, 600));

        // Letter To Image
        ImageView letterIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/letter-to-image.png")));
        letterIcon.setFitWidth(50);
        letterIcon.setFitHeight(50);
        Button letterToImageBtn = new Button("Play Letter-To-Image", letterIcon);
        letterToImageBtn.getStyleClass().add("dashboard-buttons");
        letterToImageBtn.setContentDisplay(ContentDisplay.LEFT);
        letterToImageBtn.setOnAction(e -> openGameWindow("letter-to-image.fxml", "Letter To Image", 800, 600));

        // Odd One Out
        ImageView oddIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/numbers.png")));
        oddIcon.setFitWidth(50);
        oddIcon.setFitHeight(50);
        Button oddOneOutBtn = new Button("Play Odd One Out", oddIcon);
        oddOneOutBtn.getStyleClass().add("dashboard-buttons");
        oddOneOutBtn.setContentDisplay(ContentDisplay.LEFT);
        oddOneOutBtn.setOnAction(e -> openGameWindow("odd-one-out.fxml", "Odd One Out", 700, 500));

        // Mini Quizzes
        ImageView quizIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/memory.png")));
        quizIcon.setFitWidth(50);
        quizIcon.setFitHeight(50);
        Button quizzesBtn = new Button("Mini Quizzes", quizIcon);
        quizzesBtn.getStyleClass().add("dashboard-buttons");
        quizzesBtn.setContentDisplay(ContentDisplay.LEFT);
        quizzesBtn.setOnAction(e -> openGameWindow("mini-quiz.fxml", "Mini Quizzes", 700, 550));

        // Pop the Balloon
        ImageView balloonIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/star.png")));
        balloonIcon.setFitWidth(50);
        balloonIcon.setFitHeight(50);
        Button balloonBtn = new Button("Pop the Balloon", balloonIcon);
        balloonBtn.getStyleClass().add("dashboard-buttons");
        balloonBtn.setContentDisplay(ContentDisplay.LEFT);
        balloonBtn.setOnAction(e -> openGameWindow("pop-balloon.fxml", "Pop the Balloon", 838, 518));

        // Timed Challenge
        ImageView timedIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/miniminds/images/timed.png")));
        timedIcon.setFitWidth(50);
        timedIcon.setFitHeight(50);
        Button timedBtn = new Button("Timed Challenge", timedIcon);
        timedBtn.getStyleClass().add("dashboard-buttons");
        timedBtn.setContentDisplay(ContentDisplay.LEFT);
        timedBtn.setOnAction(e -> openGameWindow("timed-challenge.fxml", "Timed Challenge", 700, 500));

        // Layout Grid 3x3
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(mathQuizBtn, 0, 0);
        grid.add(memoryGameBtn, 1, 0);
        grid.add(letterToImageBtn, 2, 0);

        grid.add(oddOneOutBtn, 0, 1);
        grid.add(quizzesBtn, 1, 1);
        grid.add(balloonBtn, 2, 1);

        grid.add(timedBtn, 0, 2);

        VBox dashboardLayout = new VBox(30, title, grid);
        dashboardLayout.setAlignment(Pos.CENTER);

        contentArea.getChildren().add(dashboardLayout);

        // Attach CSS
        if (!contentArea.getScene().getStylesheets().contains(getClass().getResource("profile.css").toExternalForm())) {
            contentArea.getScene().getStylesheets().add(getClass().getResource("profile.css").toExternalForm());
        }
    }

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
                {"Memory Card", String.valueOf(user.getMemory()), "/com/example/miniminds/images/memory.png"},
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
    private void showPomodoro() {
        contentArea.getChildren().clear();

        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2c5364);" +
                        "-fx-padding: 50;"
        );

        // Timer text
        Text timerText = new Text("25:00");
        timerText.setStyle(
                "-fx-fill: white;" +
                        "-fx-font-size: 64px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', 'Roboto', sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,255,200,0.6), 10, 0.5, 0, 0);"
        );

        // Circular progress
        double radius = 140;
        Circle progressCircle = new Circle(radius);
        progressCircle.setFill(Color.TRANSPARENT);
        progressCircle.setStroke(Color.web("#00FFC6"));
        progressCircle.setStrokeWidth(12);
        progressCircle.setStrokeLineCap(StrokeLineCap.ROUND);
        progressCircle.setEffect(new DropShadow(20, Color.web("#00FFC6")));
        double circumference = 2 * Math.PI * radius;
        progressCircle.getStrokeDashArray().add(circumference);
        progressCircle.setStrokeDashOffset(0);

        StackPane circleStack = new StackPane(progressCircle, timerText);

        // --- Glassmorphism Settings Box (Cool Design) ---
        Label workLabel = new Label("Work (min):");
        workLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Spinner<Integer> workSpinner = new Spinner<>(1, 120, 25);
        workSpinner.setEditable(true);
        workSpinner.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: rgba(255,255,255,0.25);" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        workSpinner.getEditor().setStyle(
                "-fx-background-color: linear-gradient(to right, #232526, #414345);" +
                        "-fx-text-fill: #00FFC6;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: rgba(0,255,200,0.6);" +
                        "-fx-border-width: 1;"
        );

        Label breakLabel = new Label("Break (min):");
        breakLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Spinner<Integer> breakSpinner = new Spinner<>(1, 60, 5);
        breakSpinner.setEditable(true);
        breakSpinner.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: rgba(255,255,255,0.25);" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        breakSpinner.getEditor().setStyle(
                "-fx-background-color: linear-gradient(to right, #141E30, #243B55);" +
                        "-fx-text-fill: #36D1DC;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: rgba(91,134,229,0.6);" +
                        "-fx-border-width: 1;"
        );

        Button applyBtn = new Button("✔ Apply");
        applyBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #36d1dc, #5b86e5);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(91,134,229,0.5), 8, 0.3, 0, 0);"
        );
        applyBtn.setOnMouseEntered(e -> applyBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #5b86e5, #36d1dc);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(91,134,229,0.7), 10, 0.4, 0, 0);"
        ));
        applyBtn.setOnMouseExited(e -> applyBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #36d1dc, #5b86e5);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(91,134,229,0.5), 8, 0.3, 0, 0);"
        ));

        HBox settingsBox = new HBox(20, workLabel, workSpinner, breakLabel, breakSpinner, applyBtn);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.1);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0.4, 0, 4);"
        );

        // Control buttons
        Button startPauseBtn = new Button("▶ Start");
        startPauseBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #00b09b, #96c93d);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,255,200,0.4), 10, 0.3, 0, 0);"
        );

        Button resetBtn = new Button("⟲ Reset");
        resetBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #ff512f, #dd2476);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,80,80,0.5), 10, 0.3, 0, 0);"
        );

        HBox buttonBox = new HBox(20, startPauseBtn, resetBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(circleStack, buttonBox, settingsBox);
        contentArea.getChildren().add(root);

        // --- Timer Logic ---
        IntegerProperty workDuration = new SimpleIntegerProperty(workSpinner.getValue() * 60);
        IntegerProperty breakDuration = new SimpleIntegerProperty(breakSpinner.getValue() * 60);
        IntegerProperty timeLeft = new SimpleIntegerProperty(workDuration.get());

        BooleanProperty running = new SimpleBooleanProperty(false);
        BooleanProperty isWorkSession = new SimpleBooleanProperty(true);

        final Timeline[] timeline = new Timeline[1];

        Runnable createTimeline = () -> {
            timeline[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                timeLeft.set(timeLeft.get() - 1);

                int minutes = timeLeft.get() / 60;
                int seconds = timeLeft.get() % 60;
                timerText.setText(String.format("%02d:%02d", minutes, seconds));

                double progress = (double) timeLeft.get() / (isWorkSession.get() ? workDuration.get() : breakDuration.get());
                progressCircle.setStrokeDashOffset(circumference * (1 - progress));

                if (timeLeft.get() <= 0) {
                    timeline[0].stop();
                    running.set(false);
                    if (isWorkSession.get()) {
                        timerText.setText("Break Time!");
                        timeLeft.set(breakDuration.get());
                    } else {
                        timerText.setText("Work Time!");
                        timeLeft.set(workDuration.get());
                    }
                    isWorkSession.set(!isWorkSession.get());
                    startPauseBtn.setText("▶ Start");
                }
            }));
            timeline[0].setCycleCount(Animation.INDEFINITE);
        };

        createTimeline.run();

        startPauseBtn.setOnAction(e -> {
            if (running.get()) {
                timeline[0].stop();
                startPauseBtn.setText("▶ Start");
            } else {
                timeline[0].play();
                startPauseBtn.setText("⏸ Pause");
            }
            running.set(!running.get());
        });

        resetBtn.setOnAction(e -> {
            timeline[0].stop();
            running.set(false);
            isWorkSession.set(true);
            timeLeft.set(workDuration.get());
            timerText.setText(String.format("%02d:00", workSpinner.getValue()));
            progressCircle.setStrokeDashOffset(0);
            startPauseBtn.setText("▶ Start");
        });

        applyBtn.setOnAction(e -> {
            workDuration.set(workSpinner.getValue() * 60);
            breakDuration.set(breakSpinner.getValue() * 60);
            timeLeft.set(workDuration.get());
            timerText.setText(String.format("%02d:00", workSpinner.getValue()));
            progressCircle.setStrokeDashOffset(0);
            isWorkSession.set(true);
            timeline[0].stop();
            running.set(false);
            startPauseBtn.setText("▶ Start");
        });
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
            } else if (controller instanceof MemoryGameController memoryController) {
                memoryController.setCurrentUserEmail(Session.getCurrentUserEmail());
            } else if (controller instanceof TimedChallengeController timedController) {
                timedController.setCurrentUserEmail(Session.getCurrentUserEmail());
            }

            Stage gameStage = new Stage();
            gameStage.setTitle(title);
            gameStage.setScene(new Scene(gameRoot, width, height));
            gameStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
