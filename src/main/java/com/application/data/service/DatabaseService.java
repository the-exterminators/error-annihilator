package com.application.data.service;

import com.application.data.entity.TicketProject;
import com.application.data.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;
    private static DatabaseService instance;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static synchronized DatabaseService getInstance(JdbcTemplate jdbcTemplate) throws DatabaseConnectionException {
        if (instance == null) {
            try {
                establishConnection(jdbcTemplate);
            } catch (Exception e) {
                throw new DatabaseConnectionException("Failed to establish a database connection.", e);
            }
        }
        return instance;
    }

    private static void establishConnection(JdbcTemplate jdbcTemplate) throws SQLException {
        if (instance == null) {
            Connection connection = null;
            try {
                connection = jdbcTemplate.getDataSource().getConnection();
                // Perform connection setup if necessary
                instance = new DatabaseService(jdbcTemplate);
            } catch (SQLException e) {
                throw e;
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
    }
    
    /**
     * Getters for the tickets: getTicketTitle, description, etc. (all the columns)
     * AND: getAllCreatedTickets, getAllUsersAssignedToTicket, getTicketResolvedDeltaCreated and getTicketsCreatedDateInterval - All DB functions
     * Setters for the tickets: crateticket and updateticket - All DB procedures
     */

    public String getTicketTitle(int ticketId) {
        String query = "SELECT title FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, ticketId);
    }

    public String getTicketDescription(int ticketId) {
        String query = "SELECT description FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, ticketId);
    }

    public int getTicketStatusId(int ticketId) {
        String query = "SELECT status_id FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketId);
    }

    public int getTicketTypeId(int ticketId) {
        String query = "SELECT type_id FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketId);
    }

    public Timestamp getTicketCreated(int ticketId) {
        String query = "SELECT created FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Timestamp.class, ticketId);
    }

    public Timestamp getTicketResolved(int ticketId) {
        String query = "SELECT resolved FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Timestamp.class, ticketId);
    }

    public int getTicketCreatorId(int ticketId) {
        String query = "SELECT creator_id FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketId);
    }

    public int getTicketUrgencyId(int ticketId) {
        String query = "SELECT urgency_id FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketId);
    }

    public int getTicketProjectId(int ticketId) {
        String query = "SELECT project_id FROM tickets WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, ticketId);
    }

    public List<Map<String, Object>> getAllCreatedTickets(int userId) {
        String query = "SELECT * FROM getallcreatedtickets(?)";
        return jdbcTemplate.queryForList(query, userId);
    }

    public List<Map<String, Object>> getAllUsersAssignedToTicket(int ticketId) {
        String query = "SELECT * FROM getallusersassignedtoticket(?)";
        return jdbcTemplate.queryForList(query, ticketId);
    }

    public double getTicketResolvedDeltaCreated(int ticketId) {
        String query = "SELECT * FROM getticketresolveddeltacreated(?)";
        return jdbcTemplate.queryForObject(query, Double.class, ticketId);
    }

    public int getTicketsCreatedDateInterval() {
        String query = "SELECT * FROM getticketscreateddateinterval()";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }


    // Setters

    public void createTicket(String title, String description, int statusId, int typeId, int creatorId, int urgencyId, int projectId) {
        String query = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, title, description, statusId, typeId, creatorId, urgencyId, projectId);
    }


    public void updateTicket(int ticketId, int updateTypeId, int updateStatusId, int updateProjectId, int updateUrgencyId) {
        String query = "CALL updateticket(?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, ticketId, updateTypeId, updateStatusId, updateProjectId, updateUrgencyId);
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
     * AND: getAllUserRoles, getAllUsersDB, getCurrentUserInfo, getAssignedTickets...
     * ...and loginGetUsernamePasswordHash - All DB functions
     * Setters for the users: createuser, manageUpdateUser, setuseractiv, setUserInactive, ...
     * ...updateCurrentUserInfo and updateCurrentUserPasswordHash - All DB procedures
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

    // Using this to get a single user by username (using entity)
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
                new User(
                        rs.getLong("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("passwordhash"),
                        rs.getString("role_id")
                ));
    }

    // Using this to get a list of users (using entity)
    // Second getAllUsersDB() --> Uses DB instead of Entity, look below
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

    public List<Map<String, Object>> getAllUsersDB() {
        String query = "SELECT * FROM getallusers()";
        return jdbcTemplate.queryForList(query);
    }

    // using this to get a list of users (just their usernames)
    public List<String> getAllUsernames() {
        String sql = "SELECT * FROM USERS";
        List<String> usernames = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            usernames.add(row.get("first_name").toString() + " " + row.get("last_name").toString());
        }

        return usernames;
    }

    public List<Map<String, Object>> getAllUserRoles() {
        String query = "SELECT * FROM getalluserroles()";
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getCurrentUserInfo(int userId) {
        String query = "SELECT * FROM getcurrentuserinfo(?)";
        return jdbcTemplate.queryForMap(query, userId);
    }

    public List<Map<String, Object>> getAssignedTickets(int assignedUserId) {
        String query = "SELECT * FROM getassignedtickets(?)";
        return jdbcTemplate.queryForList(query, assignedUserId);
    }

    public String getCurrentUserRole(int userId) {
        String query = "SELECT * FROM getcurrentuserrole(?)";
        return jdbcTemplate.queryForObject(query, String.class, userId);
    }

    public String loginGetUsernamePasswordHash(String username) {
        String query = "SELECT * FROM logingetusernamepasswordhash(?)";
        return jdbcTemplate.queryForObject(query, String.class, username);
    }

    // Setters

    // Calling the creatuser Procedure from the db
    public void createUser(String firstName, String lastName, String username, String email, String passwordHash, int roleId) {
        String query = "CALL createuser(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, firstName, lastName, username, email, passwordHash, roleId);
    }

    public void manageUpdateUser(int userId, String firstName, String lastName, String username, String email, int roleId) {
        String query = "CALL manageupdateuser(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, userId, firstName, lastName, username, email, roleId);
    }

    public void setUserActive(int userId) {
        String query = "CALL setuseractive(?)";
        jdbcTemplate.update(query, userId);
    }

    public void setUserInactive(int userId) {
        String query = "CALL setuserinactive(?)";
        jdbcTemplate.update(query, userId);
    }

    public void updateCurrentUserInfo(int userId, String firstName, String lastName, String username, String email) {
        String query = "CALL updatecurrentuserinfo(?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, userId, firstName, lastName, username, email);
    }

    public void updateCurrentUserPasswordHash(int userId, String passwordHash) {
        String query = "CALL updatecurrentuserpasswordhash(?, ?)";
        jdbcTemplate.update(query, userId, passwordHash);
    }


    /**
     * Getters for the comments: NEED TO BE ADDED
     * Setters: createcomment - Nothing else needed normally
     */

    // Calling the createcomment Procedure from the db
    public void createComment(String commentText, int ticketId, int userId) {
        String query = "CALL createcomment(?, ?, ?)";
        jdbcTemplate.update(query, commentText, ticketId, userId);
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
     * AND: getAllProjects, getallticketsfromproject and getProject - All DB functions
     * Setters for project: createproject, setprojectactive, setprojectinactive and updateproject - All DB procedures
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

    // Calling the creatproject Procedure from the db
    public void createProject(String projectTitle, String projectDescription, int leadUserId) {
        String query = "CALL createproject(?, ?, ?)";
        jdbcTemplate.update(query, projectTitle, projectDescription, leadUserId);
    }

    // Calling the setprojectactive Procedure from the db
    public void setProjectActive(int projectId) {
        String query = "CALL setprojectactive(?)";
        jdbcTemplate.update(query, projectId);
    }

    // Calling the setprojectinactive Procedure from the db
    public void setProjectInactive(int projectId) {
        String query = "CALL setprojectinactive(?)";
        jdbcTemplate.update(query, projectId);
    }

    // Calling the updateproject Procedure from the db
    public void updateProject(int projectId, String projectTitle, String projectDescription, int userId, boolean projectActive) {
        String query = "CALL updateproject(?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, projectId, projectTitle, projectDescription, userId, projectActive);
    }

    public List<Map<String, Object>> getAllProjects() {
        String query = "SELECT * FROM getallprojects()";
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getAllTicketsFromProject(int projectId) {
        String query = "SELECT * FROM getallticketsfromproject(?)";
        return jdbcTemplate.queryForList(query, projectId);
    }

    public Map<String, Object> getProject(int projectId) {
        String query = "SELECT * FROM getproject(?)";
        return jdbcTemplate.queryForMap(query, projectId);
    }

}
