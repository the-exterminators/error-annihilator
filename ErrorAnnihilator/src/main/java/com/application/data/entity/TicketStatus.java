package com.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class TicketStatus extends AbstractEntity {
    private String statusName;

    public TicketStatus() {

    }

    public TicketStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
