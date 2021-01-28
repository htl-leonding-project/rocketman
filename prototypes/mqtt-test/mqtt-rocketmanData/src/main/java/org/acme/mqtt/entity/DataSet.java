package org.acme.mqtt.entity;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import java.time.LocalDateTime;

public class DataSet {
    String description;
    String value;
    String unit;
    String timestamp;

    public DataSet() {
    }

    public DataSet(String description, String value, String unit, LocalDateTime timestamp) {
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "DataSet{" +
                "description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

   /* public JsonObjectBuilder getJsonObjectBuilder() {
        final JsonObjectBuilder jsonObjectBuilder =
                Json.createObjectBuilder()
                        .add("description", this.description)
                        .add("value", this.value)
                        .add("unit", this.unit)
                        .add("timestamp", this.timestamp);
        return jsonObjectBuilder;
    }*/
}
