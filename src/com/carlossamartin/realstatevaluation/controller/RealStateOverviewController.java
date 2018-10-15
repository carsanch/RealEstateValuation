package com.carlossamartin.realstatevaluation.controller;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Location;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.carlossamartin.realstatevaluation.model.idealista.AgencyEnum;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.carlossamartin.realstatevaluation.model.HomeTable;
import com.carlossamartin.realstatevaluation.restclient.idealista.ParsingAgencyClient;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaResponse;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaRestClient;
import com.carlossamartin.realstatevaluation.utils.TableViewUtils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.util.prefs.Preferences;

public class RealStateOverviewController {

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private TextField sizeField;

    @FXML
    private TextField distanceField;

    @FXML
    private Label formattedAddress;

    @FXML
    private TextField factorAvgField;

    @FXML
    private TableView<HomeTable> homeTable;

    @FXML
    private TableColumn<HomeTable, Boolean> enabledColumn;
    @FXML
    private TableColumn<HomeTable, Integer> idColumn;
    @FXML
    private TableColumn<HomeTable, Integer> distanceColumn;
    @FXML
    private TableColumn<HomeTable, Double> distanceFactorColumn;
    @FXML
    private TableColumn<HomeTable, String> propertyCodeColumn;
    @FXML
    private TableColumn<HomeTable, Double> priceColumn;
    @FXML
    private TableColumn<HomeTable, Double> sizeColumn;
    @FXML
    private TableColumn<HomeTable, Double> sizeFactorColumn;
    @FXML
    private TableColumn<HomeTable, Double> priceSizeColumn;
    @FXML
    private TableColumn<HomeTable, String> agencyColumn;
    @FXML
    private TableColumn<HomeTable, Double> agencyFactorColumn;
    @FXML
    private TableColumn<HomeTable, String> addressColumn;
    @FXML
    private TableColumn<HomeTable, String> urlColumn;

    @FXML
    private TableColumn<HomeTable, Double> latitudeColumn;
    @FXML
    private TableColumn<HomeTable, Double> longitudeColumn;

    @FXML
    private TableColumn<HomeTable, Double> ageFactorColumn;
    @FXML
    private TableColumn<HomeTable, Double> qualityFactorColumn;
    @FXML
    private TableColumn<HomeTable, String> otherColumn;
    @FXML
    private TableColumn<HomeTable, Double> otherFactorColumn;

    @FXML
    private TableColumn<HomeTable, Double> standardPriceColumn;
    @FXML
    private TableColumn<HomeTable, Double> factorProductColumn;



    private IdealistaRestClient idealistaClient;
    private GeocodingRestClient geocodingClient;

    // Reference to the main application.
    private MainApp mainApp;

    private ObservableList<HomeTable> data;
    private IdealistaResponse idealistaResponse;
    private String coordinates;
    private String distance;

    public RealStateOverviewController() {
    }

