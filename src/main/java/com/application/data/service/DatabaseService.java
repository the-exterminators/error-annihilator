package com.application.data.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Calling the createTicket Procedure from the db
    public void createTicket(String title, String description, int statusId, int typeId, int creatorId, int urgencyId, int projectId) {
        String query = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, title, description, statusId, typeId, creatorId, urgencyId, projectId);
    }


    /**
     * Getters for the ticket_types: getAll, getId and getName
     * Setters not needed at the moment, contents are static
     */

    public List<String> getAllTicketTypes() {
        String query = "SELECT type_name FROM types";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public int getTicketTypeId(String ticketType) {
        String query = "SELECT type_id FROM types WHERE type_name = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketType);
    }

    public String getTicketTypeName(int ticketId) {
        String query = "SELECT type_name FROM types WHERE type_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, ticketId);
    }


    /**
     * Getters for the urgency: getAll, getId and getName
     * Setters not needed at the moment, contents are static
     */

    public List<String> getAllUrgencyItems() {
        String query = "SELECT tu_name FROM ticket_urgency";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public int getUrgencyId(String urgency) {
        String query = "SELECT urgency_id FROM ticket_urgency WHERE tu_name = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, urgency);
    }

    public String getUrgencyName(int urgencyId) {
        String query = "SELECT tu_name FROM ticket_urgency WHERE urgency_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, urgencyId);
    }


    /**
     * Getters for the project: getAll, getId and getName
     * Getters for description, project_lead and is_active missing at the moment, since not yet used
     * Setters probably better be added as procedures directly in the db
     */

    public List<String> getAllProjectItems() {
        String query = "SELECT title FROM projects";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public int getProjectId(String projectType) {
        String query = "SELECT project_id FROM projects WHERE title = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, projectType);
    }

    public String getProjectName(int projectId) {
        String query = "SELECT title FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, projectId);
    }


}
