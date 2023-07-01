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
