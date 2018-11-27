package com.carlossamartin.realstatevaluation.restclient.google;

import com.carlossamartin.realstatevaluation.model.google.Place;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GeocodingResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("results")
    private List<Place> places;

    public GeocodingResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
