package com.example.miniminds;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text; // 🔹 Added
import java.util.Random;       // 🔹 Added
import javafx.scene.shape.Circle;

// 🔹 NEW: Controller for pop-balloon.fxml
public class PopBalloonController {

    @FXML
    private Button startGameBtn; // Button from FXML

    // 🔹 Balloons and numbers
    @FXML
    private Circle balloonCircle1; // 🔹 changed
    @FXML
    private Circle balloonCircle2; // 🔹 changed
    @FXML
    private Circle balloonCircle3; // 🔹 changed

    @FXML
    private Label emailLabel; // Label from FXML

    // 🔹 New: Texts for the 3 balloon numbers
    @FXML
    private Text balloonNum1;
    @FXML
    private Text balloonNum2;
    @FXML
    private Text balloonNum3;

    // 🔹 Score and Remaining labels
    @FXML
    private Label scoreLabel;
    @FXML
    private Label remainingLabel;

    private String currentUserEmail;
    private final Random random = new Random(); // 🔹 Added

    // 🔹 Track score and remaining rounds
    private int score = 0;
    private int remaining = 20;
    private int targetNumber;

    // 🔹 Called from MainController when opening the game
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        if (emailLabel != null) {
            emailLabel.setText("Player Email: " + email);
        }
    }

    @FXML
    private void initialize() {

        updateLabels();
        generateRound();

        // 🔹 Set button action
        startGameBtn.setOnAction(e -> {
            System.out.println("Balloon Game Started!");
        });

        // 🔹 FIXED: Circles are now clickable instead of texts
        balloonCircle1.setOnMouseClicked(e -> checkBalloon(balloonNum1)); // 🔹 changed
        balloonCircle2.setOnMouseClicked(e -> checkBalloon(balloonNum2)); // 🔹 changed
        balloonCircle3.setOnMouseClicked(e -> checkBalloon(balloonNum3)); // 🔹 changed
    }

    // 🔹 Start a new round with fresh numbers
    private void generateRound() {
        if (remaining <= 0) {
            startGameBtn.setText("Game Over!");
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
        if (remaining <= 0) return;

        int clickedNum = Integer.parseInt(clickedBalloon.getText());
        if (clickedNum == targetNumber) {
            score++;
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
