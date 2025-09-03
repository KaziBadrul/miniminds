package com.example.miniminds;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class MathQuizController {

    @FXML private Text questionText;
    @FXML private Button answerBtn1;
    @FXML private Button answerBtn2;
    @FXML private Button answerBtn3;
    @FXML private Text feedbackText;
    @FXML private Text scoreText;
    @FXML private Text remainingText;

    private int correctAnswer;
    private int score = 0;
    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 20;

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    private final Random random = new Random();

    @FXML
    private void initialize() {
        scoreText.setText("Score: 0");
        remainingText.setText("Remaining: " + TOTAL_QUESTIONS);
        generateQuestion();
    }

    private void generateQuestion() {
        if (questionCount >= TOTAL_QUESTIONS) {
            endGame();
            return;
        }

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

        if (chosen == correctAnswer) {
            score++;
            feedbackText.setText("✅ Correct!");
            feedbackText.setStyle("-fx-fill: green; -fx-font-size: 24px;");
            playConfettiEffect(clicked);
        } else {
            feedbackText.setText("❌ Wrong!");
            feedbackText.setStyle("-fx-fill: red; -fx-font-size: 24px;");
        }

        scoreText.setText("Score: " + score);
        questionCount++;

        // Delay next question
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
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
        questionText.setText("🎉 Game Over!");
        feedbackText.setText("Final Score: " + score + " / " + TOTAL_QUESTIONS);

        int previousScore = DatabaseHelper.getMathScore(currentUserEmail);
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
