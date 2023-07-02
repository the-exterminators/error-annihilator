package com.application.data.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

class DatabaseServiceTest {
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    private DatabaseService databaseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        databaseService = new DatabaseService(jdbcTemplate);
    }

    @Test
    public void testCreateTicket() {
        // Arrange
        String title = "DatabaseServiceTest";
        String description = "This is a unit test, testing if the createTicket Procedure is working";
        int statusId = 1;
        int typeId = 2;
        int creatorId = 2;
        int urgencyId = 4;
        int projectId = 2;

        // Act
        databaseService.createTicket(title, description, statusId, typeId, creatorId, urgencyId, projectId);

        // Assert
        String expectedQuery = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        verify(jdbcTemplate).update(eq(expectedQuery), eq(title), eq(description), eq(statusId), eq(typeId), eq(creatorId), eq(urgencyId), eq(projectId));
    }
}
