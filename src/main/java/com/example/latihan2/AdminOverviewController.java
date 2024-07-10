package com.example.latihan2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdminOverviewController implements Initializable {
    @FXML
    private ImageView adminOverviewBG;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button overviewButton;
    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private PieChart complaintChart;

    @FXML
    private StackPane chartWrapper;

    private AllScenes allScenes;
    private ObservableList<AduanModel> daftarAduan;

    public void init(AllScenes allScenes, ObservableList<AduanModel> daftarAduan) {
        this.allScenes = allScenes;
        this.daftarAduan = daftarAduan;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage("/images/AdminDashboard.png");
        dashboardButton.setOnAction(this::handleDashboardAction);
        overviewButton.setOnAction(this::handleOverviewAction);
        setupComboBox();
        loadCSVData();
    }
    private void setupComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList("status", "kategori", "kota"));
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateChart(newValue); // Populate chart based on selected filter
        });
    }
    private void populateChart(String filterType) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Count occurrences based on the selected filter
        List<String> categories = daftarAduan.stream()
                .map(getGroupingFunction(filterType))
                .distinct()
                .collect(Collectors.toList());

        // Create PieChart.Data for each category
        categories.forEach(category -> {
            long count = daftarAduan.stream()
                    .filter(aduan -> getGroupingFunction(filterType).apply(aduan).equals(category))
                    .count();
            PieChart.Data data = new PieChart.Data(category, count);
            pieChartData.add(data);
        });

        complaintChart.setData(pieChartData);
        complaintChart.setTitle("Aduan by " + filterType);
        complaintChart.setLegendVisible(true);
    }
    private List<AduanModel> filterData(String filterType, String filterValue) {
        // Implement logic to filter data based on filterType and filterValue
        List<AduanModel> filteredList = daftarAduan.stream()
                .filter(aduan -> {
                    switch (filterType) {
                        case "status":
                            return aduan.getStatus().equalsIgnoreCase(filterValue);
                        case "kategori":
                            return aduan.getKategori().equalsIgnoreCase(filterValue);
                        case "kota":
                            return aduan.getKota().equalsIgnoreCase(filterValue);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        return filteredList;
    }
    private Function<AduanModel, String> getGroupingFunction(String filterType) {
        switch (filterType) {
            case "status":
                return AduanModel::getStatus;
            case "kategori":
                return AduanModel::getKategori;
            case "kota":
                return AduanModel::getKota;
            default:
                return AduanModel::getStatus; // Default to status if unknown filter type
        }
    }
    private void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(
                values[0], // Profil
                values[1], // Informasi Umum
                values[2], // Waktu Tempat
                values[3], // Tautan Cepat
                values[4], // Status
                values[5]  // Detil
        );

        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        List<AduanModel> aduanList = csvReader.readCSV();
        daftarAduan = FXCollections.observableArrayList(aduanList);
    }
    private void setImage(String imageUrl) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        adminOverviewBG.setImage(image);
    }
    private void handleDashboardAction(ActionEvent actionEvent){
        allScenes.switchToAdminDashboardScene();
    }
    private void handleOverviewAction(ActionEvent actionEvent){
        allScenes.switchToAdminOverviewScene();
    }
}
