package com.example.miniminds;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// AudioVisual
import javafx.scene.media.AudioClip;

public class OddOneOutController {

    @FXML private ImageView img1;
    @FXML private ImageView img2;
    @FXML private ImageView img3;
    @FXML private ImageView img4;
    @FXML private Text feedbackText;
    @FXML private Text questionText;
    @FXML private Text remainingText;

    private final ImageView[] imgs = new ImageView[4];
    private int score = 0;             // Current score in this session
    private int questionCount = 0;
    private List<String[]> questions;
    private String currentUserEmail;
    private int prevScore = 0;         // Score before this session

    private AudioClip correctSound;
    private AudioClip wrongSound;

    private static final String[][] BASE_QUESTIONS = {
            {"dog.png", "cat.png", "lion.png", "car.png"},
            {"sun.png", "moon.png", "star.png", "cat.png"},
            {"apple.png", "banana.png", "orange.png", "car.png"},
            {"dog.png", "cat.png", "lion.png", "orange.png"},
            {"apple.png", "banana.png", "orange.png", "lion.png"},
            {"sun.png", "moon.png", "star.png", "apple.png"},
            {"dog.png", "cat.png", "lion.png", "banana.png"},
            {"apple.png", "banana.png", "orange.png", "dog.png"},
            {"sun.png", "moon.png", "star.png", "cat.png"},
            {"apple.png", "banana.png", "orange.png", "sun.png"},
            {"dog.png", "cat.png", "lion.png", "moon.png"},
            {"sun.png", "moon.png", "star.png", "orange.png"},
            {"apple.png", "banana.png", "orange.png", "car.png"},
    };

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;

        // Get previous IQ score from DB
        Integer dbScore = DatabaseHelper.getOddOneOutScore(email);
        prevScore = (dbScore != null) ? dbScore : 0;

        // Generate 20 shuffled questions
        questions = generateQuestions(20);

        // Initialize UI after email is set
        imgs[0] = img1;
        imgs[1] = img2;
        imgs[2] = img3;
        imgs[3] = img4;

        loadNextQuestion();
    }

    @FXML
    private void initialize() {
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());

        feedbackText.setText("");
        questionText.setText("Find the odd one out!");
        remainingText.setText("Remaining: 20");
    }

    private List<String[]> generateQuestions(int totalQuestions) {
        List<String[]> allQuestions = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            String[] base = BASE_QUESTIONS[i % BASE_QUESTIONS.length];
            List<String> images = new ArrayList<>();
            images.add(base[0]);
            images.add(base[1]);
            images.add(base[2]);
            String odd = base[3];

            Collections.shuffle(images);
            int oddPos = (int)(Math.random()*4);
            images.add(oddPos, odd);

            String[] question = new String[5];
            for (int j = 0; j < 4; j++) question[j] = images.get(j);
            question[4] = String.valueOf(oddPos);
            allQuestions.add(question);
        }
        return allQuestions;
    }

    private void loadNextQuestion() {
        if (questionCount >= questions.size()) {
            endGame();
            return;
        }

        String[] q = questions.get(questionCount);
        int oddIndex = Integer.parseInt(q[4]);

        for (int i = 0; i < 4; i++) {
            InputStream is = getClass().getResourceAsStream("/com/example/miniminds/images/" + q[i]);
            if (is != null) {
                imgs[i].setImage(new Image(is));
            } else {
                feedbackText.setText("Image missing: " + q[i]);
            }
            imgs[i].setDisable(false);
            imgs[i].setVisible(true);
            final int index = i;
            imgs[i].setOnMouseClicked(e -> handleClick(index, oddIndex, q[index]));
        }

        feedbackText.setText("");
        questionText.setText("Find the odd one out!");
        remainingText.setText("Remaining: " + (questions.size() - questionCount));
    }

    private void handleClick(int clickedIndex, int oddIndex, String clickedImageName) {
        for (ImageView img : imgs) img.setDisable(true);

        String name = clickedImageName.replace(".png", "").replace("_", " ");
        int time = 0;
        if (clickedIndex == oddIndex) {
            feedbackText.setText("✅ " + capitalize(name) + " is Correct!");
            feedbackText.setStyle("-fx-fill: green;");
            correctSound.play();
            time = 1;
            score++;
        } else {
            String correctName = questions.get(questionCount)[oddIndex].replace(".png","").replace("_"," ");
            feedbackText.setText("❌ " + capitalize(name) + " is Wrong! Correct: " + capitalize(correctName));
            feedbackText.setStyle("-fx-fill: red;");
            wrongSound.play();
            time = 2;
        }

        questionCount++;
        remainingText.setText("Remaining: " + (questions.size() - questionCount));

        PauseTransition pause = new PauseTransition(Duration.seconds(time));
        pause.setOnFinished(e -> loadNextQuestion());
        pause.play();
    }

    private void endGame() {
        for (ImageView img : imgs) img.setVisible(false);

        int pointsEarned = (int) Math.ceil(score / 2.0);
        int newScore = prevScore + pointsEarned;
        int level = newScore / 20;

        feedbackText.setText("🎉 Game Over!\nPrevious Score: " + prevScore +
                " + " + pointsEarned +
                "\nNew Score: " + newScore +
                "\nLevel: " + level);
        remainingText.setText("Remaining: 0");

        // Save new IQ score in DB
        DatabaseHelper.updateOddOneOutScore(currentUserEmail, newScore);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
