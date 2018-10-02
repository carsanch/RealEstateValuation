package com.carlossamartin.realstatevaluation.view;

import com.carlossamartin.realstatevaluation.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.util.prefs.Preferences;

public class SettingsViewController {

    private static final String ROOT = "root";
    private static final String EMPTY = "root";
    private MainApp mainApp;
    private Stage dialogStage;

    @FXML
    private TextField ideApiKeyField;
    @FXML
    private TextField ideSecretField;
    @FXML
    private TextField googleApiKeyField;
    @FXML
    private TextField privateFactorField;
    @FXML
    private TextField professionalFactorField;


    private boolean okClicked = false;
    private Preferences pref;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        loadPref();
    }

    private void loadPref() {
        pref = this.mainApp.getPreferences();
        ideApiKeyField.setText(pref.get("ideApiKey", null));
        ideSecretField.setText(pref.get("ideSecret", null));
        googleApiKeyField.setText(pref.get("googleApiKey", null));
        privateFactorField.setText(pref.get("privateFactor", null));
        professionalFactorField.setText(pref.get("professionalFactor", null));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {

        pref.put("test","admin");

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }


}
