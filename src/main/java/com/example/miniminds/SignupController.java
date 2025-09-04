package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SignupController {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Text categoryCue;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    @FXML
    public void initialize() {
        ageField.textProperty().addListener((obs, oldVal, newVal) -> updateCategoryCue(newVal));
    }

    private void updateCategoryCue(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            if (age > 17) {
                categoryCue.setText("You will be registered as a Parent");
                categoryCue.setStyle("-fx-fill: #007bff;");
            } else {
                categoryCue.setText("You will be registered as a Kid");
                categoryCue.setStyle("-fx-fill: #28a745;");
            }
        } catch (NumberFormatException e) {
            categoryCue.setText("");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert("Please enter a valid email address.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert("Age must be a number.");
            return;
        }

        String category = (age > 17) ? "parent" : "kid";
        String hashedPassword = hashPassword(password);

        DatabaseHelper.insertUser(name, age, email, hashedPassword, category);
//        DatabaseHelper.updateMathScore(email, 150);
        showAlert("Signup successful!");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            showAlert("Error hashing password.");
            return "";
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}