package com.carlossamartin.realstatevaluation;

import com.carlossamartin.realstatevaluation.model.idealista.HomeTable;
import com.carlossamartin.realstatevaluation.controller.RealStateOverviewController;
import com.carlossamartin.realstatevaluation.controller.RootLayoutController;
import com.carlossamartin.realstatevaluation.controller.SettingsViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private TableView<HomeTable> homeTable;
    public TableView<HomeTable> getHomeTable() {
        return homeTable;
    }
    public void setHomeTable(TableView<HomeTable> homeTable) {
        this.homeTable = homeTable;
    }

    private Preferences preferences;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RealStateValuation");

        preferences = Preferences.userNodeForPackage(MainApp.class);

        initRootLayout();
        showRealStateOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.init(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showRealStateOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RealStateOverview.fxml"));
            AnchorPane realStateOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(realStateOverview);

            // Give the controller access to the main app.
            RealStateOverviewController controller = loader.getController();
            controller.init(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showSettingView()     {
        try {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/SettingsView.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Preferences");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Set the person into the controller.
        SettingsViewController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.init(this);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
