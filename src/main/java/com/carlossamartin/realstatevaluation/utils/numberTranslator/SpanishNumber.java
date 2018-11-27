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

public class SpanishNumber extends AbstractLangNumber {


    public static SpanishDigits[] digits = SpanishDigits.getSorted();
    public static SpanishTens[] tens = SpanishTens.getSorted();
    public static ExSpanishSufix[] exponents = ExSpanishSufix.getSorted();

    public SpanishNumber(long number) {
        super(number);
    }

    @Override
    protected String parseNumber(long number) {

        if (number == Long.MIN_VALUE)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder();
        sb.append(getSing(number));
        format(Math.abs(number), sb);

        return especialCases(sb.toString());
    }

    @Override
    protected ILangNumber createNumber(long number) {
        return new SpanishNumber(number);
    }


    private void format(long number, StringBuilder sb) {

        if (number <= 100)
            baseCase((int) number, sb);
        else {

            ExSpanishSufix suf = findSufix(number);
            long leading = number / suf.getValue();

            if (leading > 1)
                format(leading, sb);

            sb.append((suf.getExponent() == 2 ? "" : " "))
                    .append(suf.isPlural(leading)).append(" ");

            format(number % suf.getValue(), sb);
        }

    }

    private String especialCases(String numero) {
        if (numero.isEmpty())
            return "cero";

        return numero.replaceAll("cincocientos", "quinientos")
                .replaceAll("nuevecientos", "novecientos")
                .replaceAll("sietecientos", "setecientos");
    }

    private void baseCase(int magnitud, StringBuilder sb) {

        if (magnitud < 16)
            sb.append(magnitud > 0 ? digits[magnitud].name() : "");
        else {
            sb.append(tens[(magnitud / 10) - 1].isPlural(magnitud));
            getUnits(magnitud, digits, sb);
        }
    }

    private ExSpanishSufix findSufix(long number) {
        long expon = nearestPowerOf10(number);

        int suf = 0;

        while (suf < exponents.length - 1 && expon >= exponents[suf + 1].getExponent())
            ++suf;

        return exponents[suf];

    }

    private String getSing(long numero) {

        return numero < 0 ? "menos " : "";
    }

    private void getUnits(int numero, SpanishDigits[] digits, StringBuilder sb) {

        int residuo = numero % 10;

        if (residuo != 0) {
            String divisor = numero > 30 ? " y " : "";
            sb.append(divisor).append(digits[residuo]);
        }
    }

    private int nearestPowerOf10(long number) {

        int n = 0;
        long f[] = {10000000000000000L, 100000000, 10000, 100, 10};
        for (int i = 0, j = 16; i < f.length; i++, j /= 2) {
            if (number >= f[i]) {
                number /= f[i];
                n += j;
            }
        }
        return n;
    }
}
