package com.example.latihan2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllScenes {

    private Stage primaryStage;
    private ObservableList<AduanModel> daftarAduan = FXCollections.observableArrayList();
    private ObservableList<UserModel> daftarUser = FXCollections.observableArrayList();
    private List<UserModel> users = new ArrayList<>();
    private AduanModel selectedAduan;
    private AddAduanController addAduanController;
    private UserDashboardController userDashboardController;
    private AdminDashboardController adminDashboardController;
    private ArahkanMasalahController arahkanMasalahController;
    private AdminOverviewController adminOverviewController;
    private LoginController loginController;
    private SignupController signupController;
    private UserModel loggedInUser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void loadUserData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/CSV/users.csv"));
            for (String line : lines) {
                String[] values = line.split(",");
                String username = values[0];
                String name = values[1];
                String password = values[2];
                boolean isAdmin = values[3].equalsIgnoreCase("yes");
                users.add(new UserModel(username, name, password, isAdmin));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadCSVData() {
        CSVRowMapper<AduanModel> mapper = values -> new AduanModel(values[0], values[1], values[2], values[3], values[4], values[5]);
        CSVReader<AduanModel> csvReader = new CSVReader<>("/CSV/aduan.csv", mapper);
        List<AduanModel> aduanList = csvReader.readCSV();
        daftarAduan.setAll(aduanList);
    }
    public void switchToLoginScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("Login.fxml"));
            Parent root = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.init(this);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/login.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public void switchToSignupScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("Signup.fxml"));
            Parent root = fxmlLoader.load();
            signupController = fxmlLoader.getController();
            signupController.init(this);
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/CSS/signup.css").toExternalForm());
            primaryStage.setTitle("Urbanify - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    public void switchToAddAduanScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("AddAduan.fxml"));
            Parent root = fxmlLoader.load();
            addAduanController = fxmlLoader.getController();
            addAduanController.init(this);
            addAduanController.setUserDashboardController(userDashboardController);
            addAduanController.setLoggedInUser(loggedInUser);
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
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("AdminOverview.fxml"));
            Parent root = fxmlLoader.load();
            adminOverviewController = fxmlLoader.getController();
            adminOverviewController.init(this, daftarAduan);
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
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("UserDashboard.fxml"));
            Parent root = fxmlLoader.load();
            userDashboardController = fxmlLoader.getController();
            userDashboardController.init(this, daftarAduan);
            userDashboardController.refreshTableData();
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
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("AdminDashboard.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(AllScenes.class.getResource("ArahkanMasalah.fxml"));
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
    public ObservableList<UserModel> getDaftarUser() {
        return daftarUser;
    }
    public List<UserModel> getUsers() {
        return users;
    }
    public void setLoggedInUser(UserModel loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public UserModel getLoggedInUser() {
        return loggedInUser;
    }
    public ObservableList<UserModel> getAllUser(){
        ObservableList<UserModel> combinedList = FXCollections.observableArrayList();
        combinedList.addAll(daftarUser);
        combinedList.addAll(users);
        return combinedList;
    }
}
