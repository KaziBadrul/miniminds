package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import java.io.IOException;

public class HomeController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    private AudioClip hoverSound;

    @FXML
    private void initialize() {
        // Load your hover sound
        hoverSound = new AudioClip(getClass().getResource("/com/example/miniminds/sounds/rectangle-ding.mp3").toExternalForm());

        // Add hover effects
        loginButton.setOnMouseEntered(e -> hoverSound.play());
        signupButton.setOnMouseEntered(e -> hoverSound.play());
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        switchScene(event, "login.fxml");
    }

    @FXML
    private void handleSignup(ActionEvent event) throws IOException {
        switchScene(event, "signup.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        VBox root = loader.load();
        Scene scene = new Scene(root, Session.getWIDTH(), Session.getHEIGHT());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
