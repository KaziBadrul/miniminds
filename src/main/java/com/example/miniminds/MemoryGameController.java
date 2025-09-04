package com.example.miniminds;

import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryGameController {

    @FXML private Label remainingLabel;
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;

    @FXML private Rectangle rect1, rect2, rect3, rect4, rect5, rect6;

    private List<Rectangle> allRects;
    private List<Rectangle> targetSequence;   // sequence to follow
    private List<Rectangle> clickedSequence;  // player's clicks
    private int remaining = 20;
    private int score = 0;
    private boolean inputEnabled = false; // disable clicks during sequence

    // === Sound setup ===
    private Media dingSound;

    @FXML
    public void initialize() {
        allRects = List.of(rect1, rect2, rect3, rect4, rect5, rect6);

        // Load sound
        URL soundURL = getClass().getResource("/com/example/miniminds/Sounds/rectangle-ding.mp3");
        if (soundURL != null) {
            dingSound = new Media(soundURL.toExternalForm());
        }

        for (Rectangle rect : allRects) {
            rect.setOnMouseClicked(e -> handleClick(rect));
        }

        startRound();
    }

    private void startRound() {
        clickedSequence = new ArrayList<>();
        messageLabel.setText("");
        inputEnabled = false; // lock input until sequence ends

        for (Rectangle rect : allRects) {
            rect.setFill(Color.web("#524ac2"));
        }

        // pick random sequence length
        targetSequence = new ArrayList<>();
        Random rand = new Random();
        int count = 2 + rand.nextInt(3); // sequence length between 2 and 4 for kids
        for (int i = 0; i < count; i++) {
            targetSequence.add(allRects.get(rand.nextInt(allRects.size())));
        }

        playSequenceSmooth();
    }

    private void playSequenceSmooth() {
        SequentialTransition sequenceAnim = new SequentialTransition();

        for (Rectangle rect : targetSequence) {
            FillTransition highlight = new FillTransition(Duration.millis(500), rect, Color.web("#524ac2"), Color.GREEN);
            FillTransition back = new FillTransition(Duration.millis(500), rect, Color.GREEN, Color.web("#524ac2"));

            highlight.setOnFinished(e -> playSound());

            sequenceAnim.getChildren().addAll(highlight, back, new PauseTransition(Duration.millis(200)));
        }

        sequenceAnim.setOnFinished(e -> inputEnabled = true); // unlock clicks after sequence
        sequenceAnim.play();
    }

    private void handleClick(Rectangle rect) {
        if (!inputEnabled || remaining <= 0 || targetSequence == null) return;

        clickedSequence.add(rect);
        playSound();

        // give click feedback (flash yellow briefly)
        FillTransition clickAnim = new FillTransition(Duration.millis(200), rect, Color.web("#524ac2"), Color.YELLOW);
        FillTransition back = new FillTransition(Duration.millis(200), rect, Color.YELLOW, Color.web("#524ac2"));
        new SequentialTransition(clickAnim, back).play();

        // check if current click is correct
        int currentIndex = clickedSequence.size() - 1;
        if (clickedSequence.get(currentIndex) != targetSequence.get(currentIndex)) {
            endRound(false);
            return;
        }

        // if sequence complete
        if (clickedSequence.size() == targetSequence.size()) {
            endRound(true);
        }
    }

    private void endRound(boolean won) {
        inputEnabled = false;
        remaining--;
        remainingLabel.setText("Remaining : " + remaining);

        if (won) {
            score++;
            scoreLabel.setText("Score : " + score);
            messageLabel.setText("Excellent");
        } else {
            messageLabel.setText("Nice try");
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            if (remaining > 0) startRound();
        });
        delay.play();
    }

    // === Play sound ===
    private void playSound() {
        if (dingSound != null) {
            MediaPlayer mediaPlayer = new MediaPlayer(dingSound);
            mediaPlayer.play();
        }
    }
}
