--Postgresql Procedures for use in Service-Layer -----------------------------------------------------------------------

-- Procedure 1.1.1 - CreateTicket --
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


-- Procedure 1.1.2 - Create User --
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


-- Procedure 1.1.3 - Create Project --
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

-- Procedure 1.1.4 - CreateComment --
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

-- Procedure 1.1.5 - AssignUsersToTicket --
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


-- Update existing Values - Procedures ---------------------------------------------------------------------------------

-- Procedure 1.2.1 - UpdateTicket --
Create or Replace Procedure UpdateTicket(arg_ticket_id integer, update_type_id integer, update_status_id integer, update_project_id integer, update_urgency_id integer)
AS $$
    Begin
        Update tickets
        Set
            type_id = update_type_id,
            status_id = update_status_id,
            project_id = update_project_id,
            urgency_id = update_urgency_id
        Where ticket_id = arg_ticket_id;
    End;
$$ Language plpgsql;

-- Procedure 1.2.2 - UpdateProject --
Create Or Replace Procedure UpdateProject(arg_project_id integer, project_title text, project_description text, user_id integer, project_active boolean)
AS $$
    Begin
        Update projects
        Set
            title = project_title,
            description = project_description,
            project_lead = user_id,
            is_active = project_active
        Where project_id = arg_project_id;
    End;
$$ Language plpgsql;

-- Procedure 1.2.3 - SetProjectInactive --
Create Or Replace Procedure SetProjectInactive(arg_project_id integer)
AS $$
    Begin
        Update projects
        Set is_active = false;
        Where project_id = arg_project_id;
    End;
$$ Language plpgsql;

-- Procedure 1.2.4 - SetProjectActive --
Create Or Replace Procedure SetProjectActive(arg_project_Id integer)
AS $$
    Begin
        Update projects
        Set is_active = true;
        Where project_id = arg_project_id;
    End;
$$ Language plpgsql;

------------------------------------------------------------------------------------------------------------------------
-- Update Procedures - Tests -------------------------------------------------------------------------------------------
Call UpdateTicket(2, 2, 2, 2, 2);
Call UpdateProject(2, 'Update Test', 'DB Project Update Procedure Testing', 2, false);
Call SetProjectActive(2);
Call SetProjectInactive(2);
------------------------------------------------------------------------------------------------------------------------


