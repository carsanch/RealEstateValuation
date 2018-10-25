package com.carlossamartin.realstatevaluation.utils.numberTranslator;

public abstract class AbstractLangNumber implements ILangNumber {

    private long number;
    private String parseNumber;

    public AbstractLangNumber(long number) {

        this.number = number;
        parseNumber = parseNumber(number);

    }

    @Override
    public ILangNumber add(ILangNumber number) {

        long add = getNumber() + number.getNumber();
        return createNumber(add);
    }

    @Override
    public ILangNumber multiply(ILangNumber number) {

        long mult = getNumber() * number.getNumber();
        return createNumber(mult);
    }

    @Override
    public ILangNumber divide(ILangNumber number) {

        long divide = getNumber() / number.getNumber();
        return createNumber(divide);
    }

    @Override
    public ILangNumber pow(ILangNumber exponent) {

        long pow = (long) Math.pow(getNumber(), exponent.getNumber());
        return createNumber(pow);
    }

    @Override
    public long getNumber() {
        return number;
    }


    abstract protected String parseNumber(long number);

    abstract protected ILangNumber createNumber(long number);

    @Override
    public String toString() {

        return parseNumber;
    }
}
