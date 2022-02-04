package at.htl.rocketman.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@RegisterForReflection
public class DataSet {
    private Long id;
    private String description;
    private String value;
    private String unit;
    private LocalDateTime timestamp;
    private Start start;

    public DataSet() {
    }

    public DataSet(String description, String value, String unit, LocalDateTime timestamp, Start start) {
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
        this.start = start;
    }

    public DataSet(String description, String value, String unit, LocalDateTime timestamp) {
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return String.format("%s: %s %s measured %s", this.getDescription(), this.getValue(), this.getUnit(), this.getTimestamp());
    }

    public String toCSVString() {
        return String.format("%d;%s;%s;%s;%s", this.getStart() == null ? null : this.getStart().getId(), this.getDescription(), this.getValue(), this.getUnit(),
                this.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
