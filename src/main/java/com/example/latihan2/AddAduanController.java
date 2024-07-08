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
import java.util.ArrayList;
import java.util.List;
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
    private TextField rincianAduanArea;
    @FXML
    private ImageView uploadFotoImageView;
    @FXML
    private Button batalButton;
    @FXML
    private Button simpanButton;

    private UserDashboardController userDashboardController; // Inject UserDashboardController
    private File selectedImageFile;
    private File copiedImageFile;
    private CSVReader<AduanModel> csvReader;
    private List<AduanModel> aduanModelList;
    private CSVRowMapper<AduanModel> csvRowMapper;
    private MainApp mainApp;
    private UserModel loggedInUser;

    public AddAduanController(){
        this.csvReader = new CSVReader<>("aduan.csv", this::mapToAduanModel);
        this.csvRowMapper = this::mapToAduanModel;
        this.aduanModelList = new ArrayList<>();
    }
    public void setUserDashboardController(UserDashboardController userDashboardController) {
        this.userDashboardController = userDashboardController;
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

        kategoriComboBox.getItems().addAll("Infrastruktur", "Transportasi", "Lingkungan", "Layanan Umum");
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
        String csvData = gatherFormData();
        if (csvData != null) {
            writeAduanToCSV(csvData);
        }
    }
    public void setLoggedInUser(UserModel loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    private String gatherFormData() {
        String profil = loggedInUser.getUsername(); // Use the logged-in user's name
        String judul = judulField.getText();
        String kategori = kategoriComboBox.getValue();
        if (judul.isEmpty() || kategori == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in the required fields.");
            return null;
        }
        String informasiUmum = judul + " - " + kategori;
        String kota = kotaField.getText();
        LocalDate waktu = waktuField.getValue();
        String rtRw = rtRwField.getText();
        String waktuTempat = kota + " " + (waktu != null ? waktu.toString() : "") + " " + rtRw;
        String tautanCepat = copiedImageFile != null ? copiedImageFile.getAbsolutePath() : "";
        String status = "Belum Diserahkan";
        String detil = rincianAduanArea.getText();

        return profil + "," + informasiUmum + "," + waktuTempat + "," + tautanCepat + "," + status + "," + detil;
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
            AduanModel newAduanModel = mapToAduanModel(csvData.split(","));
            mainApp.addAduan(newAduanModel); // Add to daftarAduan list
//            userDashboardController.updateTableData(newAduanModel);
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

            // Generate a unique file name
            String uniqueFileName = System.currentTimeMillis() + "_" + file.getName();
            Path destinationPath = resourcesDir.resolve(uniqueFileName);

            // Copy the file to the destination path
            Files.copy(file.toPath(), destinationPath);

            // Update copiedImageFile to reflect the new path
            copiedImageFile = destinationPath.toFile();

        } catch (IOException e) {
            e.printStackTrace();
            // Notify user of error
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to copy image.");
        }
    }
    private void switchToUserDashboardScene() {
        mainApp.switchToUserDashboardScene();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
