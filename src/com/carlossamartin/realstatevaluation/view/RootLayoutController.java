package com.carlossamartin.realstatevaluation.view;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.idealista.HomeTable;
import com.carlossamartin.realstatevaluation.view.utils.TableViewUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.StageStyle;

public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNew() {
        this.mainApp.getHomeTable().getItems().clear();
    }

    @FXML
    private void handleOpen() {
        //TODO
    }

    @FXML
    private void handleSave() {
        //TODO
    }

    @FXML
    private void handleSaveAs() {
        //TODO
    }

    @FXML
    private void handleCopy()
    {
        TableViewUtils.copyToClipBoard(this.mainApp.getHomeTable());
    }

    @FXML
    private void handleCopyAll()
    {
        this.mainApp.getHomeTable().getSelectionModel().selectAll();
        TableViewUtils.copyToClipBoard(this.mainApp.getHomeTable());
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
