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


--Postgresql Procedures for use in Service-Layer -----------------------------------------------------------------------
--Arguments for CreateTicket(Title, Description, Status-ID, Type-ID, Creator-ID, Priority-ID, Project-ID)
Create Or Replace Procedure CreateTicket(ticket_title text, ticket_description text, ticket_status integer,
                                        ticket_type integer, ticket_creator integer, ticket_urgency integer,
										project_id integer)
AS $$
    Declare
        currentMAX_ticket_id integer;
    Begin
        Select max(ticket_id)
        Into currentMAX_ticket_id
        From tickets;
        currentMAX_ticket_id := currentMAX_ticket_id + 1;
        Insert Into tickets Values(currentMAX_ticket_id, ticket_title, ticket_description, ticket_status, ticket_type, CURRENT_TIMESTAMP, NULL, ticket_creator, ticket_urgency, project_id);
    End;
$$ Language plpgsql;

--Arguments for CreateUser(first-name, last-name, username, email, passwordhash, role-id)
Create or Replace Procedure CreateUser(user_first_name text, user_last_name text, user_username text,
                                        user_email text, user_passwordhash text, user_role_id integer)
As $$
    Declare
        currentMAX_user_id integer;
    Begin
        Select max(user_id)
        Into currentMAX_user_id integer
        From users;
        currentMAX_user_id := currentMAX_user_id + 1;
        Insert Into users Values(currentMAX_user_id, user_first_name, user_last_name, user_username, user_email, user_passwordhash, user_role_id, true);
    End;
$$ Language plpgsql;

--Arguments for CreateProject(Title, Description, Project-Lead-User)
Create or Replace Procedure CreateProject(project_title text, project_description text, lead_user_id integer)
AS $$
    Declare
        currentMAX_project_id integer;
    Begin
        Select max(project_id)
        Into currentMAX_project_id
        From projects;
        currentMAX_project_id := currentMAX_project_id + 1;
        Insert Into projects Values(currentMAX_project_id, project_title, project_description, lead_user_id, true);
    End;
$$ Language plpgsql;

--Arguments for CreateComment(Text, Ticket-ID, Creator-ID)
Create or Replace Procedure CreateComment(comment_text text, ticket_id integer, user_id integer)
AS $$
    Declare
        currentMAX_comment_id integer;
    Begin
        Select max(comment_id)
        Into currentMAX_comment_id
        From comments;
        currentMAX_comment_id := currentMAX_comment_id + 1;
        Insert Into comments Values(currentMAX_comment_id, comment_text, ticket_id, user_id, CURRENT_TIMESTAMP);
    End;
$$ Language plpgsql;

--Arguments for AssignUsersToTicket(Ticket-ID, User-ID)
Create or Replace Procedure AssignUsersToTicket(ticket_id integer, user_id integer)
AS $$
    Declare
        currentMAX_tau_id integer;
    Begin
        Select max(tau_id)
        Into currentMAX_tau_id
        From tickets_assigned_users;
        currentMAX_tau_id := currentMAX_tau_id + 1;
        Insert Into tickets_assigned_users Values(currentMAX_tau_id, ticket_id, user_id);
    End;
$$ Language plpgsql;
------------------------------------------------------------------------------------------------------------------------

-- Test all 'Set' Procedures -------------------------------------------------------------------------------------------
Call CreateUser('John', 'Doe', 'jDoe', 'john@doe.com', '', 4);
Call CreateTicket('Test-Ticket', 'This Ticket is for DB testing', 1, 1, 6, 1, 1);
Call CreateProject('Test-Project', 'DB testing Project', 6);
Call CreateComment('comment for DB-testing', 2, 6);
Call AssignUsersToTicket(2, 6);
------------------------------------------------------------------------------------------------------------------------

-- Basic/Static Data for Tables: Roles, Status, Ticket Urgency and Ticket Types ----------------------------------------
Insert Into roles Values('Administrator');
Insert Into roles Values('Project Lead');
Insert Into roles Values('Developer');
Insert Into roles Values('User');

Insert Into status Values('New');
Insert Into status Values('In Progress');
Insert Into status Values('Waiting For Approval');
Insert Into status Values('Resolved');
Insert Into status Values('Reopened');
Insert Into status Values('Rejected');

Insert Into ticket_urgency Values('Cosmetic');
Insert Into ticket_urgency Values('Low');
Insert Into ticket_urgency Values('Medium');
Insert Into ticket_urgency Values('High');
Insert Into ticket_urgency Values('Higher');
Insert Into ticket_urgency Values('Highest');
Insert Into ticket_urgency Values('Critical');

Insert Into types Values('Bug');
Insert Into types Values('Defect');
Insert Into types Values('Change Request');
Insert Into types Values('System Failure');
------------------------------------------------------------------------------------------------------------------------
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


-- Grant Access Rights on Tables for all Users
GRANT ALL ON TABLE public.comments TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.projects TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.roles TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.status TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.ticket_urgency TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.tickets TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.tickets_assigned_users TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.types TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;
GRANT ALL ON TABLE public.users TO janaburns, application, danielkihn, isabellemariacher, manuelaudino, postgres, test;

