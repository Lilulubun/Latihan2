package com.example.latihan2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    @FXML private Button logOutButton;
    private ObservableList<AduanModel> aduanList = FXCollections.observableArrayList();
    private List<AduanModel> aduanModelList = new ArrayList<>();
    private AllScenes allScenes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage("/images/UserDashboard.png");
        initializeTableColumns();
        addAduanButton.setOnAction(event -> handleAddAduanAction());
        searchButton.setOnAction(event -> filterData());
        searchInput.setOnAction(event -> filterData());
        deleteAduanButton.setOnAction(event -> handleDeleteAduanAction());
        logOutButton.setOnAction(event -> handleLogoutAction());
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

        profilColumn.setCellFactory(column -> new WrappingTableCell<>());
        judulColumn.setCellFactory(column -> new WrappingTableCell<>());
        waktuTempatColumn.setCellFactory(column -> new WrappingTableCell<>());
        tautanCepatColumn.setCellFactory(column -> new TableCell<AduanModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Path path = Paths.get(item);
                    setText(path.getFileName().toString());
                    setStyle("-fx-text-fill: blue; -fx-underline: true;");
                    setOnMouseClicked((MouseEvent event) -> {
                        if (event.getClickCount() == 1 && !isEmpty()) {
                            showTautanCepat(item);
                        }
                    });
                }
            }
        });
        statusColumn.setCellFactory(column -> new WrappingTableCell<>());
        detilColumn.setCellFactory(column -> new WrappingTableCell<>());
    }
    private void showTautanCepat(String imagePath) {
        Stage stage = new Stage();
        ImageView imageView = new ImageView(new Image("file:" + imagePath));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600); // Adjust the width as needed
        imageView.setFitHeight(400); // Adjust the height as needed

        StackPane pane = new StackPane();
        pane.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);

        Scene scene = new Scene(pane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Image Viewer");
        stage.initModality(Modality.APPLICATION_MODAL); // Block other windows until this one is closed
        stage.showAndWait();
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
    public void handleAddAduanAction() {
        allScenes.switchToAddAduanScene();
    }
    public void setUserName(String userName) {
        userNameLabel.setText(userName);
    }
    private void handleLogoutAction() {
        allScenes.switchToLoginScene();
    }
}

