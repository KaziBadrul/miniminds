package com.example.miniminds;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniQuizController {

    @FXML private Label questionLabel;
    @FXML private Label optionALabel;
    @FXML private Label optionBLabel;
    @FXML private Label optionCLabel;
    @FXML private Label optionDLabel;
    @FXML private Label scoreLabel;
    @FXML private Label remainingLabel;
    @FXML private Label finalLabel;

    private int currentQuestionIndex = 0;
    private int score = 0;

    private List<Question> questions;
    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip winningSound;

    private String currentUserEmail;
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email; }

    @FXML
    public void initialize() {
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());
        winningSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/win-game.mp3").toExternalForm());

        finalLabel.setStyle("-fx-background-color: transparent;");
        questions = generateQuestions();
        Collections.shuffle(questions);
        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            int prevScore = DatabaseHelper.getOddOneOutScore(currentUserEmail);
            int pointsEarned = (int) Math.ceil(score / 2.0);
            int newScore = prevScore + pointsEarned;
            int level = newScore / 20;

            finalLabel.setText("🎉 Game Over!\nIQ Gained: " + prevScore +
                    " + " + pointsEarned +
                    "\nLevel: " + level);
//            remainingText.setText("Remaining: 0");

            // Save new IQ score in DB
            DatabaseHelper.updateOddOneOutScore(currentUserEmail, newScore);

            winningSound.play();
            questionLabel.setText("Quiz Finished! 🎉");
            optionALabel.setVisible(false);
            optionBLabel.setVisible(false);
            optionCLabel.setVisible(false);
            optionDLabel.setVisible(false);
            remainingLabel.setText("Remaining : 0");
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText(q.getQuestion());

        optionALabel.setText(q.getOptions().get(0));
        optionBLabel.setText(q.getOptions().get(1));
        optionCLabel.setText(q.getOptions().get(2));
        optionDLabel.setText(q.getOptions().get(3));

        // reset option colors
        resetOptionStyles();

        remainingLabel.setText("Remaining : " + (questions.size() - currentQuestionIndex));
        scoreLabel.setText("Score : " + score);
    }

    @FXML
    private void handleOptionClick(javafx.scene.input.MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        Question q = questions.get(currentQuestionIndex);

        if (clickedLabel.getText().equals(q.getCorrectAnswer())) {
            correctSound.play();
            clickedLabel.setStyle("-fx-background-color: green; -fx-background-radius: 12;");
            score++;
        } else {
            wrongSound.play();
            clickedLabel.setStyle("-fx-background-color: red; -fx-background-radius: 12;");
        }

        scoreLabel.setText("Score : " + score);

        // Move to next question after short delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                currentQuestionIndex++;
                showQuestion();
            });
        }).start();
    }

    private void resetOptionStyles() {
        String defaultStyle = "-fx-background-color: #ffbf7a; -fx-background-radius: 12;";
        optionALabel.setStyle(defaultStyle);
        optionBLabel.setStyle(defaultStyle);
        optionCLabel.setStyle(defaultStyle);
        optionDLabel.setStyle(defaultStyle);
    }

    private List<Question> generateQuestions() {
        List<Question> qs = new ArrayList<>();

        qs.add(new Question("What color is the sky?", List.of("Blue", "Green", "Red", "Yellow"), "Blue"));
        qs.add(new Question("Who is the best faculty of IUT?", List.of("Ishmam Tashdeed", "IT", "Ishmam Sir", "Tashdeed Sir"), "Ishmam Tashdeed"));
        qs.add(new Question("How many legs does a cat have?", List.of("2", "4", "6", "8"), "4"));
        qs.add(new Question("What is 2 + 3?", List.of("4", "5", "6", "7"), "5"));
        qs.add(new Question("Which animal says 'Moo'?", List.of("Dog", "Cat", "Cow", "Sheep"), "Cow"));
        qs.add(new Question("What shape has 3 sides?", List.of("Triangle", "Square", "Circle", "Rectangle"), "Triangle"));
        qs.add(new Question("What is 1 + 1?", List.of("1", "2", "3", "4"), "2"));
        qs.add(new Question("Which fruit is yellow?", List.of("Banana", "Apple", "Grapes", "Orange"), "Banana"));
        qs.add(new Question("What is 5 - 2?", List.of("2", "3", "4", "5"), "3"));
        qs.add(new Question("Which is bigger, 10 or 7?", List.of("7", "10", "Both", "None"), "10"));
        qs.add(new Question("What color are grass and leaves?", List.of("Green", "Blue", "Red", "Purple"), "Green"));
        qs.add(new Question("What is 3 + 4?", List.of("6", "7", "8", "9"), "7"));
        qs.add(new Question("Which animal barks?", List.of("Dog", "Cat", "Cow", "Lion"), "Dog"));
        qs.add(new Question("What is 10 - 5?", List.of("3", "4", "5", "6"), "5"));
        qs.add(new Question("Which planet do we live on?", List.of("Mars", "Earth", "Jupiter", "Moon"), "Earth"));
        qs.add(new Question("What comes after number 9?", List.of("8", "9", "10", "11"), "10"));
        qs.add(new Question("What is 2 × 2?", List.of("2", "3", "4", "5"), "4"));
        qs.add(new Question("Which animal has a long trunk?", List.of("Elephant", "Tiger", "Horse", "Giraffe"), "Elephant"));
        qs.add(new Question("What color is an apple?", List.of("Blue", "Red", "Purple", "Black"), "Red"));
        qs.add(new Question("What is 7 - 3?", List.of("3", "4", "5", "6"), "4"));
        qs.add(new Question("How many days are in a week?", List.of("5", "6", "7", "8"), "7"));

        return qs;
    }

    // ===== Inner Question class =====
    private static class Question {
        private final String question;
        private final List<String> options;
        private final String correctAnswer;

        public Question(String question, List<String> options, String correctAnswer) {
            this.question = question;
            this.options = new ArrayList<>(options);
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() { return question; }
        public List<String> getOptions() { return options; }
        public String getCorrectAnswer() { return correctAnswer; }
    }
}
