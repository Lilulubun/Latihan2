package com.example.latihan2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AdminDashboardController implements Initializable {
    @FXML
    private TableView<AduanModel> tableView;
    @FXML
    private TableColumn<AduanModel, String> profilColumn;
    @FXML
    private TableColumn<AduanModel, String> judulColumn;
    @FXML
    private TableColumn<AduanModel, String> waktuTempatColumn;
    @FXML
    private TableColumn<AduanModel, String> tautanCepatColumn;
    @FXML
    private TableColumn<AduanModel, String> statusColumn;
    @FXML
    private TableColumn<AduanModel, String> detilColumn;
    @FXML
    private Button arahkanMasalahButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchInput;
    @FXML
    private ImageView adminDashboardBG;
    private MainApp mainApp;
    private ObservableList<AduanModel> daftarAduan;
    private static final String ADUAN_CSV = "/CSV/aduan.csv";
    private static final String DELETED_ADUAN_CSV = "/CSV/deleted_aduan.csv";



    public void init(MainApp mainApp, ObservableList<AduanModel> daftarAduan) {
        this.mainApp = mainApp;
        this.daftarAduan = daftarAduan;
        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageUrl = "/images/AdminDashboard.png";
        setImage(imageUrl);
        // Initialize table columns
        initializeTableColumns();
        // Load data from CSV
//        loadCSVData();
        // Set search button action
        searchButton.setOnAction(event -> filterData());
        searchInput.setOnAction(event -> filterData());
        arahkanMasalahButton.setOnAction(this::handleArahkanMasalahAction);
    }

    private void loadData() {
        tableView.setItems(daftarAduan);
    }
    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        adminDashboardBG.setImage(image);
    }

    public void initializeTableColumns() {
        profilColumn.setCellValueFactory(new PropertyValueFactory<>("profil"));
        judulColumn.setCellValueFactory(new PropertyValueFactory<>("judul"));
        waktuTempatColumn.setCellValueFactory(new PropertyValueFactory<>("waktuTempat"));
        tautanCepatColumn.setCellValueFactory(new PropertyValueFactory<>("tautanCepat"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        detilColumn.setCellValueFactory(new PropertyValueFactory<>("detil"));
    }

    public void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
//        aduanList.setAll(csvReader.readCSV());
        // Ensure the update happens on the JavaFX Application Thread
        List<AduanModel> aduanList = csvReader.readCSV();
        daftarAduan.setAll(aduanList);
        tableView.setItems(daftarAduan);
    }

    private void filterData() {
        String searchText = searchInput.getText().toLowerCase();
        if (searchText.isEmpty()) {
            tableView.setItems(daftarAduan);
        } else {
            List<AduanModel> filteredList = daftarAduan.stream()
                    .filter(aduan ->
                            aduan.getProfil().toLowerCase().contains(searchText) ||
                                    aduan.getJudul().toLowerCase().contains(searchText) ||
                                    aduan.getWaktuTempat().toLowerCase().contains(searchText) ||
                                    aduan.getTautanCepat().toLowerCase().contains(searchText) ||
                                    aduan.getStatus().toLowerCase().contains(searchText) ||
                                    aduan.getDetil().toLowerCase().contains(searchText)
                    )
                    .collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    public void handleArahkanMasalahAction(ActionEvent event) {
        AduanModel selectedAduan = (AduanModel) this.tableView.getSelectionModel().getSelectedItem();
        if (selectedAduan == null) {
            this.showAlert("No Selection", "Pilih salah satu Aduan untuk diarahkan");
        } else {
            mainApp.switchToArahkanMasalahScene(selectedAduan);
        }

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((String) null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
