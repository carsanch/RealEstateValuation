package com.carlossamartin.realstatevaluation.utils.numberTranslator;

public interface ILangNumber {
    public ILangNumber add(ILangNumber number);

    public ILangNumber multiply(ILangNumber number);

    public ILangNumber divide(ILangNumber number);

    public ILangNumber pow(ILangNumber exponent);

    public long getNumber();
}
