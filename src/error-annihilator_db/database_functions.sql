-- Stored Functions ----------------------------------------------------------------------------------------------------

-- Function 1.1 -- GetAssignedTickets --
Create or Replace Function GetAssignedTickets(assigned_user_id integer)
returns Setof tickets
AS $$
    Declare
        result tickets%ROWTYPE;
    Begin
        For result In Select *
            From tickets
            Left Join tickets_assigned_users
            On tickets.ticket_id = tickets_assigned_users.ticket_id
            Where tickets_assigned_users.user_id = assigned_user_id
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.2 - GetAllProjects --
Create or Replace Function GetAllProjects()
returns Setof projects
AS $$
    Declare
        result projects%ROWTYPE;
    Begin
        For result in Select *
            From projects
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

--Function 1.3 - GetCurrentUserInfo --
Create or Replace Function GetCurrentUserInfo(arg_user_id integer)
Returns Table(first_name varchar(50), last_name varchar(50), username varchar(100), email varchar(150))
AS $$
    Begin
        Return Query
            Select users.first_name, users.last_name, users.username, users.email
            From users
            Where user_id = arg_user_id;
        Return;
    End;
$$ Language plpgsql;

--Function 1.4 - LoginGetUsernamePasswordhash --
Create Or Replace Function LoginGetUsernamePasswordhash(arg_username text)
Returns varchar(255)
AS $$
    Declare
        return_passwordhash varchar(255);
    Begin
        Select users.passwordhash
        Into return_passwordhash
        From users
        Where users.username = arg_username;
        Return return_passwordhash;
    End;
$$ Language plpgsql;

--Function 1.5 - GetAllUsers --
Create Or Replace Function GetAllUsers()
Returns Setof users
AS $$
    Declare
        result users%ROWTYPE;
    Begin
        For result in Select *
            From users
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.6 - GetAllUserRoles --
Create Or Replace Function GetAllUserRoles()
Returns Setof roles
AS $$
    Declare
        result roles%ROWTYPE;
    Begin
        For result in Select*
            From roles
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.7 - GetCurrentUserRole --
Create Or Replace Function GetCurrentUserRole(arg_user_id integer)
Returns varchar(50)
AS $$
    Declare
        result varchar(50);
    Begin
        Select roles.role_name
        Into result From roles
        Left Join users
        On roles.role_id = users.role_id
        Where user_id = arg_user_id;
        Return result;
    End;
$$ Language plpgsql;

-- Function 1.8 - GetAllCreatedTickets --
Create Or Replace Function GetAllCreatedTickets(arg_user_id integer)
Returns Setof tickets
AS $$
    Declare
        result tickets%ROWTYPE;
    Begin
        For result in Select *
            From tickets
            Where creator_id = arg_user_id
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.9 - GetAllUsersAssignedToTicket --
Create Or Replace Function GetAllUsersAssignedToTicket(arg_ticket_id integer)
Returns Table(user_id integer, username varchar(100))
AS $$
    Begin
        Return Query
            Select users.user_id, users.username
            From users
            Left Join tickets_assigned_users
            On users.user_id = tickets_assigned_users.user_id
            Where ticket_id = arg_ticket_id;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.10 - GetProject --
Create Or Replace Function GetProject(arg_project_id integer)
Returns Table(project_title varchar(50), project_description varchar(255), project_lead_user integer, project_isactive boolean)
AS $$
    Begin
        Return Query
            Select projects.title, projects.description, projects.project_lead, projects.is_active
            From projects
            Where project_id = arg_project_id;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.11 - GetAllTicketsFromProject --
Create Or Replace Function GetAllTicketsFromProject(arg_project_id integer)
Returns Setof tickets
AS $$
    Declare
        result tickets%ROWTYPE;
    Begin
        For result in Select *
            From tickets
            Where project_id = arg_project_id
        Loop
            Return Next result;
        End Loop;
        Return;
    End;
$$ Language plpgsql;

-- Function 1.12 - GetTicketResolvedDeltaCreated --
Create Or Replace Function GetTicketResolvedDeltaCreated(arg_ticket_id integer)
Returns float
AS $$
	Declare
		delta_time interval;
		var_created timestamp;
		var_resolved timestamp;
	Begin
		Select created, resolved
		Into var_created, var_resolved
		From tickets
		Where ticket_id = arg_ticket_id;
		delta_time := var_resolved - var_created;
		Return Extract(epoch From delta_time)::double precision;
	End;
$$ Language plpgsql;

------------------------------------------------------------------------------------------------------------------------
-- Views - Tests -------------------------------------------------------------------------------------------------------
-- Gets Tickets Assigned to User: John Doe
Select * From GetAssignedTickets(6);
Select * From GetAllProjects();
Select * From GetCurrentUserInfo(6);
Select * From LoginGetUsernamePasswordhash('jaDoe');
Select * From GetAllUsers();
Select * From GetAllUserRoles();
Select * From GetCurrentUserRole(6);
Select * From GetAllCreatedTickets(1);
Select * From GetAllUsersAssignedToTicket(1);
Select * From GetProject(1);
Select * From GetAllTicketsFromProject(2);
Select * From GetTicketResolvedDeltaCreated(1);
------------------------------------------------------------------------------------------------------------------------