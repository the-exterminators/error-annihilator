CREATE TABLE roles (
    role_id serial,
    role_name varchar(50),
    Primary Key (role_id)
);

CREATE TABLE users (
    user_id serial,
    first_name varchar(50),
    last_name varchar(50),
    username varchar(100),
    email varchar(150),
    passwordhash varchar(255),
    role_id integer,
    is_active boolean,
    Primary Key (user_id),
    Foreign Key (role_id) References roles(role_id)
);

CREATE TABLE projects (
    project_id serial,
    title varchar(50),
    description varchar(255),
    project_lead integer,
    is_active boolean,
    Primary Key (project_id),
    Foreign Key (project_lead) References users(user_id)
);

Create Table status (
    status_id serial,
    status_name varchar(255),
    Primary Key (status_id)
);

Create Table types (
    type_id serial,
    type_name varchar(255),
    Primary Key (type_id)
);

Create Table ticket_urgency (
    urgency_id serial,
    tu_name varchar(150),
    Primary Key (urgency_id)
);

Create Table tickets (
    ticket_id serial,
    title varchar(127),
    description varchar(255),
    status_id integer,
    type_id integer,
    created timestamp,
    resolved timestamp,
    creator_id integer,
    urgency_id integer,
    project_id integer,
    Primary Key (ticket_id),
    Foreign Key (status_id) References status(status_id),
    Foreign Key (creator_id) References users(user_id),
    Foreign Key (type_id) References types(type_id),
    Foreign Key (urgency_id) References ticket_urgency(urgency_id),
    Foreign Key (project_id) References projects(project_id)
);

Create Table tickets_assigned_users (
    tau_id serial,
    ticket_id integer,
    user_id integer,
    Primary Key (tau_id),
    Foreign Key (ticket_id) References tickets(ticket_id),
    Foreign Key (user_id) References users(user_id)
);

Create Table comments (
    comment_id serial,
    comment_text varchar(255),
    ticket_id integer,
    user_id integer,
    created timestamp,
    Primary Key (comment_id),
    Foreign Key (ticket_id) References tickets(ticket_id),
    Foreign Key (user_id) References users(user_id)
);


--Postgresql Procedurs for use in Service-Layer
--Arguments for CreateTicket(Title, Description, Status-ID, Type-ID, Creator-ID, Priority-ID, Project-ID)
Create Or Replace Procedure CreateTicket(ticket_title text, ticket_description text, ticket_status integer,
                                        ticket_type integer, ticket_creator integer, ticket_urgency integer,
										project_id integer)
AS $$
    Begin
        Insert Into tickets Values(default, ticket_title, ticket_description, ticket_status, ticket_type, CURRENT_TIMESTAMP, NULL, ticket_creator, ticket_urgency, project_id);
    End;
$$ Language plpgsql;

--Arguments for CreateUser(first-name, last-name, username, email, passwordhash, role-id)
Prepare CreateUser(varchar(50), varchar(50), varchar(100), varchar(150), varchar(255), integer) AS
    Insert Into users Values(default, $1, $2, $3, $4, $5, $6, true);

--Arguments for CreateProject(Title, Description, Project-Lead-User)
Prepare CreateProject(varchar(50), varchar(255), integer) AS
    Insert Into projects Values(default, $1, $2, $3, true);

--Arguments for CreateComment(Text, Ticket-ID, Creator-ID)
Prepare CreateComment(varchar(255), integer, integer) AS
    Insert Into comments Values(default, $1, $2, $3, CURRENT_TIMESTAMP);

--Arguments for AssignUsersToTicket(Ticket-ID, User-ID)
Prepare AssignUsersToTicket(integer, integer) AS
    Insert Into tickets_assigned_users Values(default, $1, $2);

/*
--Triggers
--arguments for deletUser(User-ID)
--Trigger not yet usable - need to find a way to pass user-id as argument
Create or Replace Function deleteUser(IN user_id integer)
Returns Trigger
AS $$
    Begin
        Update users
        Set is-active = false, username = '', passwordhash = null, email = null,
        Where user-id = user_id;
        Return null;
    End;
$$ Language plpgsql;

Create or Replace Trigger deleteUser Before
    Delete
    On users
    For Each Row
    Execute Procedure deleteStaff();

--Argument for deleteProject(project-id)
--Trigger not yet usable - need to find a way to pass project-id as argument
Create or Replace Function deletProject(IN project_id integer)
Returns Trigger
AS $$
    Begin
        Update projects
        Set is-active = false,
        Where project-id = project_id;
        Return null;
    End;
$$ Language plpgsql;

Create or Replace Trigger deleteProject Before
    Delete
    On projects
    For Each Row
    Execute Procedure deleteProject();
*/