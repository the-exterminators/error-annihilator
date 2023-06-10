package com.application.data.entity;

import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.List;

public class TicketTest {
    private Ticket ticket;
    private User ticketCreator;
    private List<User> assignedUsers;

    @BeforeEach
    public void setUp() {
        //create an instance of the Ticket class in the setUp method before each test
        ticketCreator = new User();
        assignedUsers = new LinkedList<>();
        assignedUsers.add(new User());
        assignedUsers.add(new User());
        ticket = new Ticket("Test Ticket", "Bug", "Description",
                 new TicketStatus("Open"), ticketCreator, assignedUsers);
    }

}
