package com.carlossamartin.realstatevaluation.restclient.google;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Place;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.util.prefs.Preferences;

public class GeocodingRestClient {

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GOOGLE_API_URL_TEST ="https://2c9b0f46-162b-4d95-9d1e-fdc5cd4c91a3.mock.pstmn.io/maps/api/geocode/json";

    private static Preferences preferences;

    public Place getPlace(String address)
    {
        preferences = Preferences.userNodeForPackage(MainApp.class);
        String googleApiKey = preferences.get("googleApiKey", null);

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("address", address);
        params.add("key", googleApiKey);

        String url = "TEST".equals(googleApiKey) ? GOOGLE_API_URL_TEST : GOOGLE_API_URL;
        WebResource webResource;
        webResource= client.resource(url).queryParams(params);

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
