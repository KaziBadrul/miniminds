package com.example.miniminds;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

// AudioVisual
import javafx.scene.media.AudioClip;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.io.InputStream;
import java.util.*;

public class LetterToImageController {

    @FXML private ImageView imageView;
    @FXML private HBox lettersBox;     // Holds draggable letters
    @FXML private Text feedbackText;
    @FXML private Text scoreText;
    @FXML private Text remainingText;
    @FXML private Label targetBox;      // Drop target

    private String currentUserEmail; // set this from your login session
    private int newScore = 0; // session score (for this game run only)

    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip winningSound;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    private List<String> images = Arrays.asList(
            "apple.png",
            "banana.png",
            "cat.png",
            "dog.png",
            "elephant.png",
            "factory.png",
            "grape.png",
            "house.png",
            "icecream.png"
    );

    private int currentIndex = 0;
    private int score = 0;

    @FXML
    private void initialize() {
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());
        winningSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/win-game.mp3").toExternalForm());

        Collections.shuffle(images);
        loadNextImage();

        // Setup drag target
        targetBox.setOnDragOver(this::handleDragOver);

        // Drag entered -> highlight
        targetBox.setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) {
                targetBox.getStyleClass().add("drop-box-highlight");
            }
        });

        // Drag exited -> remove highlight
        targetBox.setOnDragExited(e -> {
            targetBox.getStyleClass().remove("drop-box-highlight");
        });

        targetBox.setOnDragDropped(this::handleDragDropped);
    }

    private void loadNextImage() {
        if (currentIndex >= images.size()) {
            winningSound.play();
            feedbackText.setText("🎉 Game Over! Final Score: " + score);
            lettersBox.getChildren().clear();
            imageView.setVisible(false);
            targetBox.setText("Yay!");

            int prevScore = DatabaseHelper.getLetterToImageScore(currentUserEmail);
            int updatedScore = prevScore + (int) Math.ceil(newScore / 2.0);
            int level = updatedScore / 20;
            DatabaseHelper.updateLetterToImageScore(currentUserEmail, updatedScore);


            scoreText.setText("Spelling Experience: " + prevScore +
                    " + " + (int)Math.ceil(newScore / 2.0) +
                    " \nLevel: " + level);
            return;
        }

        String imgName = images.get(currentIndex);
        InputStream is = getClass().getResourceAsStream("/com/example/miniminds/images/" + imgName);
        if (is != null) {
            imageView.setImage(new Image(is));
        }

        feedbackText.setText("Drag the correct letter!");
        scoreText.setText("Score: " + score);
        remainingText.setText("Remaining: " + (images.size() - currentIndex));

        generateLetterOptions(imgName.charAt(0));
        targetBox.getStyleClass().removeAll("drop-box-correct", "drop-box-wrong");
        targetBox.setText("Drop Here");
    }

    private void generateLetterOptions(char correctLetter) {
        lettersBox.getChildren().clear();

        Set<Character> letters = new HashSet<>();
        letters.add(correctLetter);
        Random rand = new Random();

        // Add 3 random wrong letters
        while (letters.size() < 4) {
            char c = (char) ('A' + rand.nextInt(26));
            letters.add(c);
        }

        List<Character> shuffled = new ArrayList<>(letters);
        Collections.shuffle(shuffled);

        for (char c : shuffled) {
            Label letterTile = new Label(String.valueOf(Character.toUpperCase(c)));
            letterTile.getStyleClass().add("letter-tile");

            // Enable dragging
            letterTile.setOnDragDetected(event -> {
                Dragboard db = letterTile.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(letterTile.getText());
                db.setContent(content);
                event.consume();
            });

            lettersBox.getChildren().add(letterTile);
        }
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != targetBox && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            String draggedLetter = db.getString();
            char correctLetter = Character.toUpperCase(images.get(currentIndex).charAt(0));
            int time = 1;

            targetBox.getStyleClass().removeAll("drop-box-highlight", "drop-box-correct", "drop-box-wrong");

            if (draggedLetter.charAt(0) == correctLetter) {
                feedbackText.setText("✅ Correct! " + draggedLetter + " is for " + images.get(currentIndex).replace(".png", ""));
                feedbackText.setStyle("-fx-fill: green; -fx-size: 20px;");
                score++;
                newScore++;
                targetBox.getStyleClass().add("drop-box-correct");
                time = 1;

                // Sound
                correctSound.play();

                // Bounce animation
                ScaleTransition st = new ScaleTransition(Duration.seconds(0.3), targetBox);
                st.setFromX(1.0);
                st.setFromY(1.0);
                st.setToX(1.2);
                st.setToY(1.2);
                st.setCycleCount(2);
                st.setAutoReverse(true);
                st.play();

            } else {
                feedbackText.setText("❌ Wrong! Correct letter is " + correctLetter + " for " + images.get(currentIndex).replace(".png", ""));
                feedbackText.setStyle("-fx-fill: red; -fx-size: 20px;");
                targetBox.getStyleClass().add("drop-box-wrong");
                time = 2;

                // Sound
                wrongSound.play();

                // Shake animation
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.1), targetBox);
                tt.setFromX(-10);
                tt.setToX(10);
                tt.setCycleCount(6);
                tt.setAutoReverse(true);
                tt.play();

            }

            currentIndex++;

            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(time));
            pause.setOnFinished(e -> loadNextImage());
            pause.play();

            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
}
