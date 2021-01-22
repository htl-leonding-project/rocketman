package at.htl.rocketman.entity;

import java.time.LocalDateTime;

public class DataSet {
    String description;
    String value;
    String unit;
    LocalDateTime timestamp;

    public DataSet() {
    }

    public DataSet(String description, String value, String unit, LocalDateTime timestamp) {
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s: %s %s measured %s", this.getDescription(), this.getValue(), this.getUnit(), this.getTimestamp());
    }
}
