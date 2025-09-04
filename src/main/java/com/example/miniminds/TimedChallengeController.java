package com.example.miniminds;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

// AudioVisual
import javafx.scene.media.AudioClip;
import java.util.Random;

public class TimedChallengeController {

    @FXML private Text questionText;
    @FXML private Button answerBtn1;
    @FXML private Button answerBtn2;
    @FXML private Button answerBtn3;
    @FXML private Text feedbackText;
    @FXML private Text scoreText;
    @FXML private Text timerText;

    private int correctAnswer;
    private int score = 0;
    private final int GAME_TIME = 60; // seconds
    private int timeLeft = GAME_TIME;

    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip winningSound;

    private String currentUserEmail;
    private final Random random = new Random();
    private Timeline timer;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    @FXML
    private void initialize() {
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());
        winningSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/win-game.mp3").toExternalForm());

        scoreText.setText("Score: 0");
        timerText.setText("Time: " + GAME_TIME + "s");

        startTimer();
        generateQuestion();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerText.setText("Time: " + timeLeft + "s");

            if (timeLeft <= 0) {
                timer.stop();
                endGame();
            }
        }));
        timer.setCycleCount(GAME_TIME);
        timer.play();
    }

    private void generateQuestion() {
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        correctAnswer = a + b;
        questionText.setText(a + " + " + b + " = ?");

        // Randomly place correct answer
        int correctIndex = random.nextInt(3);
        int wrong1 = correctAnswer + (random.nextBoolean() ? 1 : -1);
        int wrong2 = correctAnswer + (random.nextBoolean() ? 2 : -2);

        Button[] buttons = {answerBtn1, answerBtn2, answerBtn3};
        for (int i = 0; i < buttons.length; i++) {
            if (i == correctIndex) {
                buttons[i].setText(String.valueOf(correctAnswer));
            } else {
                buttons[i].setText(i == (correctIndex + 1) % 3 ? String.valueOf(wrong1) : String.valueOf(wrong2));
            }
            buttons[i].setDisable(false);
        }

        feedbackText.setText("");
    }

    @FXML
    private void handleAnswer(javafx.event.ActionEvent event) {
        Button clicked = (Button) event.getSource();
        int chosen = Integer.parseInt(clicked.getText());

        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);

        int timeDelay;
        if (chosen == correctAnswer) {
            score++;
            feedbackText.setText("✅ Correct!");
            feedbackText.setStyle("-fx-fill: green; -fx-font-size: 30px; -fx-font-weight: bold;");
            playConfettiEffect(clicked);
            correctSound.play();
            timeDelay = 0; // move quickly
        } else {
            feedbackText.setText("❌ Wrong! Correct: " + correctAnswer);
            feedbackText.setStyle("-fx-fill: red; -fx-font-size: 30px; -fx-font-weight: bold;");
            wrongSound.play();
            timeDelay = 1;
        }

        scoreText.setText("Score: " + score);

        // Delay before next question
        PauseTransition pause = new PauseTransition(Duration.seconds(timeDelay));
        pause.setOnFinished(e -> generateQuestion());
        pause.play();
    }

    private void playConfettiEffect(Button btn) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), btn);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    private void endGame() {
        winningSound.play();
        questionText.setText("⏰ Time’s Up!");
        feedbackText.setText("Final Score: " + score);

        // Save performance in DB
        int previousScore = DatabaseHelper.getTimedScore(currentUserEmail);
        int newScore = previousScore + score;
        DatabaseHelper.updateTimedScore(currentUserEmail, newScore);

        feedbackText.setStyle("-fx-font-weight: bold; -fx-fill: white; -fx-font-size: 35px;");

        feedbackText.setText(
                "Previous Score: " + previousScore +
                        " + " + score +
                        "\nNew Score: " + newScore
        );

        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);
    }
}
