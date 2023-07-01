package com.application.data.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final JdbcTemplate jdbcTemplate;

    public TicketService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTicket(String title, String description, int status, int type, int creator, int urgency, int projectId) {
        String query = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, title, description, status, type, creator, urgency, projectId);
    }
}
