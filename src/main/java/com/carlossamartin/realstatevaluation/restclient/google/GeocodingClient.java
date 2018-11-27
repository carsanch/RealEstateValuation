package com.carlossamartin.realstatevaluation.restclient.google;


import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface GeocodingClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getGeocoding(@QueryParam("address")String address, @QueryParam("key")String key);
}
