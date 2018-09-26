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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sun.plugin.dom.DOMObject;

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
    private TableColumn<HomeTable, Boolean> agencyColumn;
    @FXML
    private TableColumn<HomeTable, Double> factorColumn;
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

        idColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("id"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Integer>("distance"));
        propertyCodeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,String>("propertyCode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<HomeTable, Double>("price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("size"));
        priceSizeColumn.setCellValueFactory(new PropertyValueFactory<HomeTable,Double>("priceSize"));
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
        } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
            homeTable.getSelectionModel().selectNext();
            event.consume();
        } else if (event.getCode() == KeyCode.LEFT) {
            // work around due to
            // TableView.getSelectionModel().selectPrevious() due to a bug
            // stopping it from working on
            // the first column in the last row of the table
            selectPrevious();
            event.consume();
        }
        });
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition< HomeTable, ? > focusedCell = homeTable
                .focusModelProperty().get().focusedCellProperty().get();
        homeTable.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    private void selectPrevious() {
        if (homeTable.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition < HomeTable, ? > pos = homeTable.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                homeTable.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < homeTable.getItems().size()) {
                // wrap to end of previous row
                homeTable.getSelectionModel().select(pos.getRow() - 1,
                        homeTable.getVisibleLeafColumn(
                                homeTable.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = homeTable.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                homeTable.getSelectionModel().select(homeTable.getItems().size() - 1);
            } else if (focusIndex > 0) {
                homeTable.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private TableColumn < HomeTable, ? > getTableColumn(
            final TableColumn < HomeTable, ? > column, int offset) {
        int columnIndex = homeTable.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return homeTable.getVisibleLeafColumn(newColumnIndex);
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
