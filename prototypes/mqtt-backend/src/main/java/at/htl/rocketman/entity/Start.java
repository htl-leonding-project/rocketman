package at.htl.rocketman.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Start {
    private Long id;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Start() {
    }

    public Start(String comment, LocalDateTime startDate, LocalDateTime endDate) {
        this.comment = comment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Start(String comment, LocalDateTime startDate) {
        this.comment = comment;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        if (this.getEndDate() == null) {
            return String.format("%d;%s;%s;null",
                    this.getId(),
                    this.getComment(),
                    this.getStartDate().toString());
        }
        return String.format("%d;%s;%s;%s",
                this.getId(),
                this.getComment(),
                this.getStartDate().toString(),
                this.getEndDate().toString());
    }

    public String toCSVString() {
        return String.format("%d;%s;%s;%s",
                this.getId(),
                this.getComment(),
                this.getStartDate().format(DateTimeFormatter.ISO_DATE_TIME),
                this.getEndDate() == null ? "null" : this.getEndDate().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
