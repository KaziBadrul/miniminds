package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private AudioClip loginSound;

    @FXML
    private void handleLogin(ActionEvent event) {
        if (loginSound == null) {
            loginSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/login.wav").toExternalForm());
        }

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both email and password.");
            return;
        }

        String hashedPassword = hashPassword(password);

        if (DatabaseHelper.validateUser(email, hashedPassword)) {
            loginSound.play();
            Session.setCurrentUserEmail(email);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showAlert("Error loading main view.");
            }
        } else {
            showAlert("Invalid email or password.");
        }
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