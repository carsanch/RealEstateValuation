package com.carlossamartin.realstatevaluation;

import com.carlossamartin.realstatevaluation.model.HomeTable;
import com.carlossamartin.realstatevaluation.model.HomeTableWrapper;
import com.carlossamartin.realstatevaluation.controller.RealStateOverviewController;
import com.carlossamartin.realstatevaluation.controller.RootLayoutController;
import com.carlossamartin.realstatevaluation.controller.SettingsViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private final static String TITLE_APP = "RealStateValuation";

    private Stage primaryStage;
    private BorderPane rootLayout;

    private RealStateOverviewController realStateOverviewController;

    private Preferences preferences;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(TITLE_APP);

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
            realStateOverviewController = loader.getController();
            realStateOverviewController.init(this);

            // Try to load last opened person file.
            File file = getHomeFilePath();
            if (file != null) {
                HomeTableWrapper wrapper = loadWrapperFromFile(file);

                if(wrapper != null)
                {
                    realStateOverviewController.setData(wrapper);
                }
            }

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

    public void clearData() {
        realStateOverviewController.clearData();
    }

    public HomeTableWrapper loadWrapperFromFile(File file) {

        HomeTableWrapper wrapper = null;
        try {
            JAXBContext context = JAXBContext
                    .newInstance(HomeTableWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            wrapper = (HomeTableWrapper) um.unmarshal(file);

            // Save the file path to the registry.
            setHomeFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }

        return wrapper;
    }

    public void savePersonDataToFile(File file, HomeTableWrapper wrapper) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(HomeTableWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setHomeFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public File getHomeFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setHomeFilePath(File file) {

        if (file != null) {
            preferences.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle(TITLE_APP + " - " + file.getName());
        } else {
            preferences.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle(TITLE_APP);
        }
    }

    public HomeTableWrapper loadWrapperFromTable() {
         return realStateOverviewController.getData();
    }

    public TableView<HomeTable> getHomeTable() {
        return realStateOverviewController.getHomeTable();
    }
}
