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

import com.carlossamartin.realestatevaluation.model.idealista.AgencyEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParsingAgencyClient {

    private static final String PROFESSIONAL_IDEALISTA_FIELD = "Profesional";
    private static final String PRIVATE_IDEALISTA_FIELD = "Particular";

    private Document doc;

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
}
