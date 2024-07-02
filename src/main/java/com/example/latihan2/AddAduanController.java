package com.example.latihan2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddAduanController implements Initializable {
    @FXML
    private ImageView addAduanBG;
    @FXML
    private ImageView logoImageView;
    @FXML
    private TextField judulField;
    @FXML
    private ComboBox<String> kategoriComboBox;
    @FXML
    private TextField kotaField;
    @FXML
    private DatePicker waktuField;
    @FXML
    private TextField rtRwField;
    @FXML
    private TextArea rincianAduanArea;
    @FXML
    private ImageView uploadFotoImageView;
    @FXML
    private ImageView uploadVideoImageView;
    @FXML
    private Button batalButton;
    @FXML
    private Button simpanButton;

    private UserDashboardController userDashboardController; // Inject UserDashboardController
    private File selectedImageFile;
    private File copiedImageFile;
    private CSVReader<AduanModel> csvReader;
    private CSVRowMapper<AduanModel> csvRowMapper;
    private MainApp mainApp;

    public AddAduanController(){
        this.csvReader = new CSVReader<>("aduan.csv", this::mapToAduanModel);
        this.csvRowMapper = this::mapToAduanModel;
    }

    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        addAduanBG.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageUrl = "/images/AddAduan.png";
        setImage(imageUrl);

        kategoriComboBox.getItems().addAll("Kategori 1", "Kategori 2", "Kategori 3");
        String imageUpload = "/images/UploadImage.png";
        setImageUpload(imageUpload);


        // Set action for the buttons
        batalButton.setOnAction(event -> clearForm());
        simpanButton.setOnAction(event -> saveFormData());
    }

    private void setImageUpload(String imageUpload){
        Image image = new Image(getClass().getResourceAsStream(imageUpload));
        uploadFotoImageView.setImage(image);
    }

    private void clearForm() {
        judulField.clear();
        kategoriComboBox.getSelectionModel().clearSelection();
        kotaField.clear();
        waktuField.getEditor().clear();
        rtRwField.clear();
        rincianAduanArea.clear();
        uploadFotoImageView.setImage(null); // Clear the image view
        selectedImageFile = null; // Clear the selected image file
    }

    private void saveFormData() {
        // Logic to save form data
        String profil = "default User";
        String judul = judulField.getText();
        String kategori = kategoriComboBox.getValue();
        String informasiUmum = judul+" - "+kategori;
        String kota = kotaField.getText();
        LocalDate waktu = waktuField.getValue();
        String rtRw = rtRwField.getText();
        String waktuTempat = kota + " " + (waktu != null ? waktu.toString() : "") + " " + rtRw;
        String tautanCepat = copiedImageFile != null ? copiedImageFile.getAbsolutePath() : "";
        String status = "Belum Diserahkan";
        String detil = rincianAduanArea.getText();

        String csvData = profil + "," + informasiUmum + "," + waktuTempat + "," + tautanCepat + "," + status + "," + detil;
        writeAduanToCSV(csvData);
    }
    private void writeAduanToCSV(String csvData) {
        Path csvPath = Paths.get("src/main/resources/CSV/aduan.csv");

        try {
            // Ensure the directory exists
            Path parentDir = csvPath.getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            // Write to the CSV file
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath.toString(), true));
            writer.write(csvData);
            writer.newLine(); // Write a new line for the next entry (if applicable)
            writer.close(); // Close the writer after use

            // Notify user of success
            showAlert(Alert.AlertType.INFORMATION, "Success", "Aduan berhasil disimpan!");
            switchToUserDashboardScene();

        } catch (IOException e) {
            e.printStackTrace();
            // Notify user of error
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan aduan. Silakan coba lagi.");
        }
    }
    private AduanModel mapToAduanModel(String[] row) {
        // Implement your mapping logic here to convert CSV row to Aduan object
        // Example:
        String profil = row[0];
        String informasiUmum = row[1];
        String waktuTempat = row[2];
        String tautanCepat = row[3];
        String status = row[4];
        String detil = row[5];

        return new AduanModel(profil, informasiUmum, waktuTempat, tautanCepat, status, detil);
    }
    @FXML
    private void handleUploadFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            uploadFotoImageView.setImage(image);
            // Copy the image to the resources directory
            copyImageToResources(file);
        }
    }
    private void copyImageToResources(File file) {
        try {
            // Define the destination directory inside the resources folder
            Path resourcesDir = Paths.get("src/main/resources/images");

            // Ensure the directory exists
            if (!Files.exists(resourcesDir)) {
                Files.createDirectories(resourcesDir);
            }

            // Create a destination file path
            copiedImageFile = new File(resourcesDir.toFile(), file.getName());

            // Copy the file
            Files.copy(file.toPath(), copiedImageFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    private void switchToUserDashboardScene() {
        mainApp.switchToUserDashboardScene();
//        if (mainApp != null && mainApp.getUserDashboardController() != null) {
//            mainApp.switchToUserDashboardScene();
//            mainApp.getUserDashboardController().refreshData();// Call switchToUserDashboardScene from MainApp
//        } else {
//            System.err.println("mainApp or userDashboardController is null. Cannot switch scene.");
//            // Handle null case if needed
//        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
