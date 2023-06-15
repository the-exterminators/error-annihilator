package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    @Test
    public void testGetters() {
        //testing the getter-Methods
        Assertions.assertEquals("Test Ticket", ticket.getTicketName());
        Assertions.assertEquals("Bug", ticket.getTicketType());
        Assertions.assertEquals("Description", ticket.getDescription());
        Assertions.assertEquals(0, ticket.getProgressPercent());
        Assertions.assertNotNull(ticket.getCreatedTimeStamp());
        Assertions.assertNull(ticket.getResolvedTimeStamp());
        Assertions.assertEquals("Open", ticket.getTicketStatus().getStatusName());
        Assertions.assertEquals(ticketCreator, ticket.getTicketCreator());
        Assertions.assertEquals(assignedUsers, ticket.getAssignedUsers());
        Assertions.assertEquals(new ArrayList<TicketComment>(), ticket.getTicketComment());
    }
}
