package com.example.latihan2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerLink;
    @FXML
    private ImageView logoImage;

    private AllScenes allScenes;
    public void init(AllScenes allScenes) {
        this.allScenes = allScenes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage("/images/Logo.png");
        passwordField.setOnAction(event -> handleLoginAction());
        registerLink.setOnAction(event -> handleRegisterAction());
    }
    @FXML
    private void handleLoginAction(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields");
            return;
        }

        UserModel user = allScenes.getAllUser().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password");
        } else {
            allScenes.setLoggedInUser(user); // Set the logged-in user in MainApp
            if (user.isAdmin()) {
                allScenes.switchToAdminDashboardScene();
            } else {
                allScenes.switchToUserDashboardScene();
            }
        }
    }
    @FXML
    private void handleRegisterAction(){
        allScenes.switchToSignupScene();
    }
    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        logoImage.setImage(image);
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
