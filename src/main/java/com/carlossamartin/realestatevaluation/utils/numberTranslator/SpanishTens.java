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

package com.carlossamartin.realestatevaluation.utils.numberTranslator;


import java.util.Arrays;
import java.util.Comparator;

public enum SpanishTens {

    dies(10, "dieci"), veinte(20, "veinti"), treinta(30), cuarenta(40),
    cincuenta(50), sesenta(60), setenta(70), ochenta(80),
    noventa(90), cien(100);

    private int number;
    private String plural;


    private SpanishTens(int pnumber, String pplural) {

        number = pnumber;
        plural = pplural;

    }

    private SpanishTens(int pnumber) {

        this(pnumber, "");
    }

    public String isPlural(int pnumber) {

        if (pnumber > number && plural != "")
            return plural;

        return name();
    }

    public static SpanishTens[] getSorted() {

        SpanishTens[] values = SpanishTens.values();
        Arrays.sort(values,
                Comparator.comparing((SpanishTens des) -> des.getNumber()));
        return values;
    }

    public int getNumber() {

        return number;
    }

}
