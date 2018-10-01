package com.carlossamartin.realstatevaluation.view;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Location;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.carlossamartin.realstatevaluation.model.idealista.AgencyEnum;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.carlossamartin.realstatevaluation.model.idealista.HomeTable;
import com.carlossamartin.realstatevaluation.restclient.idealista.ParsingAgencyClient;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaResponse;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaRestClient;
import com.carlossamartin.realstatevaluation.view.utils.EditCell;
import com.carlossamartin.realstatevaluation.view.utils.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;

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
    private TableColumn<HomeTable, Integer> idColumn;
    @FXML
    private TableColumn<HomeTable, Integer> distanceColumn;
    @FXML
    private TableColumn<HomeTable, String> propertyCodeColumn;
    @FXML
    private TableColumn<HomeTable, Double> priceColumn;
    @FXML
    private TableColumn<HomeTable, Double> sizeColumn;
    @FXML
    private TableColumn<HomeTable, Double> priceSizeColumn;
    @FXML
    private TableColumn<HomeTable, String> agencyColumn;
    @FXML
    private TableColumn<HomeTable, Double> factorColumn;
    @FXML
    private TableColumn<HomeTable, String> addressColumn;
    @FXML
    private TableColumn<HomeTable, String> urlColumn;

    @FXML
    private TableColumn<HomeTable, Double> latitudeColumn;
    @FXML
    private TableColumn<HomeTable, Double> longitudeColumn;


    private IdealistaRestClient idealistaClient;
    private GeocodingRestClient geocodingClient;

    // Reference to the main application.
    private MainApp mainApp;

    public RealStateOverviewController() {
    }

    @FXML
    public void initialize() {
        homeTable.getSelectionModel().setCellSelectionEnabled(true);
        homeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TableViewUtils.copySelectedToClipBoard(homeTable);
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        homeTable.setContextMenu(menu);
    }

    @FXML
    private void handleSearch()
    {
        if (null == this.mainApp || null == this.mainApp.getHomeTable()) {
            this.mainApp.setHomeTable(homeTable);
        }

        geocodingClient = new GeocodingRestClient();
        idealistaClient = new IdealistaRestClient();

        String address = searchField.getText();
        Place place = geocodingClient.getPlace(address);
        formattedAddress.setText(place.getFormattedAddress());

        Location location = place.getGeometry().getLocation();
        String coordinates = String.format("%s,%s", location.getLat(), location.getLng());

        String distance = "".equals(distanceField.getText()) ? distanceField.getPromptText() : distanceField.getText();
        IdealistaResponse idealistaResponse = idealistaClient.getSamples(coordinates, distance);

        idColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("id"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("distance"));
        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("propertyCode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("size"));
        priceSizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("priceSize"));

        agencyColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("agency"));

        addressColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("address"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("url"));

        latitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("longitude"));



        /*ObservableList<HomeTable> data =
                FXCollections.observableArrayList(
                        new HomeTable(idealistaResponse.getElementList().get(0)),
                        new HomeTable(idealistaResponse.getElementList().get(1)),
                        new HomeTable(idealistaResponse.getElementList().get(2)),
                        new HomeTable(idealistaResponse.getElementList().get(3)),
                        new HomeTable(idealistaResponse.getElementList().get(4))
                );*/

        List<HomeTable> list = new ArrayList<>();

        int idCounter = 0;
        for(Home item : idealistaResponse.getElementList())
        {
            if(item.getShowAddress()) {
                list.add(new HomeTable(++idCounter,item));
            }
        }
        ObservableList<HomeTable> data = FXCollections.observableArrayList(list);
        homeTable.setItems(data);

        setupFactorColumn();
        setTableEditable();

        Thread agencyFactorThread = new Thread() {
            public void run() {
                getAgencyAndFactor();
            }
        };
        agencyFactorThread.start();
    }

    private void getAgencyAndFactor() {
        ParsingAgencyClient parsingAgencyClient = new ParsingAgencyClient();

        for (HomeTable home : homeTable.getItems()) {
            AgencyEnum value = parsingAgencyClient.getProfessional(home.getUrl());
            home.setAgency(value.text());

            if(value.equals(AgencyEnum.PROFESSIONAL))
            {
                home.setFactor(0.93);
            }
            else if(value.equals(AgencyEnum.PRIVATE))
            {
                home.setFactor(0.97);
            }
            else {
                home.setFactor(1.00);
            }
        }
    }

    private void setupFactorColumn() {
        // sets the cell factory to use EditCell which will handle key presses
        // and firing commit events
        factorColumn.setCellFactory(EditCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        // updates the salary field on the PersonTableData object to the
        // committed value
        factorColumn.setOnEditCommit(event -> {
        final Double value = event.getNewValue() != null ?
                event.getNewValue() : event.getOldValue();
        ((HomeTable) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).setFactor(value);
        homeTable.refresh();
        });
    }


    private void setTableEditable() {
        homeTable.setEditable(true);
        // allows the individual cells to be selected
        homeTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        homeTable.setOnKeyPressed(event -> {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            editFocusedCell();
        }
        });
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition< HomeTable, ? > focusedCell = homeTable.focusModelProperty().get().focusedCellProperty().get();
        homeTable.edit(focusedCell.getRow(), focusedCell.getTableColumn());
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
