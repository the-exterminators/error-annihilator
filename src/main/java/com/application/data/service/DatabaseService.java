package com.application.data.service;

import com.application.data.entity.TicketProject;
import com.application.data.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * Getters for the roles: getRoleById and getAllRoles
     * Setters not needed at the moment, contents are static
     * All by Jana, so might need some updating
     */

    // Using this to get all users, as I need to add a role column
    public String getRoleById(Integer ID){
        String query = "SELECT role_name FROM ROLES WHERE role_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, ID);
    }

    // Using this for the Role Dropdowns
    public List<String> getAllRoles(){
        String query = "SELECT role_name FROM roles";
        return jdbcTemplate.queryForList(query, String.class);
    }

    /**
     * Getters for the users: getUserByID, getAllUsers and getAllUsernames
     * Setters not needed at the moment, contents are static
     * All by Jana, so might need some updating
     */

    // Using this to get a single user by ID (using entity)
    public User getUserByID(Long id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("passwordhash"),
                        rs.getString("role_id")
                ));
    }

    // Using this to get a list of users (using entity)
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            User obj = new User();
            obj.setFirstName(row.get("first_name").toString());
            obj.setLastName(row.get("last_name").toString());
            obj.setUserName(row.get("username").toString());
            obj.setEmail(row.get("email").toString());
            obj.setDummyPassword(row.get("passwordhash").toString());
            obj.setUserRole(getRoleById((Integer) row.get("role_id")));
            users.add(obj);
        }
        return users;
    }

    // using this to get a list of users (just their usernames
    public List<String> getAllUsernames() {
        String sql = "SELECT * FROM USERS";
        List<String> usernames = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            usernames.add(row.get("first_name").toString() + " " + row.get("last_name").toString());
        }

        return usernames;
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

    // Jana: Brauchte TicketProject Items, sorry für die hässliche Benennung
    public List<TicketProject> getAllProjectItems2() {
        String query = "SELECT * FROM projects";
        return jdbcTemplate.query(query,
                (rs, rowNum) ->
                        new TicketProject(
                                rs.getLong("project_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                getUserByID(rs.getLong("project_lead"))
                        ));
    }


}
