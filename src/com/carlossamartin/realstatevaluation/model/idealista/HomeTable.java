package com.carlossamartin.realstatevaluation.model.idealista;

import javafx.beans.property.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

public class HomeTable {

    public HomeTable() {
    }

    private IntegerProperty id;

    private StringProperty propertyCode;
    private IntegerProperty distance;
    private DoubleProperty price;
    private DoubleProperty size;
    private IntegerProperty rooms;
    private IntegerProperty bathrooms;
    private StringProperty address;
    private BooleanProperty showAddress;
    private StringProperty url;

    private DoubleProperty priceSize;
    private DoubleProperty factor;

    public HomeTable(Integer id, Home home) {
        this.id = new SimpleIntegerProperty(id);
        this.distance = new SimpleIntegerProperty(home.getDistance());
        this.propertyCode = new SimpleStringProperty(home.getPropertyCode());
        this.price = new SimpleDoubleProperty(home.getPrice());
        this.size = new SimpleDoubleProperty(home.getSize());
        this.address = new SimpleStringProperty(home.getAddress());
        this.url = new SimpleStringProperty(home.getUrl());

        this.factor = new SimpleDoubleProperty(1.0);
        this.priceSize = new SimpleDoubleProperty(new BigDecimal(this.price.doubleValue() / this.size.doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
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

    public boolean isShowAddress() {
        return showAddress.get();
    }

    public BooleanProperty showAddressProperty() {
        return showAddress;
    }

    public void setShowAddress(boolean showAddress) {
        this.showAddress.set(showAddress);
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

    public double getPriceSize() {
        return priceSize.get();
    }

    public DoubleProperty priceSizeProperty() {
        return priceSize;
    }

    public void setPriceSize(double priceSize) {
        this.priceSize.set(priceSize);
    }

    public double getFactor() {
        return factor.get();
    }

    public DoubleProperty factorProperty() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor.set(factor);
    }
}
