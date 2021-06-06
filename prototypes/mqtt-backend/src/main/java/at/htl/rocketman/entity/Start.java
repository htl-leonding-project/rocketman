package at.htl.rocketman.entity;

import java.time.LocalDateTime;

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
}
