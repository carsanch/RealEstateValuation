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
