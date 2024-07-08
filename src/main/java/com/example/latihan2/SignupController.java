package com.example.latihan2;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signupButton;
    @FXML
    private ImageView signupBG;
    private MainApp mainApp;
    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage("/images/Signup.png");
        // Set action for signup button
        signupButton.setOnAction(event -> {
            // Validate input
            if (validateInput()) {
                saveToMemory();
                // Save data to CSV
                saveToCSV();
                // Optionally, navigate to another scene or perform other actions
                mainApp.switchToLoginScene();
            }
        });
    }
    private void saveToCSV() {
        String username = usernameField.getText();
        String email = nameField.getText();
        String password = passwordField.getText();
        boolean isAdmin = false;

        // Assuming CSV structure: username,email,password
        String csvLine = username + "," + email + "," + password + ","+ isAdmin+"\n";
        String csvFile = "src/main/resources/CSV/users.csv";  // Adjusted CSV file path

        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(csvLine);
            showAlert("Sukses","Berhasil Mendaftar");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show error message to user)
        }
    }
    private void saveToMemory(){
        String username = usernameField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        UserModel newUser = new UserModel(username, name, password, false);
        mainApp.getDaftarUser().add(newUser);
    }
    private boolean validateInput() {
        String username = usernameField.getText();
        String email = nameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if any field is empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Optionally, show an error message or indication to the user
            showAlert("Gagal Mendaftar","Isi terlebih dahulu");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            // Optionally, show an error message or indication to the user
            showAlert("Gagal Mendaftar","Password tidak cocok");
            return false;
        }

        // Check if username is unique
        if (!isUsernameUnique(username)) {
            // Optionally, show an error message or indication to the user
            showAlert("Gagal Mendaftar","Nama Pengguna Sudah ada");
            return false;
        }

        // You can add more specific validation logic here, such as checking email format

        return true;  // Input is valid
    }

    private boolean isUsernameUnique(String newUsername) {
        String csvFile = "src/main/resources/CSV/users.csv";  // Adjusted CSV file path

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String existingUsername = userData[0].trim(); // Assuming username is the first field in CSV

                // Check if the new username already exists in CSV
                if (existingUsername.equals(newUsername)) {
                    return false; // Username exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show error message to user)
        }

        return true; // Username is unique
    }
    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        signupBG.setImage(image);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
