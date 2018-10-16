package com.carlossamartin.realstatevaluation.model;

import com.carlossamartin.realstatevaluation.model.idealista.AgencyEnum;
import com.carlossamartin.realstatevaluation.model.idealista.Home;
import javafx.beans.property.*;

import java.math.BigDecimal;

public class HomeTable {

    private static final int SCALE = 2;

    private BooleanProperty enabled;
    private IntegerProperty id;
    private StringProperty propertyCode;

    private IntegerProperty distance;
    private DoubleProperty distanceFactor;

    private DoubleProperty price;
    private DoubleProperty size;
    private DoubleProperty sizeFactor;
    private DoubleProperty priceSize;

    private StringProperty agency;
    private DoubleProperty agencyFactor;

    private IntegerProperty rooms;
    private IntegerProperty bathrooms;
    private StringProperty address;
    private StringProperty url;

    private DoubleProperty latitude;
    private DoubleProperty longitude;

    private DoubleProperty ageFactor;
    private DoubleProperty qualityFactor;

    private StringProperty other;
    private DoubleProperty otherFactor;

    private DoubleProperty factorProduct;
    private DoubleProperty standardPrice;

    public HomeTable() {
        this.enabled = new SimpleBooleanProperty();
        this.id = new SimpleIntegerProperty();
        this.distance = new SimpleIntegerProperty();
        this.distanceFactor = new SimpleDoubleProperty();

        this.propertyCode = new SimpleStringProperty();

        this.price = new SimpleDoubleProperty();
        this.size = new SimpleDoubleProperty();
        this.sizeFactor = new SimpleDoubleProperty();

        this.agency = new SimpleStringProperty();
        this.agencyFactor = new SimpleDoubleProperty();

        this.priceSize = new SimpleDoubleProperty();
        this.standardPrice = new SimpleDoubleProperty();

        this.address = new SimpleStringProperty();
        this.url = new SimpleStringProperty();

        this.latitude = new SimpleDoubleProperty();
        this.longitude = new SimpleDoubleProperty();

        this.rooms = new SimpleIntegerProperty();
        this.bathrooms = new SimpleIntegerProperty();

        this.ageFactor = new SimpleDoubleProperty();
        this.qualityFactor = new SimpleDoubleProperty();
        this.other = new SimpleStringProperty();
        this.otherFactor = new SimpleDoubleProperty();

        this.factorProduct = new SimpleDoubleProperty();
        this.standardPrice = new SimpleDoubleProperty();
    }

    public HomeTable(Integer id, Home home) {

        this.enabled = new SimpleBooleanProperty(true);
        this.id = new SimpleIntegerProperty(id);

        this.distance = new SimpleIntegerProperty(home.getDistance());
        this.distanceFactor = new SimpleDoubleProperty(1.0);

        this.propertyCode = new SimpleStringProperty(home.getPropertyCode());

        this.price = new SimpleDoubleProperty(home.getPrice());
        this.size = new SimpleDoubleProperty(home.getSize());
        this.sizeFactor = new SimpleDoubleProperty(1.0);

        this.agency = new SimpleStringProperty(AgencyEnum.UNDEFINED.text());
        this.agencyFactor = new SimpleDoubleProperty(1.0);

        this.address = new SimpleStringProperty(home.getAddress());
        this.url = new SimpleStringProperty(home.getUrl());

        this.latitude = new SimpleDoubleProperty(home.getLatitude());
        this.longitude = new SimpleDoubleProperty(home.getLongitude());

        this.rooms = new SimpleIntegerProperty(home.getRooms());
        this.bathrooms = new SimpleIntegerProperty(home.getBathrooms());

        this.ageFactor = new SimpleDoubleProperty(1.0);
        this.qualityFactor = new SimpleDoubleProperty(1.0);
        this.other = new SimpleStringProperty();
        this.otherFactor = new SimpleDoubleProperty(1.0);

        //this.priceSize = new SimpleDoubleProperty();
        calculateSizePrice();

        //this.factorProduct = new SimpleDoubleProperty();
        calculateFactorProduct();

        //this.standardPrice
        calculateStandardPrice();
    }

