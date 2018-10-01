package com.carlossamartin.realstatevaluation.model.idealista;

public enum AgencyEnum {
    PROFESSIONAL ("YES"), PRIVATE ("NO"), UNDEFINED(" - ");

    String text;

    AgencyEnum(String text) {
        this.text = text;
    }

    public String text()
    {
        return text;
    }
}
