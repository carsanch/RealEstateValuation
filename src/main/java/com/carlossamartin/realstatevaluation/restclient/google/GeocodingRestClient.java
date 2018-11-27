package com.carlossamartin.realstatevaluation.restclient.google;

import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.model.google.Place;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.prefs.Preferences;

public class GeocodingRestClient {

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GOOGLE_API_URL_TEST ="https://2c9b0f46-162b-4d95-9d1e-fdc5cd4c91a3.mock.pstmn.io/maps/api/geocode/json";

    private static Preferences preferences;

    public Place getPlace(String address)
    {
        preferences = Preferences.userNodeForPackage(MainApp.class);
        String googleApiKey = preferences.get("googleApiKey", null);


        String url = "TEST".equals(googleApiKey) ? GOOGLE_API_URL_TEST : GOOGLE_API_URL;

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(url));
        GeocodingClient proxy = target.proxy(GeocodingClient.class);

        Response response = proxy.getGeocoding(address,googleApiKey);


        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        GeocodingResponse geoResponse = response.readEntity(GeocodingResponse.class);
        return geoResponse.getPlaces().get(0);
    }

    //TODO
    static void getSamplesTest()
    {
        GeocodingRestClient client = new GeocodingRestClient();
        Place out = client.getPlace("Calle Toledo 1, Madrid");
        System.out.println(out.getFormattedAddress());
    }

    public static void main(String[] args) {
        getSamplesTest();
    }
}
