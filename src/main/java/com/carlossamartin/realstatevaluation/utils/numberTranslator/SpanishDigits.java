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
