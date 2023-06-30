package com.application.views;

import com.vaadin.flow.component.avatar.Avatar;

public class AssignedUser {

    private int id;



    //private String img;
    private String avatar;

    private String assignedUser;

    private double project;
    private String status;


    private String ticketTitle;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //public String getImg() {
        //return img;
    //}

    //public void setImg(String img) {
        //this.img = img;
    //}
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String client) {
        this.assignedUser = client;
    }

    public double getProject() {
        return project;
    }

    public void setProject(double project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }
}