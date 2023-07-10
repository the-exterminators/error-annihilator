package com.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class TicketProject extends AbstractEntity {
    @NotEmpty
    private Integer projectId = 0;

    private Boolean is_active = true;

    @NotEmpty
    private String projectName = "";
    @NotEmpty
    private String projectDescription = "";
    @NotNull
    @ManyToOne
    private User projectLead;

    public TicketProject() {}

    public TicketProject(Integer projectId, String projectName, String projectDescription, User projectLead) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectLead = projectLead;
    }

    public TicketProject(Integer projectId, String projectName, String projectDescription, User projectLead, Boolean is_active) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectLead = projectLead;
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return projectName;
    }

    public Integer getProjectId(){
        return projectId;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public void setProjectId(Integer ID){
        this.projectId = ID;
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