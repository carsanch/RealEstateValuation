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

public enum SpanishDigits {

    cero(0), uno(1), dos(2), tres(3), cuatro(4), cinco(5), seis(6), siete(7), ocho(8),
    nueve(9), dies(10), once(11), doce(12), trece(13), catorce(14), quince(15);

    private int number;

    private SpanishDigits(int pnumber) {
        number = pnumber;
    }

    public static SpanishDigits[] getSorted() {

        SpanishDigits[] values = SpanishDigits.values();
        Arrays.sort(values,
                Comparator.comparing((SpanishDigits hex) -> hex.getNumber()));
        return values;
    }


    public int getNumber() {

        return number;
    }

}
