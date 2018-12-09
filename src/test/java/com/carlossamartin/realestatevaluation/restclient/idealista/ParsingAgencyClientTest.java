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
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParsingAgencyClientTest {

    @Ignore
    @Test
    public void sunnyTest() {
        ParsingAgencyClient client = new ParsingAgencyClient();
        AgencyEnum out = client.getProfessional("https://www.idealista.com/inmueble/2058667/");
        assertEquals(out, AgencyEnum.PRIVATE);
    }

}