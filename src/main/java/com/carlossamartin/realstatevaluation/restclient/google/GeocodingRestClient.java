/*
 * Real State Valuation Project
 *
 * Copyright © 2018 Carlos Sánchez Martín (carlos.samartin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
