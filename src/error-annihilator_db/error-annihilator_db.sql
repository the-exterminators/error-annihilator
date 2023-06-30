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

