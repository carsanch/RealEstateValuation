package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.carlossamartin.realstatevaluation.MainApp;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.prefs.Preferences;

public class IdealistaRestClient {

    private static final String API_IDEALISTA_URL = "https://api.idealista.com/3.5/es/search";
    private static final String API_IDEALISTA_URL_TEST = "https://2133eee7-3606-4ed4-a7fc-deee0fe036db.mock.pstmn.io/3.5/es/search";

    private static Preferences preferences;

    public IdealistaResponse getSamples(String center, String distance)
    {
        preferences = Preferences.userNodeForPackage(MainApp.class);
        String ideApiKey = preferences.get("ideApiKey", null);
        String ideSecret = preferences.get("ideSecret", null);

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("center", center);
        params.add("propertyType", "homes");
        params.add("distance", distance);
        params.add("operation", "sale");
        params.add("numPage", "1");
        params.add("order", "distance");
        params.add("sort", "asc");

        String url = "TEST".equals(ideApiKey) ? API_IDEALISTA_URL_TEST : API_IDEALISTA_URL;
        WebResource webResource;
        webResource= client.resource(url).queryParams(params);

        if(null == IdealistaCredential.getToken())
        {
            IdealistaCredential.renewToken(ideApiKey,ideSecret);
        }
        ClientResponse response = webResource.type("application/json")
                .header(HttpHeaders.AUTHORIZATION, IdealistaCredential.getToken())
                .post(ClientResponse.class);

        if(response.getStatus() == 401)
        {
            IdealistaCredential.renewToken(ideApiKey, ideSecret);
            getSamples(center,distance);
        }

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        IdealistaResponse idealistaResponse = response.getEntity(IdealistaResponse.class);

        return idealistaResponse;
    }

    //TODO
    static void getSamplesTest()
    {
        IdealistaRestClient client = new IdealistaRestClient();
        IdealistaResponse out = client.getSamples("28.114451,-15.421717", "150");
        System.out.println(out);
    }

    public static void main(String[] args) {
        getSamplesTest();
    }
}