    @FXML
    public void initialize() {
        geocodingClient = new GeocodingRestClient();
        idealistaClient = new IdealistaRestClient();

        homeTable.getSelectionModel().setCellSelectionEnabled(true);
        homeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem copyItemMenu = new MenuItem("Copy");
        copyItemMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TableViewUtils.copySelectedToClipBoard(homeTable);
            }
        });
        MenuItem deleteItemMenu = new MenuItem("Delete");
        deleteItemMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
                homeTable.getItems().remove(selectedItem);
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(copyItemMenu);
        menu.getItems().add(deleteItemMenu);
        homeTable.setContextMenu(menu);

        data = FXCollections.observableArrayList(
                new Callback<HomeTable, Observable[]>() {
                    @Override
                    public Observable[] call(HomeTable param) {
                        return new Observable[]{
                                param.enabledProperty(),
                                param.distanceFactorProperty(),
                                param.agencyFactorProperty(),
                                param.sizeFactorProperty(),
                                param.ageFactorProperty(),
                                param.qualityFactorProperty(),
                                param.otherFactorProperty()
                        };
                    }
                }
        );
        data.addListener((ListChangeListener<? super HomeTable>) c ->
        {
            calculateSizeFactor();
            calculateFactorProduct();
            calculateStandardPrice();
            calculateAvgFactor();
        });

        enabledColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Boolean>("enabled"));
        enabledColumn.setCellFactory( tc -> new CheckBoxTableCell()
        {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                TableRow<HomeTable> currentRow = getTableRow();
                if (!isEmpty()) {
                    if(item.equals(false)) {

                        currentRow.setStyle("-fx-background-color: lightgrey; -fx-font-style: italic");
                    }
                    else {
                        currentRow.setStyle("");
                    }
                }
            }
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("id"));

        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("distance"));
        distanceFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("distanceFactor"));
        distanceFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        distanceFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDistanceFactor(t.getNewValue());
                    }
                }
        );

        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("propertyCode"));

        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("size"));

        sizeFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("sizeFactor"));
        sizeFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        sizeFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSizeFactor(t.getNewValue());
                    }
                }
        );

        priceSizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("priceSize"));

        agencyColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("agency"));

        agencyFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("agencyFactor"));
        agencyFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        agencyFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setAgencyFactor(t.getNewValue());
                    }
                }
        );

        addressColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("address"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("url"));

        latitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("longitude"));

        ageFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("ageFactor"));
        ageFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        ageFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setAgeFactor(t.getNewValue());
                    }
                }
        );

        qualityFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("qualityFactor"));
        qualityFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        qualityFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setQualityFactor(t.getNewValue());
                    }
                }
        );

        otherColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("other"));
        otherColumn.setCellFactory(TextFieldTableCell.<HomeTable,String> forTableColumn(new DefaultStringConverter()));
        otherColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, String> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setOther(t.getNewValue());
                    }
                }
        );

        otherFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("otherFactor"));
        otherFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        otherFactorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setOtherFactor(t.getNewValue());
                    }
                }
        );

        factorProductColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("factorProduct"));
        standardPriceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("standardPrice"));

        homeTable.setItems(data);

    }

    @FXML
    private void handleSearch()
    {
        //NEW SEARCH
        if(this.mainApp.isNewSearch())
        {
            String address = searchField.getText();
            Place place = geocodingClient.getPlace(address);
            formattedAddress.setText(place.getFormattedAddress());

            Location location = place.getGeometry().getLocation();
            coordinates = String.format("%s,%s", location.getLat(), location.getLng());
            distance = "".equals(distanceField.getText()) ? distanceField.getPromptText() : distanceField.getText();

            idealistaResponse = idealistaClient.getSamples(coordinates, distance, 0);

            if(idealistaResponse.getTotalPages() > 1)
            {
                searchButton.setText("More...");
                this.mainApp.setNewSearch(false);
            }
        }
        //MORE RESULTS
        else
        {
            Integer page = idealistaResponse.getActualPage();
            if(page <= idealistaResponse.getTotalPages()) {
                idealistaResponse = idealistaClient.getSamples(coordinates, distance, ++page);
            }else
            {
                searchButton.setDisable(true);
                this.mainApp.setNewSearch(true);
            }
        }

        //PROCESS
        if(null != idealistaResponse && idealistaResponse.getTotal() > 0) {
            int idCounter = homeTable.getItems().size();
            for (Home item : idealistaResponse.getElementList()) {
                if (item.getShowAddress()) {
                    data.add(new HomeTable(++idCounter, item));
                }
            }

            calculateAgencyAndFactor();
        }
    }

    private void calculateAgencyAndFactor() {
        Preferences pref = Preferences.userNodeForPackage(MainApp.class);
        Double privateFactor = new Double(pref.get("privateFactor", null).replace(",","."));
        Double professionalFactor = new Double(pref.get("professionalFactor", null).replace(",","."));

        Thread agencyFactorThread = new Thread() {
            public void run() {
                getAgencyAndFactor(privateFactor,professionalFactor);
            }
        };
        agencyFactorThread.start();
    }

    private void getAgencyAndFactor(Double privateFactor, Double professionalFactor) {
        ParsingAgencyClient parsingAgencyClient = new ParsingAgencyClient();

        for (HomeTable home : homeTable.getItems()) {
            AgencyEnum value = parsingAgencyClient.getProfessional(home.getUrl());
            home.setAgency(value.text());

            if(value.equals(AgencyEnum.PROFESSIONAL))
            {
                home.setAgencyFactor(professionalFactor);
            }
            else if(value.equals(AgencyEnum.PRIVATE))
            {
                home.setAgencyFactor(privateFactor);
            }
            else {
                home.setAgencyFactor(1.00);
            }

            home.calculateFactorProduct();
            home.calculateStandardPrice();
            homeTable.refresh();
        }
    }

    private void calculateStandardPrice() {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if(null != selectedItem) {
            selectedItem.calculateStandardPrice();
            homeTable.refresh();
        }
    }

    private void calculateSizeFactor()
    {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if(null != selectedItem){
            selectedItem.calculateSizePrice();
            homeTable.refresh();
        }
    }

    private void calculateFactorProduct()
    {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if(null != selectedItem){
            selectedItem.calculateFactorProduct();
            homeTable.refresh();
        }
    }

    private void calculateAvgFactor()
    {
        Double summation = 0.0;
        int count = 0;
        for (HomeTable home : data) {
            if(home.isEnabled()){
                count++;
                summation += home.getFactorProduct();
            }
        }

        Double avg = summation / count;
        factorAvgField.setText( String.format("%.2f", avg));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainApp.setHomeTable(homeTable);
        this.mainApp.setNewSearch(true);
        this.mainApp.setSearchButton(searchButton);
        this.mainApp.setFormattedAddress(formattedAddress);
    }

}
