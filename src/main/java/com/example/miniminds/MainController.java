package com.example.miniminds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    private void showDashboard() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Text("Dashboard content goes here."));
    }

    @FXML
    private void showProfile() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Text("Profile content goes here."));
    }

    @FXML
    private void showSettings() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Text("Settings content goes here."));
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        System.out.println("User logged out.");
    }
}