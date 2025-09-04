package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HomeController {

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
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();
    }
}