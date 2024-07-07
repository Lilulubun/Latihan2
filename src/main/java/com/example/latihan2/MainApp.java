package com.example.latihan2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private ObservableList<AduanModel> daftarAduan = FXCollections.observableArrayList();
    private AduanModel selectedAduan;
    private AddAduanController addAduanController;
    private UserDashboardController userDashboardController;
    private AdminDashboardController adminDashboardController;
    private ArahkanMasalahController arahkanMasalahController;
    private AdminOverviewController adminOverviewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        loadCSVData();
        switchToAdminDashboardScene();
    }
    private void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        List<AduanModel> aduanList = csvReader.readCSV();
        daftarAduan.setAll(aduanList);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void switchToAddAduanScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("AddAduan.fxml"));
            Parent root = fxmlLoader.load();
            addAduanController = fxmlLoader.getController();
            addAduanController.init(this);
            addAduanController.setUserDashboardController(userDashboardController);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/addAduan.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Solusi pasti keluhanmu");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public void switchToAdminOverviewScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("AdminOverview.fxml"));
            Parent root = fxmlLoader.load();
            adminOverviewController = fxmlLoader.getController();
            adminOverviewController.init(this);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/adminOverview.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Admin Overview");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }

    public void switchToUserDashboardScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("UserDashboard.fxml"));
            Parent root = fxmlLoader.load();
            userDashboardController = fxmlLoader.getController();
            userDashboardController.init(this, daftarAduan);
            userDashboardController.refreshTableData(); // Refresh da
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/userDashboard.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public void switchToAdminDashboardScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("AdminDashboard.fxml"));
            Parent root = fxmlLoader.load();
            adminDashboardController = fxmlLoader.getController();
            adminDashboardController.init(this, daftarAduan);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/adminDashboard.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public AdminDashboardController getAdminDashboardController() {
        return adminDashboardController;
    }
    public void switchToArahkanMasalahScene(AduanModel aduanModel) {
        try {
            this.selectedAduan = aduanModel;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ArahkanMasalah.fxml"));
            Parent root = fxmlLoader.load();
            arahkanMasalahController = fxmlLoader.getController();
            arahkanMasalahController.init(this, selectedAduan, daftarAduan);
            arahkanMasalahController.setAduanModel(selectedAduan);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/arahkanMasalah.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public void setSelectedAduan(AduanModel aduanModel) {
        this.selectedAduan = aduanModel;
    }
    public AduanModel getSelectedAduan() {
        return selectedAduan;
    }
    public void addAduan(AduanModel aduan) {
        daftarAduan.add(aduan);
    }
}
