package com.example.miniminds;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Text;
import java.util.Random;
import javafx.scene.shape.Circle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class PopBalloonController {

    @FXML public Label scoreTextLabel;
    @FXML public Label remainingTextLabel;
    @FXML private Button startGameBtn; // Button from FXML

    //  Balloons and numbers
    @FXML private Circle balloonCircle1;
    @FXML private Circle balloonCircle2;
    @FXML private Circle balloonCircle3;

    @FXML private CubicCurve balloonRibbon1;
    @FXML private CubicCurve balloonRibbon2;
    @FXML private CubicCurve balloonRibbon3;

    @FXML private Text balloonNum1;
    @FXML private Text balloonNum2;
    @FXML private Text balloonNum3;

    //  Score and Remaining labels
    @FXML private Label scoreLabel;
    @FXML private Label remainingLabel;

    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip popSound;
    private AudioClip winningSound;

    private String currentUserEmail;
    private final Random random = new Random();

    //  Track score and remaining rounds
    private int score = 0;
    private int remaining = 20;
    private int targetNumber;

    // Called from MainController when opening the game
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    private void animateRibbons() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(balloonRibbon1.controlX1Property(), balloonRibbon1.getControlX1()),
                        new KeyValue(balloonRibbon1.controlX2Property(), balloonRibbon1.getControlX2()),
                        new KeyValue(balloonRibbon2.controlX1Property(), balloonRibbon2.getControlX1()),
                        new KeyValue(balloonRibbon2.controlX2Property(), balloonRibbon2.getControlX2()),
                        new KeyValue(balloonRibbon3.controlX1Property(), balloonRibbon3.getControlX1()),
                        new KeyValue(balloonRibbon3.controlX2Property(), balloonRibbon3.getControlX2())
                ),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(balloonRibbon1.controlX1Property(), balloonRibbon1.getControlX1() + 15),
                        new KeyValue(balloonRibbon1.controlX2Property(), balloonRibbon1.getControlX2() + 15),
                        new KeyValue(balloonRibbon2.controlX1Property(), balloonRibbon2.getControlX1() + 15),
                        new KeyValue(balloonRibbon2.controlX2Property(), balloonRibbon2.getControlX2() + 15),
                        new KeyValue(balloonRibbon3.controlX1Property(), balloonRibbon3.getControlX1() + 15),
                        new KeyValue(balloonRibbon3.controlX2Property(), balloonRibbon3.getControlX2() + 15)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void initialize() {
        animateRibbons();
        correctSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/correct.wav").toExternalForm());
        wrongSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/wrong.wav").toExternalForm());
        popSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/balloon-pop.mp3").toExternalForm());
        winningSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/win-game.mp3").toExternalForm());


        updateLabels();
        generateRound();

        // 🔹 Set button action
        startGameBtn.setOnAction(e -> {
            System.out.println("Balloon Game Started!");
        });

        // 🔹 FIXED: Circles are now clickable instead of texts
        balloonCircle1.setOnMouseClicked(e -> checkBalloon(balloonNum1));
        balloonCircle2.setOnMouseClicked(e -> checkBalloon(balloonNum2));
        balloonCircle3.setOnMouseClicked(e -> checkBalloon(balloonNum3));
    }

    // 🔹 Start a new round with fresh numbers
    private void generateRound() {
        if (remaining <= 0) {
            startGameBtn.setText("Game Over!");
            int previousScore = DatabaseHelper.getBalloonScore(currentUserEmail);
            int addedScore = (int) Math.ceil(score / 2.0);
            int newScore = previousScore + addedScore;
            DatabaseHelper.updateBalloonScore(currentUserEmail, newScore);

            int level = newScore / 20;

            remainingTextLabel.setText(
                    "Previous Score: " + previousScore +
                            " + " + addedScore +
                            "\nNew Score: " + newScore +
                            "\nLevel: " + level
            );
            scoreTextLabel.setText("");
            scoreLabel.setText("");
            remainingLabel.setText("");
            return;
        }

        int num1 = random.nextInt(9) + 1;

        int num2;
        do {
            num2 = random.nextInt(9) + 1;
        } while (num2 == num1);

        int num3;
        do {
            num3 = random.nextInt(9) + 1;
        } while (num3 == num1 || num3 == num2);

        balloonNum1.setText(String.valueOf(num1));
        balloonNum2.setText(String.valueOf(num2));
        balloonNum3.setText(String.valueOf(num3));

        int[] nums = {num1, num2, num3};
        targetNumber = nums[random.nextInt(nums.length)];

        startGameBtn.setText("Pop the number " + targetNumber);
    }

    // 🔹 Check if clicked balloon is correct
    private void checkBalloon(Text clickedBalloon) {
        if (remaining <= 0) {
            winningSound.play();
            return;
        };

        int clickedNum = Integer.parseInt(clickedBalloon.getText());
        popSound.play();
        if (clickedNum == targetNumber) {
            correctSound.play();
            score++;
        } else {
            wrongSound.play();
        }
        remaining--;

        updateLabels();
        generateRound();
    }

    // 🔹 Update score and remaining labels
    private void updateLabels() {
        scoreLabel.setText(""+ score);
        remainingLabel.setText(""+remaining);
    }
}
