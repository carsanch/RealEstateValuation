package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.ws.rs.core.HttpHeaders;
import java.util.Base64;

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
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        String url = "TEST".equals(ideApiKey) ?TOKEN_IDEALISTA_URL_TEST: TOKEN_IDEALISTA_URL;
        WebResource webResource;
        webResource= client.resource(url).queryParam("grant_type","client_credentials");

        ClientResponse response = webResource.type("application/json")
                .header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", getBasicCredentials(ideApiKey, ideSecret)))
                .post(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        IdealistaCredentialResponse credentialsResponse = response.getEntity(IdealistaCredentialResponse.class);
        token = credentialsResponse.getAccessToken();
    }

    private static String getBasicCredentials(String ideApiKey, String ideSecret) {
        String keySecret = String.format("%s:%s", ideApiKey, ideSecret);
        return new String(Base64.getEncoder().encode(keySecret.getBytes()));
    }
}
