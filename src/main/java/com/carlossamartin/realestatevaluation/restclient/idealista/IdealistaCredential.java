/*
 * Real Estate Valuation Project
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

package com.carlossamartin.realestatevaluation.restclient.idealista;


import com.carlossamartin.realestatevaluation.MainApp;
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
