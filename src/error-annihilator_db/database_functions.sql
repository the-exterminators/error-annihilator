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

-- Function 1.7 GetCurrentUserRole --
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

-- Function 1.8 GetAllCreatedTickets --
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
------------------------------------------------------------------------------------------------------------------------