package com.example.latihan2;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    private AllScenes allScenes;
    @Override
    public void start(Stage primaryStage) throws Exception {
        allScenes = new AllScenes();
        allScenes.setPrimaryStage(primaryStage);
        allScenes.loadCSVData();
        allScenes.loadUserData();
        allScenes.switchToLoginScene();
    }
}
