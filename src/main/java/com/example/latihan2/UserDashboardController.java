package com.example.latihan2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @FXML
    private Button deleteAduanButton;
    @FXML
    private Label userNameLabel;
    private ObservableList<AduanModel> aduanList = FXCollections.observableArrayList();
    private List<AduanModel> aduanModelList = new ArrayList<>();
    private AllScenes allScenes;

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
        deleteAduanButton.setOnAction(event -> handleDeleteAduanAction());
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
    public void init(AllScenes allScenes, ObservableList<AduanModel> daftarAduan) {
        this.allScenes = allScenes;
        UserModel loggedInUser = allScenes.getLoggedInUser();
        if (loggedInUser != null) {
            String userName = loggedInUser.getName().toLowerCase();
            ObservableList<AduanModel> filteredList = FXCollections.observableArrayList(
                    daftarAduan.stream()
                            .filter(aduan -> aduan.getProfil().toLowerCase().contains(userName))
                            .collect(Collectors.toList())
            );
            this.aduanList = filteredList;
            tableView.setItems(this.aduanList);
            setUserName(loggedInUser.getName());
        } else {
            // Handle the case when there is no logged-in user (optional)
            this.aduanList = daftarAduan;
            tableView.setItems(this.aduanList);
        }
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
    @FXML
    private void handleDeleteAduanAction() {
        AduanModel selectedAduan = tableView.getSelectionModel().getSelectedItem();
        if (selectedAduan != null) {
            aduanList.remove(selectedAduan);
            aduanModelList.remove(selectedAduan);
            saveDataToCSV();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Aduan Selected");
            alert.setContentText("Please select an aduan in the table.");
            alert.showAndWait();
        }
    }
    private void saveDataToCSV() {
        Path filePath = Paths.get("src/main/resources/CSV/aduan.csv");
        List<String> dataLines = aduanModelList.stream()
                .map(aduan -> String.join(",",
                        aduan.getProfil(),
                        aduan.getJudul(),
                        aduan.getWaktuTempat(),
                        aduan.getTautanCepat(),
                        aduan.getStatus(),
                        aduan.getDetil()))
                .collect(Collectors.toList());

        try {
            Files.write(filePath, dataLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        aduanList.setAll(csvReader.readCSV());
        //List<AduanModel> aduanList = csvReader.readCSV();
        tableView.setItems(aduanList);
    }
    public void handleAddAduanAction() {
        allScenes.switchToAddAduanScene();
    }
    public void setUserName(String userName) {
        userNameLabel.setText(userName);
    }
}

