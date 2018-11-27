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

package com.carlossamartin.realstatevaluation.utils.numberTranslator;

import java.util.Arrays;
import java.util.Comparator;

public enum ExSpanishSufix {


    ciento("s", 2),
    mil("", 3),
    millon("es", 6),
    billon("es", 9),
    trillon("es", 12),
    quadrillion("es", 15);


    private String plural;
    private int exponent;
    private long value;

    private ExSpanishSufix(String plural, int exponent) {

        this.plural = plural;
        this.exponent = exponent;
        this.value = (long) Math.pow(10, exponent);

    }

    public static ExSpanishSufix[] getSorted() {

        ExSpanishSufix[] values = ExSpanishSufix.values();
        Arrays.sort(values,
                Comparator.comparing((ExSpanishSufix hex) -> hex.getExponent()));

        return values;
    }

    public String isPlural(long number) {

        return this.name() + (number > 1 ? plural : "");
    }

    public int getExponent() {

        return exponent;
    }

    public long getValue() {

        return value;
    }


}
