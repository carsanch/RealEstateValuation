/*
 * Real Estate Valuation Project
 *
 * Copyright © 2018 Carlos Sánchez Martín (carlos.samartin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carlossamartin.realestatevaluation.controller;

import com.carlossamartin.realestatevaluation.MainApp;
import com.carlossamartin.realestatevaluation.model.HomeTableWrapper;
import com.carlossamartin.realestatevaluation.utils.TableViewUtils;
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
        File file = mainApp.getHomeFilePath();
        if (file != null) {
            HomeTableWrapper wrapper = mainApp.loadWrapperFromTable();
            mainApp.savePersonDataToFile(file, wrapper);
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
            mainApp.savePersonDataToFile(file, wrapper);
        }
    }

    @FXML
    private void handleCopy() {
        TableViewUtils.copySelectedToClipBoard(this.mainApp.getHomeTable());
    }

    @FXML
    private void handleCopyAll() {
        this.mainApp.getHomeTable().getSelectionModel().selectAll();
        TableViewUtils.copySelectedToClipBoard(this.mainApp.getHomeTable(), true);
        this.mainApp.getHomeTable().getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Real Estate Valuation");
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
