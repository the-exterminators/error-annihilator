package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketStatusTest {
    @Test
    public void testTicketStatus() {
        // Create a new TicketStatus object
        TicketStatus ticketStatus = new TicketStatus("Open");

        // Test the getStatusName() method
        String statusName = ticketStatus.getStatusName();
        Assertions.assertEquals("Open", statusName, "Status name should be 'Open'");

        // Test the setStatusName() method
        ticketStatus.setStatusName("Closed");
        statusName = ticketStatus.getStatusName();
        Assertions.assertEquals("Closed", statusName, "Status name should be 'Closed'");
    }
}
