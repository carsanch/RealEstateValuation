package com.carlossamartin.realstatevaluation.restclient.idealista;


import com.carlossamartin.realstatevaluation.MainApp;
import com.carlossamartin.realstatevaluation.restclient.google.GeocodingClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Base64;
import java.util.prefs.Preferences;

public class IdealistaCredential {

    private final static String SCHEMA = "Bearer";
    private static final String TOKEN_IDEALISTA_URL = "https://api.idealista.com/oauth/token";
    private static final String TOKEN_IDEALISTA_URL_TEST = "https://2c9b0f46-162b-4d95-9d1e-fdc5cd4c91a3.mock.pstmn.io/oauth/token";


    private static String token;

    public static String getToken() {
        if(null == token) {
            return null;
        }else {
            return String.format("%s %s", SCHEMA, token);
        }
    }

    public static void renewToken(String ideApiKey, String ideSecret)
    {
        String url = "TEST".equals(ideApiKey) ?TOKEN_IDEALISTA_URL_TEST: TOKEN_IDEALISTA_URL;

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(url))
                .queryParam("grant_type", "client_credentials");

        Response response = target.request()
                .header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", getBasicCredentials(ideApiKey, ideSecret)))
                .post(null);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        IdealistaCredentialResponse credentialsResponse = response.readEntity(IdealistaCredentialResponse.class);
        token = credentialsResponse.getAccessToken();
    }

    private static String getBasicCredentials(String ideApiKey, String ideSecret) {
        String keySecret = String.format("%s:%s", ideApiKey, ideSecret);
        return new String(Base64.getEncoder().encode(keySecret.getBytes()));
    }

    public static void main(String[] args) {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        
        String ideApiKey = preferences.get("ideApiKey", null);
        String ideSecret = preferences.get("ideSecret", null);
        renewToken(ideApiKey, ideSecret);
    }
}
