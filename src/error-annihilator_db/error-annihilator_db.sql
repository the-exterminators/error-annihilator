CREATE TABLE error-annihilator_db.public users (
    user-id integer NOT NULL AUTO_INCREMENT,
    first-name varchar(50),
    last-name varchar(50),
    username varchar(100),
    email varchar(150),
    passwordhash varchar(255),
    role-id integer,
    is-active boolean,
    Primary Key (user-id),
    Foreign Key (role-id) References roles(role-id)
);

CREATE TABLE error-annihilator_db.public roles (
    role-id NOT NULL AUTO_INCREMENT,
    name varchar(50),
    Primary Key (role-id)
);

CREATE TABLE error-annihilator_db.public projects (
    project-id integer NOT NULL AUTO_INCREMENT,
    title varchar(50),
    description varchar(255),
    project-lead integer,
    is-active boolean,
    Primary Key (project-id),
    Foreign Key (project-lead) References users(user-id)
);

Create Table error-annihilator_db.public status (
    status-id integer NOT NULL AUTO_INCREMENT,
    name varchar(255),
    Primary Key (status-id)
);

Create Table error-annihilator_db.public comments (
    comment-id integer NOT NULL AUTO_INCREMENT,
    text varchar(255),
    ticket-id integer,
    user-id integer,
    created timestamp,
    Primary Key (comment-id),
    Foreign Key (ticket-id) References tickets(ticket-id),
    Foreign Key (user-id) References users(user-id)
);

Create Table error-annihilator_db.public types (
    type-id integer NOT NULL AUTO_INCREMENT,
    name varchar(255),
    Primary Key (type-id)
);

Create Table error-annihilator_db.public tickets (
    ticket-id integer NOT NULL AUTO_INCREMENT,
    title varchar(127),
    description varchar(255),
    status-id integer,
    type-id integer,
    created timestamp,
    resolved timestamp,
    creator-id integer,
    urgency-id integer,
    Primary Key (ticket-id),
    Foreign Key (status-id) References status(status-id),
    Foreign Key (creator-id) References users(user-id),
    Foreign Key (type-id) References types(type-id),
    Foreign Key (urgency-id) References ticket-urgency(urgency-id)
);

Create Table error-annihilator_db.public tickets-assigned-users (
    tau-id integer NOT NULL AUTO_INCREMENT,
    ticket-id integer,
    user-id integer,
    Primary Key (tau-id),
    Foreign Key (ticket-id) References tickets(ticket-id),
    Foreign Key (user-id) References users(user-id)
);

Create Table error-annihilator_db.public ticket-urgency (
    urgency-id integer NOT NULL AUTO_INCREMENT,
    name varchar(150)
);

--Prepared Statements for use in Service-Layer

--Arguments for CreateTicket(Title, Description, Status-ID, Type-ID, Creator-ID, Priority-ID)
Prepare CreateTicket(varchar(127), varchar(255), integer, integer, integer, integer) AS
Insert Into tickets Values(default, $1, $2, $3, $4, CURRENT_TIMESTAMP, NULL, $5, $6);

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
Insert Into tickets-assigned-users Values(default, $1, $2);


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