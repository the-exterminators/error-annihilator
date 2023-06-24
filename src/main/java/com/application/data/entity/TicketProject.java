package com.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class TicketProject extends AbstractEntity {
    private String projectName;

    public TicketProject() {

    }

    public TicketProject(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
