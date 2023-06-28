package com.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class TicketProject extends AbstractEntity {
    @NotEmpty
    private String projectName = "";
    @NotEmpty
    private String projectDescription = "";
    @NotNull
    @ManyToOne
    private User projectLead;

    public TicketProject() {}

    public TicketProject(String projectName, String projectDescription, User projectLead) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectLead = projectLead;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription(){
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription){
        this.projectDescription = projectDescription;
    }

    public User getProjectLead(){
        return projectLead;
    }

    public void setProjectLead(User projectLead){
        this.projectLead = projectLead;
    }

}