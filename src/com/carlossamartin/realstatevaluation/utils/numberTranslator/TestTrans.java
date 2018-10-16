package com.carlossamartin.realstatevaluation.utils.numberTranslator;

public class TestTrans {

    /**
     * https://codereview.stackexchange.com/questions/105974/number-to-spanish-word-converter
     */

    public static void main(String[] args) {
        SpanishNumber p = new SpanishNumber(230740L);
        System.out.println(p.toString().toUpperCase());
    }
}
