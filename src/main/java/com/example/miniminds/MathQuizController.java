package com.example.miniminds;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class MathQuizController {

    @FXML private Text questionText;
    @FXML private Button answerBtn1;
    @FXML private Button answerBtn2;
    @FXML private Button answerBtn3;
    @FXML private Text feedbackText;

    private int correctAnswer;

    private final Random random = new Random();

    @FXML
    private void initialize() {
        generateQuestion();
    }

    private void generateQuestion() {
        int a = random.nextInt(10) + 1; // 1–10
        int b = random.nextInt(10) + 1;
        correctAnswer = a + b;

        questionText.setText(a + " + " + b + " = ?");

        // Randomly place the correct answer among 3 buttons
        int correctIndex = random.nextInt(3);
        int wrong1 = correctAnswer + (random.nextBoolean() ? 1 : -1);
        int wrong2 = correctAnswer + (random.nextBoolean() ? 2 : -2);

        Button[] buttons = {answerBtn1, answerBtn2, answerBtn3};
        for (int i = 0; i < buttons.length; i++) {
            if (i == correctIndex) {
                buttons[i].setText(String.valueOf(correctAnswer));
            } else if (buttons[i].getText().isEmpty()) {
                buttons[i].setText(i == (correctIndex + 1) % 3 ? String.valueOf(wrong1) : String.valueOf(wrong2));
            }
        }

        feedbackText.setText("");
    }

    @FXML
    private void handleAnswer(javafx.event.ActionEvent event) {
        Button clicked = (Button) event.getSource();
        int chosen = Integer.parseInt(clicked.getText());

        if (chosen == correctAnswer) {
            feedbackText.setText("✅ Correct!");
            playConfettiEffect(clicked);
            generateQuestion(); // load new question
        } else {
            feedbackText.setText("❌ Try again!");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }

    private void playConfettiEffect(Button btn) {
        // Simple fade animation for now
        FadeTransition ft = new FadeTransition(Duration.millis(300), btn);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }
}
