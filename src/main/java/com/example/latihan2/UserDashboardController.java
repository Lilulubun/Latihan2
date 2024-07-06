package com.example.latihan2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.application.Application.launch;

public class UserDashboardController implements Initializable {
    @FXML
    public ImageView userDashboardBG;
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
    private TextField searchInput;
    @FXML
    private Button searchButton;
    @FXML
    private Button addAduanButton;
    private ObservableList<AduanModel> aduanList = FXCollections.observableArrayList();
    private List<AduanModel> aduanModelList = new ArrayList<>();
    private MainApp mainApp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageUrl = "/images/UserDashboard.png";
        setImage(imageUrl);
        // Initialize table columns
        initializeTableColumns();
        // Load data from CSV
//        loadCSVDataToList();
//        tableView.setItems(aduanList);
        addAduanButton.setOnAction(event -> handleAddAduanAction());
        // Set search button action
        searchButton.setOnAction(event -> filterData());
        searchInput.setOnAction(event -> filterData());
    }
    private void loadCSVDataToList() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        aduanModelList.addAll(csvReader.readCSV());
        aduanList.setAll(aduanModelList);
    }
    public void updateTableData(AduanModel newAduanModel) {
        aduanList.add(newAduanModel);
    }
    public void init(MainApp mainApp, ObservableList<AduanModel> daftarAduan) {
        this.mainApp = mainApp;
        this.aduanList = daftarAduan;
        tableView.setItems(aduanList);
    }
    public void refreshTableData() {
        tableView.refresh();
    }
    private void filterData() {
        String searchText = searchInput.getText().toLowerCase();
        if (searchText.isEmpty()) {
            tableView.setItems(aduanList);
        } else {
            List<AduanModel> filteredList = aduanList.stream()
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

    public void initializeTableColumns() {
        profilColumn.setCellValueFactory(new PropertyValueFactory<>("profil"));
        judulColumn.setCellValueFactory(new PropertyValueFactory<>("judul"));
        waktuTempatColumn.setCellValueFactory(new PropertyValueFactory<>("waktuTempat"));
        tautanCepatColumn.setCellValueFactory(new PropertyValueFactory<>("tautanCepat"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        detilColumn.setCellValueFactory(new PropertyValueFactory<>("detil"));
    }

    // Method to set image to ImageView
    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        userDashboardBG.setImage(image);
    }

    public void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        aduanList.setAll(csvReader.readCSV());
        //List<AduanModel> aduanList = csvReader.readCSV();
        tableView.setItems(aduanList);
    }
    public void handleAddAduanAction() {
        mainApp.switchToAddAduanScene();
    }
}

