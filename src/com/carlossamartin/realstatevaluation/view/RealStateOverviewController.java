package com.carlossamartin.realstatevaluation.view;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Location;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaResponse;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaRestClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class RealStateOverviewController {

    @FXML
    private TextField searchField;

    @FXML
    private Label formattedAddress;

    @FXML
    private List<Label> sample0;

    @FXML
    private List<Label> sample1;

    @FXML
    private List<Label> sample2;

    IdealistaRestClient idealistaClient;
    GeocodingRestClient geocodingClient;

    // Reference to the main application.
    private MainApp mainApp;

    public RealStateOverviewController() {
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleSearch()
    {
        geocodingClient = new GeocodingRestClient();
        idealistaClient = new IdealistaRestClient();

        String address = searchField.getText();
        Place place = geocodingClient.getPlace(address);
        formattedAddress.setText(place.getFormattedAddress());

        Location location = place.getGeometry().getLocation();
        String coordinates = String.format("%s,%s", location.getLat(), location.getLng());
        IdealistaResponse idealistaResponse = idealistaClient.getSamples(coordinates, "300");

        fillSample(sample0, idealistaResponse.getElementList().get(0));
        fillSample(sample1, idealistaResponse.getElementList().get(1));
        fillSample(sample2, idealistaResponse.getElementList().get(2));

    }

    private void fillSample(List<Label> sample, Home home) {
        sample.get(0).setText(home.getPropertyCode());
        sample.get(1).setText(home.getPrice().toString());
        sample.get(2).setText(home.getSize().toString());
        sample.get(3).setText(home.getRooms().toString());
        sample.get(4).setText(home.getBathrooms().toString());
        sample.get(5).setText(home.getAddress().toString());
        sample.get(6).setText(home.getUrl().toString());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
