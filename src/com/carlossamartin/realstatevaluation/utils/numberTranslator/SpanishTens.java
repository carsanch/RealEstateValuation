package com.carlossamartin.realstatevaluation.utils.numberTranslator;


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