    public void calculateSizePrice() {
        this.priceSize = new SimpleDoubleProperty(new BigDecimal(this.price.doubleValue() /
                this.size.doubleValue()).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public void calculateFactorProduct() {
        this.factorProduct = new SimpleDoubleProperty(new BigDecimal(this.sizeFactor.doubleValue() * this.distanceFactor.doubleValue() * this.agencyFactor.doubleValue()
                * this.ageFactor.doubleValue() * this.qualityFactor.doubleValue() * this.otherFactor.doubleValue()).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public void calculateStandardPrice() {
        this.standardPrice = new SimpleDoubleProperty(new BigDecimal(this.factorProduct.doubleValue()
                * this.priceSize.doubleValue()).setScale(SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getPropertyCode() {
        return propertyCode.get();
    }

    public StringProperty propertyCodeProperty() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode.set(propertyCode);
    }

    public int getDistance() {
        return distance.get();
    }

    public IntegerProperty distanceProperty() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance.set(distance);
    }

    public double getDistanceFactor() {
        return distanceFactor.get();
    }

    public DoubleProperty distanceFactorProperty() {
        return distanceFactor;
    }

    public void setDistanceFactor(double distanceFactor) {
        this.distanceFactor.set(distanceFactor);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getSize() {
        return size.get();
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    public void setSize(double size) {
        this.size.set(size);
    }

    public double getSizeFactor() {
        return sizeFactor.get();
    }

    public DoubleProperty sizeFactorProperty() {
        return sizeFactor;
    }

    public void setSizeFactor(double sizeFactor) {
        this.sizeFactor.set(sizeFactor);
    }

    public double getPriceSize() {
        return priceSize.get();
    }

    public DoubleProperty priceSizeProperty() {
        return priceSize;
    }

    public void setPriceSize(double priceSize) {
        this.priceSize.set(priceSize);
    }

    public String getAgency() {
        return agency.get();
    }

    public StringProperty agencyProperty() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency.set(agency);
    }

    public double getAgencyFactor() {
        return agencyFactor.get();
    }

    public DoubleProperty agencyFactorProperty() {
        return agencyFactor;
    }

    public void setAgencyFactor(double agencyFactor) {
        this.agencyFactor.set(agencyFactor);
    }

    public double getStandardPrice() {
        return standardPrice.get();
    }

    public DoubleProperty standardPriceProperty() {
        return standardPrice;
    }

    public void setStandardPrice(double standardPrice) {
        this.standardPrice.set(standardPrice);
    }

    public int getRooms() {
        return rooms.get();
    }

    public IntegerProperty roomsProperty() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms.set(rooms);
    }

    public int getBathrooms() {
        return bathrooms.get();
    }

    public IntegerProperty bathroomsProperty() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms.set(bathrooms);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public double getLatitude() {
        return latitude.get();
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public double getLongitude() {
        return longitude.get();
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public double getAgeFactor() {
        return ageFactor.get();
    }

    public DoubleProperty ageFactorProperty() {
        return ageFactor;
    }

    public void setAgeFactor(double ageFactor) {
        this.ageFactor.set(ageFactor);
    }

    public double getQualityFactor() {
        return qualityFactor.get();
    }

    public DoubleProperty qualityFactorProperty() {
        return qualityFactor;
    }

    public void setQualityFactor(double qualityFactor) {
        this.qualityFactor.set(qualityFactor);
    }

    public String getOther() {
        return other.get();
    }

    public StringProperty otherProperty() {
        return other;
    }

    public void setOther(String other) {
        this.other.set(other);
    }

    public double getOtherFactor() {
        return otherFactor.get();
    }

    public DoubleProperty otherFactorProperty() {
        return otherFactor;
    }

    public void setOtherFactor(double otherFactor) {
        this.otherFactor.set(otherFactor);
    }

    public double getFactorProduct() {
        return factorProduct.get();
    }

    public DoubleProperty factorProductProperty() {
        return factorProduct;
    }

    public void setFactorProduct(double factorProduct) {
        this.factorProduct.set(factorProduct);
    }
}

