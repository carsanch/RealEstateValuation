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
import com.carlossamartin.realstatevaluation.view.utils.TableViewUtils;
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
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class RealStateOverviewController {

    @FXML
    private TextField searchField;

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

    ObservableList<HomeTable> data;

    public RealStateOverviewController() {
    }

    @FXML
    public void initialize() {
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
                                param.factorProperty()
                        };
                    }
                }
        );
        data.addListener((ListChangeListener<? super HomeTable>) c ->
        {
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
                        
                        currentRow.setStyle("-fx-background-color:gray");
                    }
                    else {
                        currentRow.setStyle("");
                    }
                }
            }
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("id"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("distance"));
        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("propertyCode"));

        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("size"));
        priceSizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("priceSize"));

        agencyColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("agency"));
        factorColumn.setCellFactory(TextFieldTableCell.<HomeTable,Double> forTableColumn(new DoubleStringConverter()));
        factorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HomeTable, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HomeTable, Double> t) {
                        ((HomeTable) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFactor(t.getNewValue());
                    }
                }
        );

        addressColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("address"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("url"));

        latitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("longitude"));

        homeTable.setItems(data);

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

        int idCounter = 0;
        for(Home item : idealistaResponse.getElementList())
        {
            if(item.getShowAddress()) {
                data.add(new HomeTable(++idCounter,item));
            }
        }

        calculateAgencyAndFactor();
    }

    private void calculateAgencyAndFactor() {
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

    private void calculateAvgFactor()
    {
        Double summation = 0.0;
        int count = 0;
        for (HomeTable home : data) {
            if(home.isEnabled()){
                count++;
                summation += home.getFactor();
            }
        }

        Double avg = summation / count;
        factorAvgField.setText( String.format("%.4f", avg));
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
