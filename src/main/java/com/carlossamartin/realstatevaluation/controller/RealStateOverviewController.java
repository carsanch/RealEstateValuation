package com.carlossamartin.realstatevaluation.controller;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.HomeTable;
import com.carlossamartin.realstatevaluation.model.HomeTableWrapper;
import com.carlossamartin.realstatevaluation.model.google.Location;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.carlossamartin.realstatevaluation.model.idealista.AgencyEnum;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaResponse;
import com.carlossamartin.realstatevaluation.restclient.idealista.IdealistaRestClient;
import com.carlossamartin.realstatevaluation.restclient.idealista.ParsingAgencyClient;
import com.carlossamartin.realstatevaluation.utils.Constants;
import com.carlossamartin.realstatevaluation.utils.TableViewUtils;
import com.carlossamartin.realstatevaluation.utils.numberTranslator.SpanishNumber;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

public class RealStateOverviewController {

    @FXML
    private ImageView icoImageView;

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

    //Summary Section
    @FXML
    private Label date1Label;
    @FXML
    private TextField date2TextField;

    @FXML
    private TextField summaryFactorField;

    @FXML
    private TextField standardAvgField1;
    @FXML
    private TextField standardAvgField2;

    @FXML
    private TextField sizeSummaryField1;
    @FXML
    private TextField sizeSummaryField2;

    @FXML
    private TextField standardFinalPriceField1;
    @FXML
    private TextField standardFinalPriceField2;

    @FXML
    private TextField writtenNumber1Field;
    @FXML
    private TextField writtenNumber2Field;

    private IdealistaRestClient idealistaClient;
    private GeocodingRestClient geocodingClient;

    // Reference to the main application.
    private MainApp mainApp;

    private ObservableList<HomeTable> data;
    private IdealistaResponse idealistaResponse;
    private String coordinates;
    private String distance;

    private Date date;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private boolean newSearch = true;

    public RealStateOverviewController() {
    }

