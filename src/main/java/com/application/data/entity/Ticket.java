package com.application.data.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Ticket extends AbstractEntity {

    public Ticket () {

    };

    // Created a new constructor with ticket number and ticket project, so it doesnt mess with any existing in memory stuff
    public Ticket(String tickerNumber, String ticketName, String ticketType, String description, TicketStatus ticketStatus,
                  TicketProject ticketProject, @NotNull User ticketCreator, @Nullable List<User> assignedUsers) {

        this.ticketNumber = tickerNumber;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.description = description;

        if (ticketStatus != null) {
            this.ticketStatus = ticketStatus;
        } else this.ticketStatus = new TicketStatus("New");

        this.ticketProject = ticketProject;

        this.ticketCreator = ticketCreator;
        this.assignedUsers = assignedUsers;

        this.createdTimeStamp = Timestamp.from(Instant.now());
        this.resolvedTimeStamp = null;
        this.progressPercent = 0;
    }

    public Ticket(String ticketName, String ticketType, String description, TicketStatus ticketStatus,
                  @NotNull User ticketCreator, @Nullable List<User> assignedUsers) {

        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.description = description;

        if (ticketStatus != null) {
            this.ticketStatus = ticketStatus;
        } else this.ticketStatus = new TicketStatus("New");

        this.ticketCreator = ticketCreator;
        this.assignedUsers = assignedUsers;

        this.createdTimeStamp = Timestamp.from(Instant.now());
        this.resolvedTimeStamp = null;
        this.progressPercent = 0;
    }

    // New property ticket number - I did it as a string because I had to use a textfield in the frontend - numberfield only accepts doubles
    @NotEmpty
    private String ticketNumber = ""; // I just did it over the constructor but Im not sure how it works with IDs

    @NotEmpty
    private String ticketName = "";

    @NotEmpty
    private String ticketType = "";

    @Nullable
    private String description = "";

    private int progressPercent = 0;

    private Timestamp createdTimeStamp = null;

    private Timestamp resolvedTimeStamp = null;


    @NotNull
    @ManyToOne
    private TicketStatus ticketStatus;

    // New property ticket project
    @NotNull
    @ManyToOne
    private TicketProject ticketProject;

    @NotNull
    @ManyToOne
    private User ticketCreator;

    @OneToMany(mappedBy = "ticket")
    @Nullable
    private List<User> assignedUsers = new LinkedList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketComment> ticketComment = new ArrayList<>();

    // Getter & Setter for the new properties ticket number and ticket project
    public String getTicketNumber(){
        return ticketNumber;
    }
    public void setTicketNumber(String ticketNumber){
        this.ticketNumber = ticketNumber;
    }
    public TicketProject getTicketProject() {
        return ticketProject;
    }
    public void setTicketProject(TicketProject ticketProject) {
        this.ticketProject = ticketProject;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public Timestamp getResolvedTimeStamp() {
        return resolvedTimeStamp;
    }

    public void setResolvedTimeStamp(Timestamp resolvedTimeStamp) {
        this.resolvedTimeStamp = resolvedTimeStamp;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public User getTicketCreator() {
        return ticketCreator;
    }

    public void setTicketCreator(User ticketCreator) {
        this.ticketCreator = ticketCreator;
    }

    @Nullable
    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(@Nullable List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }


    public List<TicketComment> getTicketComment() {
        return ticketComment;
    }

    public void setTicketComment(List<TicketComment> ticketComment) {
        this.ticketComment = ticketComment;
    }

}
