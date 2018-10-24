package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.carlossamartin.realstatevaluation.model.idealista.AgencyEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParsingAgencyClient {

    private static final String PROFESSIONAL_IDEALISTA_FIELD = "Profesional";
    private static final String PRIVATE_IDEALISTA_FIELD = "Particular";

    Document doc;

    public ParsingAgencyClient() {
    }

    public AgencyEnum getProfessional(String url)
    {
        AgencyEnum result;

        try {
            doc = Jsoup.connect(url).referrer("http://www.google.com").get();

            String professionalField = doc.select("div[class=professional-name]").first()
                    .select("div[class=name]").first().childNode(0).toString();

            if(professionalField.contains(PROFESSIONAL_IDEALISTA_FIELD))
            {
                result = AgencyEnum.PROFESSIONAL;
            }
            else if(professionalField.contains(PRIVATE_IDEALISTA_FIELD))
            {
                result = AgencyEnum.PRIVATE;
            }
            else {
                result = AgencyEnum.UNDEFINED;
            }

        } catch (IOException e) {
            e.printStackTrace();
            result = AgencyEnum.UNDEFINED;
        }


        return result;
    }

    public static void main(String[] args) {
        ParsingAgencyClient client = new ParsingAgencyClient();
        System.out.println(client.getProfessional("https://www.idealista.com/inmueble/2058667/"));

    }
}
