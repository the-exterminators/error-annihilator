package com.application.data.service;

import com.application.data.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

class TicketServiceTest {
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    private TicketService ticketService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketService(jdbcTemplate);
    }

    @Test
    void createTicket_ShouldCallJdbcTemplateWithCorrectParameters() {
        // Arrange
        String title = "TicketServiceTest";
        String description = "This is a unit test, testing if the createTicket Procedure is working";
        int status = 1;
        int type = 2;
        int creator = 2;
        int urgency = 4;
        int projectId = 2;

        // Act
        ticketService.createTicket(title, description, status, type, creator, urgency, projectId);

        // Assert
        String expectedQuery = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        verify(jdbcTemplate).update(eq(expectedQuery), eq(title), eq(description), eq(status), eq(type), eq(creator), eq(urgency), eq(projectId));
    }
}
