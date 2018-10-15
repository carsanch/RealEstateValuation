package com.carlossamartin.realstatevaluation.controller;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.HomeTableWrapper;
import com.carlossamartin.realstatevaluation.utils.TableViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import java.io.File;

public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;
    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("Application Started.");
    }

    @FXML
    private void handleNew() {
        this.mainApp.clearData();
        this.mainApp.setHomeFilePath(null);
    }

    @FXML
    private void handleSettings() {
        this.mainApp.showSettingView();
    }
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadWrapperFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File personFile = mainApp.getHomeFilePath();
        if (personFile != null) {
            HomeTableWrapper wrapper = mainApp.loadWrapperFromTable();
            mainApp.savePersonDataToFile(personFile, wrapper);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            HomeTableWrapper wrapper = mainApp.loadWrapperFromTable();
            mainApp.savePersonDataToFile(file,wrapper);
        }
    }

    @FXML
    private void handleCopy()
    {
        TableViewUtils.copySelectedToClipBoard(this.mainApp.getHomeTable());
    }

    @FXML
    private void handleCopyAll()
    {
        this.mainApp.getHomeTable().getSelectionModel().selectAll();
        TableViewUtils.copySelectedToClipBoard(this.mainApp.getHomeTable(), true);
        this.mainApp.getHomeTable().getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Real State Valuation");
        alert.setHeaderText("About");
        alert.setContentText("Author: Carlos Sánchez\nEmail: carlos.samartin@gmail.com");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
