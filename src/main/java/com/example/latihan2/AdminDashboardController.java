package com.example.latihan2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
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
    @FXML
    private Button dashboardButton;
    @FXML
    private Button overviewButton;
    @FXML private Button logOutButton;
    private AllScenes allScenes;
    private ObservableList<AduanModel> daftarAduan;



    public void init(AllScenes allScenes, ObservableList<AduanModel> daftarAduan) {
        this.allScenes = allScenes;
        this.daftarAduan = daftarAduan;
        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage("/images/AdminDashboard.png");
        initializeTableColumns();
        searchButton.setOnAction(event -> filterData());
        searchInput.setOnAction(event -> filterData());
        arahkanMasalahButton.setOnAction(this::handleArahkanMasalahAction);
        dashboardButton.setOnAction(this::handleDashboardAction);
        overviewButton.setOnAction(this::handleOverviewAction);
        logOutButton.setOnAction(event -> handleLogoutAction());
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
            allScenes.switchToArahkanMasalahScene(selectedAduan);
        }

    }
    private void handleDashboardAction(ActionEvent actionEvent) {
        allScenes.switchToAdminDashboardScene();
    }
    private void handleOverviewAction(ActionEvent actionEvent){
        allScenes.switchToAdminOverviewScene();
    }
    private void handleLogoutAction() {
        allScenes.switchToLoginScene();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((String) null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
