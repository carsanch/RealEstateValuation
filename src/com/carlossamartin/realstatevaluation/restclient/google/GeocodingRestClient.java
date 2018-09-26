package com.carlossamartin.realstatevaluation.restclient.google;

import com.carlossamartin.realstatevaluation.model.google.Place;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;

public class GeocodingRestClient {

    private static final String GOOGLE_API_KEY = "***REMOVED***";
    //private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GOOGLE_API_URL ="https://2133eee7-3606-4ed4-a7fc-deee0fe036db.mock.pstmn.io/maps/api/geocode/json";

    public Place getPlace(String address)
    {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("address", address);
        params.add("key", GOOGLE_API_KEY);

        WebResource webResource;
        webResource= client.resource(GOOGLE_API_URL).queryParams(params);

        ClientResponse response = webResource.type("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        GeocodingResponse geoResponse = response.getEntity(GeocodingResponse.class);
        return geoResponse.getPlaces().get(0);
    }

    //TODO
    static void getSamplesTest()
    {
        GeocodingRestClient client = new GeocodingRestClient();
        Place out = client.getPlace("Calle aguadulce 25, Las palmas");
        System.out.println(out);
    }

    public static void main(String[] args) {
        getSamplesTest();
    }
}
