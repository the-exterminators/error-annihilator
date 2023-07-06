package com.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class TicketStatus extends AbstractEntity {
    private Long statusId;
    private String statusName;

    public TicketStatus() {

    }

    public TicketStatus(String statusName) {
        this.statusName = statusName;
    }
    public TicketStatus(Long id,String statusName) {
        this.statusId = id;
        this.statusName = statusName;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
