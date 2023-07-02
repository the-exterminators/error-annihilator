-- Views ---------------------------------------------------------------------------------------------------------------
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


------------------------------------------------------------------------------------------------------------------------
-- Views - Tests -------------------------------------------------------------------------------------------------------
-- Gets Tickets Assigned to User: John Doe
Select * From GetAssignedTickets(6);
------------------------------------------------------------------------------------------------------------------------