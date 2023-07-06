package com.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User extends AbstractEntity implements UserDetails {

    public User() {

    };

    // Password and authentification info missing at the moment
    public User(String firstName, String lastName, String userName, String email, String dummyPassword, String userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.dummyPassword = dummyPassword;
        this.userRole = userRole;
    }

    public User(Long id, String firstName, String lastName, String userName, String email, String dummyPassword, String userRole) {
        this.user_id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.dummyPassword = dummyPassword;
        this.userRole = userRole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @SequenceGenerator(name = "idgenerator")
    private Long user_id;

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @NotEmpty
    private String userName = "";

    @Email
    @NotEmpty
    private String email = "";

    // To test has to be removed later
    @NotEmpty
    private String dummyPassword = "";

    @NotEmpty
    private String userRole ="";

    @NotEmpty
    private Boolean is_active = true;

    @OneToMany(mappedBy = "ticketCreator")
    private List<Ticket> createdTickets;

    @ManyToOne
    private Ticket ticket;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getDummyPassword() {
        return dummyPassword;
    }

    public void setDummyPassword(String dummyPassword) {
        this.dummyPassword = dummyPassword;
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return dummyPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return is_active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return is_active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return is_active;
    }

    @Override
    public boolean isEnabled() {
        return is_active;
    }
}
