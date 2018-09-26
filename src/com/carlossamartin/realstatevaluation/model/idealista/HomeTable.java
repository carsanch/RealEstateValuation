package com.carlossamartin.realstatevaluation.model.idealista;

import javafx.beans.property.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class HomeTable {

    public HomeTable() {
    }

    private StringProperty propertyCode;
    private IntegerProperty distance;
    private LongProperty price;
    private LongProperty size;
    private IntegerProperty rooms;
    private IntegerProperty bathrooms;
    private StringProperty address;
    private BooleanProperty showAddress;
    private StringProperty url;

    public HomeTable(Home home) {
        this.distance = new SimpleIntegerProperty();
        this.distance.setValue(home.getDistance());

        this.propertyCode = new SimpleStringProperty();
        this.propertyCode.setValue(home.getPropertyCode());

        this.price = new SimpleLongProperty();
        this.price.setValue(home.getPrice());

        this.size = new SimpleLongProperty();
        this.size.setValue(home.getSize());

        this.address = new SimpleStringProperty();
        this.address.setValue(home.getAddress());

        this.url = new SimpleStringProperty();
        this.url.setValue(home.getUrl());
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

    public long getPrice() {
        return price.get();
    }

    public LongProperty priceProperty() {
        return price;
    }

    public void setPrice(long price) {
        this.price.set(price);
    }

    public long getSize() {
        return size.get();
    }

    public LongProperty sizeProperty() {
        return size;
    }

    public void setSize(long size) {
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
}
