package com.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractEntity {

    public User() {

    };

    // Password and authentification info missing at the moment
    public User(String firstName, String lastName, String userName, String email, String userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;
    }

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @NotEmpty
    private String userName = "";

    @Email
    @NotEmpty
    private String email = "";

    @NotEmpty
    private String userRole ="";

    @OneToMany(mappedBy = "ticketCreator")
    private List<Ticket> createdTickets;

    @ManyToOne
    private Ticket ticket;


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public List<Ticket> getCreatedTickets() {
        return createdTickets;
    }

    public void setCreatedTickets(List<Ticket> createdTickets) {
        this.createdTickets = createdTickets;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }



}
