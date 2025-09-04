package com.example.miniminds;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MemoryGameController {

    @FXML private Label remainingLabel;
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;

    @FXML private Rectangle rect1, rect2, rect3, rect4, rect5, rect6;

    private List<Rectangle> allRects;
    private Set<Rectangle> targetSet;
    private Set<Rectangle> clickedSet;
    private int remaining = 20;
    private int score = 0;

    // === Added: Media setup ===
    private Media dingSound;

    @FXML
    public void initialize() {
        allRects = List.of(rect1, rect2, rect3, rect4, rect5, rect6);

        // Load sound (from resources)
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
        clickedSet = new HashSet<>();
        messageLabel.setText("");

        for (Rectangle rect : allRects) {
            rect.setFill(Color.web("#524ac2"));
        }

        // pick random targets
        targetSet = new HashSet<>();
        Random rand = new Random();
        int count = 1 + rand.nextInt(allRects.size());
        while (targetSet.size() < count) {
            targetSet.add(allRects.get(rand.nextInt(allRects.size())));
        }

        // show them green briefly + play sound
        for (Rectangle rect : targetSet) {
            rect.setFill(Color.GREEN);
            playSound(); // <-- Play ding when rectangles light up
        }

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                for (Rectangle rect : allRects) {
                    rect.setFill(Color.web("#524ac2"));
                }
            });
        }).start();
    }

    private void handleClick(Rectangle rect) {
        if (remaining <= 0 || targetSet == null) return;

        clickedSet.add(rect);
        rect.setFill(Color.LIGHTBLUE);

        playSound(); // <-- Play ding when user clicks

        if (clickedSet.size() == targetSet.size()) {
            evaluateRound();
        }
    }

    private void evaluateRound() {
        boolean won = clickedSet.equals(targetSet);

        remaining--;
        remainingLabel.setText("Remaining : " + remaining);

        if (won) {
            score++;
            scoreLabel.setText("Score : " + score);
            messageLabel.setText("Excellent");
        } else {
            messageLabel.setText("Nice try");
        }

        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                if (remaining > 0) startRound();
            });
        }).start();
    }

    // === Helper to play sound ===
    private void playSound() {
        if (dingSound != null) {
            MediaPlayer mediaPlayer = new MediaPlayer(dingSound);
            mediaPlayer.play();
        }
    }
}
