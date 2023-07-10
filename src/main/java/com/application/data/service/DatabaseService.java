package com.application.data.service;

import com.application.data.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public int getTicketStatus(String status_name){
        String query = "SELECT status_id FROM status WHERE status_name = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, status_name);
    }

    public TicketStatus getStatusByID(Integer id) {
        String sql = "SELECT * FROM status WHERE status_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new TicketStatus(
                        (int) rs.getInt("status_id"),
                        rs.getString("status_name")
                ));
    }

    public List<String> getAllTicketStatus() {
        String query = "SELECT status_name FROM status";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public List<TicketStatus> getAllTicketStatusEntityList(){
        String query = "SELECT * FROM status";
        List<TicketStatus> status = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map row : rows) {
            TicketStatus obj = new TicketStatus(
                    (Integer) row.get("status_id"),
                    row.get("status_name").toString()
            );
            status.add(obj);
        }
        return status;
    }

    public void setStatus(int ticketId){
        String query = "UPDATE tickets SET status_id = 5 WHERE ticket_id = ?";
        jdbcTemplate.update(query, ticketId);
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

    public List<Ticket> getAllCreatedTicketsEntityList(int userId) {
        String query = "SELECT * FROM tickets WHERE creator_id = ?";
        List<Ticket> tickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, userId);
        for (Map row : rows) {
            Ticket obj = new Ticket(
                    row.get("ticket_id").toString(),
                    row.get("title").toString(),
                    getTicketTypeName((Integer)row.get("type_id")),
                    row.get("description").toString(),
                    getStatusByID((Integer) row.get("status_id")),
                    getProjectEntity((Integer) row.get("project_id")),
                    getUserByID((Integer) row.get("creator_id")),
                    getAllUsersAssignedToTicketEntity((Integer) row.get("ticket_id"))
            );
            obj.setCreatedTimeStamp((Timestamp) row.get("created"));
            obj.setResolvedTimeStamp((Timestamp) row.get("resolved"));
            obj.setUrgency(getUrgencyName((Integer) row.get("urgency_id")));
            obj.setTicketComment(getCommentsByTicketId((Integer) row.get("ticket_id")));
            tickets.add(obj);
        }
        return tickets;
    }

    public List<Map<String, Object>> getAllUsersAssignedToTicket(int ticketId) {
        String query = "SELECT * FROM getallusersassignedtoticket(?)";
        return jdbcTemplate.queryForList(query, ticketId);
    }

    public List<User> getAllUsersAssignedToTicketEntity(int ticketId) {
        String query = "SELECT * FROM users JOIN tickets_assigned_users USING (user_id) WHERE ticket_id = ?";
        List<User> users = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, ticketId);

        for (Map row : rows) {
            User obj = new User();
            obj.setUser_id((Integer) row.get("user_id"));
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

    public double getTicketResolvedDeltaCreated(int ticketId) {
        String query = "SELECT * FROM getticketresolveddeltacreated(?)";
        return jdbcTemplate.queryForObject(query, Double.class, ticketId);
    }

    public int getTicketsCreatedDateInterval() {
        String query = "SELECT * FROM getticketscreateddateinterval()";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public Ticket getTicketByIdEntity(int ticketid) {
        String query = "SELECT * FROM tickets where ticket_id = ?";
        Ticket ticket = jdbcTemplate.queryForObject(query, new Object[]{ticketid}, (rs, rowNum) ->
                new Ticket(
                        rs.getString("ticket_id"),
                        rs.getString("title"),
                        getTicketTypeName(rs.getInt("type_id")),
                        rs.getString("description"),
                        getStatusByID(rs.getInt("status_id")),
                        getProjectEntity(rs.getInt("project_id")),
                        getUserByID(rs.getInt("creator_id")),
                        getAllUsersAssignedToTicketEntity(rs.getInt("ticket_id")),
                        rs.getTimestamp("created"),
                        rs.getTimestamp("resolved"),
                        getUrgencyName(rs.getInt("urgency_id"))
                ));
        return ticket;
    }

    // Setters

    public void createTicket(String title, String description, int statusId, int typeId, int creatorId, int urgencyId, int projectId) {
        //String query = "CALL createticket(?, ?, ?, ?, ?, ?, ?)";
        String query = "INSERT INTO tickets VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, null, ?, ?, ?)";
        jdbcTemplate.update(query, getNextTicketId(), title, description, statusId, typeId, creatorId, urgencyId, projectId);
    }

    public Integer getNextTicketId(){
        String query = "SELECT max(ticket_id) From tickets";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class);
        if(id == null){
            id = 0;
        }
        return id + 1;
    }


    public void updateTicket(int ticketId, int updateTypeId, int updateStatusId, int updateProjectId, int updateUrgencyId) {
        String query = "CALL updateticket(?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, ticketId, updateTypeId, updateStatusId, updateProjectId, updateUrgencyId);
    }

    public void updateTicket(int ticketId, int updateTypeId, int updateStatusId, int updateProjectId, int updateUrgencyId, String title, String description) {
        String query = "UPDATE tickets SET type_id = ?," +
                "status_id = ?," +
                "project_id = ?," +
                "urgency_id = ?," +
                "title = ?," +
                "description = ?" +
                "WHERE ticket_id = ?";
        jdbcTemplate.update(query, updateTypeId, updateStatusId, updateProjectId, updateUrgencyId, title, description, ticketId);
    }

    public Integer getNextTauId(){
        String query = "SELECT max(tau_id) From tickets_assigned_users";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class);
        if(id == null){
            id = 0;
        }
        return id + 1;
    }

    // loop through all entries related to the ticket and compare if they are in the list
    // Excuse the complicated function, it can surely be easier than this :(
    public void updateAssignedUser(int ticketId, Set<User> users){
        List<User> currentUsers = getAllUsersAssignedToTicketEntity(ticketId);
        Set<User> newUsers = new HashSet<>();

        // Loop through all current assigned users
        if(!currentUsers.isEmpty()) {
            Boolean deleteUser = true;
            for (User currUser : currentUsers) {
                // Loop through our new user list
                for (User user : users) {
                    // if the new user exists we dont want to delete the current user
                    if (user.getUser_id() == currUser.getUser_id()) {
                        deleteUser = false;
                        // if the array doesnt already contain the user, we want to add the user to the new users
                    } else if (!newUsers.contains(user)) { // also need to compare to other currUsers
                        Boolean addUser = true;
                        for(User currUser2 : currentUsers) {
                            if(currUser2.getUser_id() == user.getUser_id()) {
                                addUser = false;
                            }
                        }
                        if(addUser){
                            newUsers.add(user);
                        }
                    }
                }
                if (deleteUser) {
                    String query = "DELETE FROM tickets_assigned_users WHERE user_id = ? AND ticket_id = ?";
                    jdbcTemplate.update(query, currUser.getUser_id(), ticketId);
                }
                deleteUser = true;
            }
        } else {
            newUsers = users;
        }
        for(User user : newUsers){
            String query = "INSERT INTO tickets_assigned_users VALUES(?,?,?)";
            jdbcTemplate.update(query, getNextTauId(), ticketId, user.getUser_id());
        }
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

    public Integer getRoleByName(String name){
        String query = "SELECT role_id FROM ROLES WHERE role_name = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, name);
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
    public User getUserByID(Integer id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
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
                        rs.getInt("user_id"),
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
        String sql = "SELECT * FROM USERS WHERE is_active = true";
        List<User> users = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            User obj = new User();
            obj.setUser_id((Integer) row.get("user_id"));
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
        String sql = "SELECT * FROM USERS WHERE is_active = true";
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

    public List<Ticket> getAssignedTickets(int assignedUserId) {
        String query = "Select * From tickets Left Join tickets_assigned_users USING (ticket_id) Where tickets_assigned_users.user_id = ?";
        List<Ticket> tickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, assignedUserId);
        for (Map row : rows) {
            Ticket obj = new Ticket(
                    row.get("ticket_id").toString(),
                    row.get("title").toString(),
                    getTicketTypeName((Integer)row.get("type_id")),
                    row.get("description").toString(),
                    getStatusByID((Integer) row.get("status_id")),
                    getProjectEntity((Integer) row.get("project_id")),
                    getUserByID((Integer) row.get("creator_id")),
                    getAllUsersAssignedToTicketEntity((Integer) row.get("ticket_id"))
            );
            obj.setCreatedTimeStamp((Timestamp) row.get("created"));
            obj.setResolvedTimeStamp((Timestamp) row.get("resolved"));
            obj.setUrgency(getUrgencyName((Integer) row.get("urgency_id")));
            obj.setTicketComment(getCommentsByTicketId((Integer) row.get("ticket_id")));
            tickets.add(obj);
        }
        return tickets;
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
        String query = "Insert into comments Values(?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(query, getNextCommentId(), commentText, ticketId, userId);
    }

    public Integer getNextCommentId(){
        String query = "SELECT max(comment_id) From comments";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class);
        if(id == null){
            id = 0;
        }
        return id + 1;
    }


    public List<TicketComment> getCommentsByTicketId(int ticketId) {
        String query = "SELECT * FROM comments WHERE ticket_id = ?";
        List<TicketComment> comments = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, ticketId);

        for (Map row : rows) {
            TicketComment obj = new TicketComment(
                    (Integer) row.get("comment_id"),
                    row.get("comment_text").toString(),
                    getUserByID((Integer) row.get("user_id")),
                    getTicketByIdEntity((Integer) row.get("ticket_id")),
                    (Timestamp) row.get("created")
            );
            comments.add(obj);
        }
        return comments;
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
                                rs.getInt("project_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                getUserByID(rs.getInt("project_lead")),
                                rs.getBoolean("is_active")
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

    public List<Ticket> getAllTicketsFromProjectEntityList(int projectId) {
        String query = "SELECT * FROM tickets WHERE project_id = ?";
        List<Ticket> tickets = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, projectId);
        for (Map row : rows) {
            Ticket obj = new Ticket(
                    row.get("ticket_id").toString(),
                    row.get("title").toString(),
                    getTicketTypeName((Integer)row.get("type_id")),
                    row.get("description").toString(),
                    getStatusByID((Integer) row.get("status_id")),
                    getProjectEntity((Integer) row.get("project_id")),
                    getUserByID((Integer) row.get("creator_id")),
                    getAllUsersAssignedToTicketEntity((Integer) row.get("ticket_id"))
            );
            obj.setCreatedTimeStamp((Timestamp) row.get("created"));
            obj.setResolvedTimeStamp((Timestamp) row.get("resolved"));
            obj.setUrgency(getUrgencyName((Integer) row.get("urgency_id")));
            obj.setTicketComment(getCommentsByTicketId((Integer) row.get("ticket_id")));
            tickets.add(obj);
        }
        return tickets;
    }

    public Map<String, Object> getProject(int projectId) {
        String query = "SELECT * FROM getproject(?)";
        return jdbcTemplate.queryForMap(query, projectId);
    }

    public TicketProject getProjectEntity(long projectId) {
        String query = "SELECT * FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{projectId}, (rs, rowNum) ->
                new TicketProject(//Long projectId, String projectName, String projectDescription, User projectLead
                        rs.getInt("project_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        getUserByID(rs.getInt("project_lead"))
                ));
    }

}