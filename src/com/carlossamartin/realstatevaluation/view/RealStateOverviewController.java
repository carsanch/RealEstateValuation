package com.carlossamartin.realstatevaluation.view;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Location;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.carlossamartin.realstatevaluation.model.idealista.HomeTable;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaResponse;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaRestClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class RealStateOverviewController {

    @FXML
    private TextField searchField;

    @FXML
    private TextField distanceField;

    @FXML
    private Label formattedAddress;

    @FXML
    private TableView<HomeTable> homeTable;

    @FXML
    private TableColumn<HomeTable, Integer> distanceColumn;
    @FXML
    private TableColumn<HomeTable, String> propertyCodeColumn;
    @FXML
    private TableColumn<HomeTable, Long> priceColumn;
    @FXML
    private TableColumn<HomeTable, Integer> sizeColumn;
    @FXML
    private TableColumn<HomeTable, String> addressColumn;
    @FXML
    private TableColumn<HomeTable, String> urlColumn;


    private IdealistaRestClient idealistaClient;
    private GeocodingRestClient geocodingClient;

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

        String distance = "".equals(distanceField.getText()) ? distanceField.getPromptText() : distanceField.getText();
        IdealistaResponse idealistaResponse = idealistaClient.getSamples(coordinates, distance);

        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("distance"));
        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("propertyCode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Long>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("size"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("address"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("url"));

        /*ObservableList<HomeTable> data =
                FXCollections.observableArrayList(
                        new HomeTable(idealistaResponse.getElementList().get(0)),
                        new HomeTable(idealistaResponse.getElementList().get(1)),
                        new HomeTable(idealistaResponse.getElementList().get(2)),
                        new HomeTable(idealistaResponse.getElementList().get(3)),
                        new HomeTable(idealistaResponse.getElementList().get(4))
                );*/

        List<HomeTable> list = new ArrayList<>();
        for(Home item : idealistaResponse.getElementList())
        {
            if(item.getShowAddress()) {
                list.add(new HomeTable(item));
            }
        }
        ObservableList<HomeTable> data = FXCollections.observableArrayList(list);
        homeTable.setItems(data);
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
