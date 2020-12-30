package org.acme.mqtt.entity;

public class DataHelper {
    String description;
    String unit;

    public DataHelper(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }
}