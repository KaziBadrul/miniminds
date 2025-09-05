package com.example.miniminds;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

// AudioVisual
import javafx.scene.media.AudioClip;

import java.util.Random;

public class MathQuizController {

    @FXML private Text questionText;
    @FXML private Button answerBtn1;
    @FXML private Button answerBtn2;
    @FXML private Button answerBtn3;
    @FXML private Text feedbackText;
    @FXML private Text scoreText;
    @FXML private Text remainingText;
    @FXML private Text levelText;

    private int correctAnswer;
    private int score = 0;
    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 20;

    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip winningSound;

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadUserLevel();   // <-- load DB here, not in initialize()
    }

    private void loadUserLevel() {
        int scoreFromDB = DatabaseHelper.getMathScore(currentUserEmail);
        userLevel = scoreFromDB / 20;
        if (userLevel < 1) userLevel = 1;

        levelText.setText("Level " + userLevel);
        scoreText.setText("Score: 0");
        remainingText.setText("Remaining: " + TOTAL_QUESTIONS);

        generateQuestion();
    }

    private final Random random = new Random();
    private int userLevel = 1;

    @FXML
    private void initialize() {
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());
        winningSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/win-game.mp3").toExternalForm());

        scoreText.setText("Score: 0");
        remainingText.setText("Remaining: " + TOTAL_QUESTIONS);
        generateQuestion();
    }

    private void generateQuestion() {
        if (questionCount >= TOTAL_QUESTIONS) {
            endGame();
            return;
        }



        if (userLevel <= 5) {
            int a = random.nextInt(10 * userLevel) + 1;
            int b = random.nextInt(10 * userLevel) + 1;
            correctAnswer = a + b;
            questionText.setText(a + " + " + b + " = ?");
        }
        if (userLevel > 5) {
            int modLevel = userLevel % 10;
            if (modLevel == 0) modLevel = userLevel / 10;  // ensure it's not zero
            int a = random.nextInt(10 * modLevel) + 1;
            int b = random.nextInt(10 * modLevel) + 1;
            correctAnswer = a * b;
            questionText.setText(a + " X " + b + " = ?");
        }

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
            buttons[i].setDisable(false); // re-enable for new question
        }

        // Reset feedback
        feedbackText.setText("");

        remainingText.setText("Remaining: " + (TOTAL_QUESTIONS - questionCount));
    }

    @FXML
    private void handleAnswer(javafx.event.ActionEvent event) {
        Button clicked = (Button) event.getSource();
        int chosen = Integer.parseInt(clicked.getText());

        // Disable buttons immediately
        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);

        int time;
        if (chosen == correctAnswer) {
            score++;
            feedbackText.setText("✅ Correct!");
            feedbackText.setStyle("-fx-fill: green; -fx-font-size: 24px;");
            playConfettiEffect(clicked);
            correctSound.play();
            time = 1;
        } else {
            feedbackText.setText("❌ Wrong! Correct: " + correctAnswer);
            feedbackText.setStyle("-fx-fill: red; -fx-font-size: 24px;");
            wrongSound.play();
            time = 2;
        }

        scoreText.setText("Score: " + score);
        questionCount++;

        // Delay next question
        PauseTransition pause = new PauseTransition(Duration.seconds(time));
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
        questionText.setText("🎉 Game Over!");
        feedbackText.setText("Final Score: " + score + " / " + TOTAL_QUESTIONS);

        int previousScore = DatabaseHelper.getMathScore(currentUserEmail);
//        System.out.println("Previous Score from DB: " + currentUserEmail);
        int addedScore = (int) Math.ceil(score / 2.0);
        int newScore = previousScore + addedScore;
        DatabaseHelper.updateMathScore(currentUserEmail, newScore);

        int level = newScore / 20;

        feedbackText.setText(
                "Previous Score: " + previousScore +
                        " + " + addedScore +
                        "\nNew Score: " + newScore +
                        "\nLevel: " + level
        );

        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);

        remainingText.setText("Remaining: 0");
    }
}
