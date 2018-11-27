package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.carlossamartin.realstatevaluation.MainApp;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.prefs.Preferences;

public class IdealistaRestClient {

    private static final String API_IDEALISTA_URL = "https://api.idealista.com/3.5/es/search";
    private static final String API_IDEALISTA_URL_TEST = "https://2c9b0f46-162b-4d95-9d1e-fdc5cd4c91a3.mock.pstmn.io/3.5/es/search";

    private static Preferences preferences;

    public IdealistaResponse getSamples(String center, String distance, Integer page)
    {
        preferences = Preferences.userNodeForPackage(MainApp.class);
        String ideApiKey = preferences.get("ideApiKey", null);
        String ideSecret = preferences.get("ideSecret", null);

        MultivaluedMap params = new MultivaluedMapImpl();

        params.add("center", center);
        params.add("propertyType", "homes");
        params.add("distance", distance);
        params.add("operation", "sale");
        params.add("numPage", page.toString());
        params.add("order", "distance");
        params.add("sort", "asc");
        params.add("maxItems", "50");

        String url = "TEST".equals(ideApiKey) ? API_IDEALISTA_URL_TEST : API_IDEALISTA_URL;

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(url)).queryParams(params);

        if(null == IdealistaCredential.getToken())
        {
            IdealistaCredential.renewToken(ideApiKey,ideSecret);
        }

        Response response = target.request()
                .header(HttpHeaders.AUTHORIZATION, IdealistaCredential.getToken()).post(null);

        if(response.getStatus() == 401)
        {
            IdealistaCredential.renewToken(ideApiKey, ideSecret);
            getSamples(center,distance, page);
        }

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        IdealistaResponse idealistaResponse = response.readEntity(IdealistaResponse.class);
        return idealistaResponse;
    }

    //TODO
    static void getSamplesTest()
    {
        IdealistaRestClient client = new IdealistaRestClient();
        IdealistaResponse out = client.getSamples("28.114451,-15.421717", "150", 0);
        System.out.println(out.getElementList().get(0).getAddress());
    }

    public static void main(String[] args) {
        getSamplesTest();
    }
}