    @FXML
    public void initialize() {
        icoImageView.setImage(new Image(MainApp.class.getResourceAsStream("/images/blue-home-icon.png")));

        date = Calendar.getInstance().getTime();
        date1Label.setText(df.format(date));

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

        sizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            copySize();
        });

        standardAvgField1.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateFinalPrice();
        });
        standardAvgField2.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateFinalPrice2();
        });
        sizeSummaryField1.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateFinalPrice();
        });
        sizeSummaryField2.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateFinalPrice2();
        });
        summaryFactorField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateFinalPrice2();
        });
        standardFinalPriceField1.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateWrittenNumber1();
        });
        standardFinalPriceField2.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateWrittenNumber2();
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
            calculateAvgStandard();
        });

        enabledColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Boolean>("enabled"));
        enabledColumn.setCellFactory(tc -> new CheckBoxTableCell() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                TableRow<HomeTable> currentRow = getTableRow();
                if (!isEmpty()) {
                    if (item.equals(false)) {

                        currentRow.setStyle("-fx-background-color: lightgrey; -fx-font-style: italic");
                    } else {
                        currentRow.setStyle("");
                    }
                }
            }
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Integer>("id"));

        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Integer>("distance"));
        distanceFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("distanceFactor"));
        distanceFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, String>("propertyCode"));

        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("size"));

        sizeFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("sizeFactor"));
        sizeFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        priceSizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("priceSize"));

        agencyColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, String>("agency"));

        agencyFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("agencyFactor"));
        agencyFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        addressColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, String>("address"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, String>("url"));

        latitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("longitude"));

        ageFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("ageFactor"));
        ageFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        qualityFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("qualityFactor"));
        qualityFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        otherColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, String>("other"));
        otherColumn.setCellFactory(TextFieldTableCell.<HomeTable, String>forTableColumn(new DefaultStringConverter()));
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

        otherFactorColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("otherFactor"));
        otherFactorColumn.setCellFactory(TextFieldTableCell.<HomeTable, Double>forTableColumn(new DoubleStringConverter()));
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

        factorProductColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("factorProduct"));
        standardPriceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("standardPrice"));

        homeTable.setItems(data);

    }

    @FXML
    private void copySize() {
        sizeSummaryField1.setText(sizeField.getText());
        sizeSummaryField2.setText(sizeField.getText());
    }

    @FXML
    private void handleSearch() {
        //NEW SEARCH
        if (this.newSearch) {
            String address = searchField.getText();
            Place place = geocodingClient.getPlace(address);
            formattedAddress.setText(place.getFormattedAddress());

            Location location = place.getGeometry().getLocation();
            coordinates = String.format("%s,%s", location.getLat(), location.getLng());
            distance = "".equals(distanceField.getText()) ? distanceField.getPromptText() : distanceField.getText();

            idealistaResponse = idealistaClient.getSamples(coordinates, distance, 0);

            if (idealistaResponse.getTotalPages() > 1) {
                searchButton.setText("More...");
                this.newSearch = false;
            }
        }
        //MORE RESULTS
        else {
            Integer page = idealistaResponse.getActualPage();
            if (page <= idealistaResponse.getTotalPages()) {
                idealistaResponse = idealistaClient.getSamples(coordinates, distance, ++page);
            } else {
                searchButton.setDisable(true);
                this.newSearch = true;
            }
        }

        //PROCESS
        if (null != idealistaResponse && idealistaResponse.getTotal() > 0) {
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
        Double privateFactor = new Double(pref.get("privateFactor", null).replace(",", "."));
        Double professionalFactor = new Double(pref.get("professionalFactor", null).replace(",", "."));

        Thread agencyFactorThread = new Thread() {
            public void run() {
                getAgencyAndFactor(privateFactor, professionalFactor);
            }
        };
        agencyFactorThread.start();
    }

    private void getAgencyAndFactor(Double privateFactor, Double professionalFactor) {
        ParsingAgencyClient parsingAgencyClient = new ParsingAgencyClient();

        for (HomeTable home : homeTable.getItems()) {
            AgencyEnum value = parsingAgencyClient.getProfessional(home.getUrl());
            home.setAgency(value.text());

            if (value.equals(AgencyEnum.PROFESSIONAL)) {
                home.setAgencyFactor(professionalFactor);
            } else if (value.equals(AgencyEnum.PRIVATE)) {
                home.setAgencyFactor(privateFactor);
            } else {
                home.setAgencyFactor(1.00);
            }

            home.calculateFactorProduct();
            home.calculateStandardPrice();
            homeTable.refresh();
        }
    }

    private void calculateStandardPrice() {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if (null != selectedItem) {
            selectedItem.calculateStandardPrice();
            homeTable.refresh();
        }
    }

    private void calculateSizeFactor() {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if (null != selectedItem) {
            selectedItem.calculateSizePrice();
            homeTable.refresh();
        }
    }

    private void calculateFactorProduct() {
        HomeTable selectedItem = homeTable.getSelectionModel().getSelectedItem();
        if (null != selectedItem) {
            selectedItem.calculateFactorProduct();
            homeTable.refresh();
        }
    }

    private void calculateAvgStandard() {
        Double summation = 0.0;
        int count = 0;
        for (HomeTable home : data) {
            if (home.isEnabled()) {
                count++;
                summation += home.getStandardPrice();
            }
        }

        Double avg = summation / count;
        String avgText = new BigDecimal(avg).setScale(Constants.SCALE, BigDecimal.ROUND_HALF_UP).toString();

        standardAvgField1.setText(avgText);
        standardAvgField2.setText(avgText);
    }

    private void calculateFinalPrice() {
        String avgText = standardAvgField1.getText();
        String sizeText = sizeSummaryField1.getText();

        if (!avgText.isEmpty() && !sizeText.isEmpty()) {
            Double avg1 = Double.parseDouble(avgText);
            Double size1 = Double.parseDouble(sizeText);

            BigDecimal finalPrice = new BigDecimal(avg1 * size1).setScale(Constants.SCALE, BigDecimal.ROUND_HALF_UP);
            standardFinalPriceField1.setText(finalPrice.toString());
        }
    }

    private void calculateFinalPrice2() {
        String avgText = standardAvgField2.getText();
        String sizeText = sizeSummaryField2.getText();
        String factorText = summaryFactorField.getText();

        if (!avgText.isEmpty() && !sizeText.isEmpty() && !factorText.isEmpty()) {
            Double avg2 = Double.parseDouble(avgText);
            Double size2 = Double.parseDouble(sizeText);
            Double factor = Double.parseDouble(factorText);

            BigDecimal finalPrice = new BigDecimal(avg2 * size2 * factor).setScale(Constants.SCALE, BigDecimal.ROUND_HALF_UP);
            standardFinalPriceField2.setText(finalPrice.toString());
        }
    }

    private void calculateWrittenNumber1() {
        BigDecimal finalPrice = new BigDecimal(standardFinalPriceField1.getText());
        int finalPriceInt = finalPrice.intValue();
        writtenNumber1Field.setText(calculateWrittenNumber(finalPriceInt));
    }

    private void calculateWrittenNumber2() {
        BigDecimal finalPrice = new BigDecimal(standardFinalPriceField2.getText());
        int finalPriceInt = finalPrice.intValue();
        writtenNumber2Field.setText(calculateWrittenNumber(finalPriceInt));
    }

    private String calculateWrittenNumber(int finalPriceInt) {
        SpanishNumber p = new SpanishNumber(Long.valueOf(finalPriceInt));
        return p.toString().toUpperCase();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void init(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public TableView<HomeTable> getHomeTable() {
        return homeTable;
    }

    public void setHomeTable(TableView<HomeTable> homeTable) {
        this.homeTable = homeTable;
    }

    public void setHomeTableFromHomeTableWrapper(HomeTableWrapper wrapper) {

        distanceField.setText(wrapper.getDistance());
        sizeField.setText(wrapper.getSize());

        date1Label.setText(wrapper.getCurrentDate());
        date2TextField.setText(wrapper.getBeforeDate());

        if (null != wrapper.getFactorBeforeDate()) {
            summaryFactorField.setText(wrapper.getFactorBeforeDate().toString());
        }

        searchField.setText(wrapper.getFormattedAddress());
        formattedAddress.setText(wrapper.getFormattedAddress());

        homeTable.getItems().clear();
        homeTable.getItems().addAll(wrapper.getHomes());
    }

    public HomeTableWrapper getHomeTableWrapperFromHomeTable() {
        HomeTableWrapper wrapper = new HomeTableWrapper();

        wrapper.setDistance(distanceField.getText());
        wrapper.setSize(sizeField.getText());

        wrapper.setCurrentDate(date1Label.getText());
        wrapper.setBeforeDate(date2TextField.getText());

        if (!summaryFactorField.getText().isEmpty()) {
            wrapper.setFactorBeforeDate(Double.parseDouble(summaryFactorField.getText()));
        }

        wrapper.setFormattedAddress(formattedAddress.getText());
        wrapper.setHomes(homeTable.getItems());

        return wrapper;
    }

    public void clearData() {
        newSearch = true;

        homeTable.getItems().clear();
        searchButton.setText("Search");
        searchField.setText("");
        formattedAddress.setText("");

        sizeField.setText("");
        distanceField.setText("");

        standardAvgField1.setText("");
        standardAvgField2.setText("");

        sizeSummaryField1.setText("");
        sizeSummaryField2.setText("");

        standardFinalPriceField1.setText("");
        standardFinalPriceField2.setText("");

        summaryFactorField.setText("");

        date = Calendar.getInstance().getTime();
        date1Label.setText(df.format(date));

        date2TextField.setText("");

        writtenNumber1Field.setText("");
        writtenNumber2Field.setText("");
    }
}
