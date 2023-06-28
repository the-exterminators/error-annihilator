/* Hello! I have most of stefans SQL-Code in here as well, as I was playing in a different playground at some point and needed
to recreate or database!
You can use the navigation below my copying and searching the terms in this file. The uppercase words can be searched as is,
with the functions etc you might have to add a '(' or so to find them right away!
Im available for any questions and also this doesnt include Isabelles views (yet)!
*/

/* Navigation ===============================
*   - CREATE TABLES _______________________ *
*       - roles                             *
*       - users                             *
*       - projects                          *
*       - status                            *
*       - types                             *
*       - ticket_urgency                    *
*       - tickets                           *
*       - tickets_assigned_users            *
*       - comments                          *
*   - CREATE PROCEDURES ___________________ *
*       - CreateTicket                      *
*       - CreateUser                        *
*       - CreateComment                     *
*       - CreateProject                     *
*       - AssignUserToTicket                *
*   - DELETE TRIGGERS _____________________ *
*       - DeleteUser                        *
*       - DeleteProject                     *
*   - GET FUNCTIONS _______________________ *
*       - GetAllUsers                       *
*       - GetTicketsAssignedTo              *
*       - SearchTicketByID                  *
*       - GetCommentsByTicketID             *
*       - GetAssignedUsersByTicketID        *
*   - WISHLIST ____________________________ *
*   - DROPS _______________________________ *
=========================================== */



-- CREATE TABLES ================================================================================================================
-- Just copied from Stefans exisiting code
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


-- CREATE PROCEDURES ==========================================================================================================

Create Or Replace Procedure CreateTicket(ticket_title text, ticket_description text, ticket_status integer,
                                        ticket_type integer, ticket_creator integer, ticket_urgency integer,
										project_id integer)
AS $$
    Begin
        Insert Into tickets
        Values(default, ticket_title, ticket_description, ticket_status, ticket_type, CURRENT_TIMESTAMP, NULL, ticket_creator, ticket_urgency, project_id);
    End;
$$ Language plpgsql;

-- Postgresql Procedurs for use in Service-Layer
-- Arguments for CreateTicket(Title, Description, Status-ID, Type-ID, Creator-ID, Priority-ID, Project-ID)
-- Just copied from Stefans exisiting code

-- -----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE CreateUser(varchar(50), varchar(50), varchar(100), varchar(150), varchar(255), integer)
AS $$
	BEGIN
		INSERT
		INTO users
		VALUES (default, $1, $2, $3, $4, $5, $6, true);
	END
$$ LANGUAGE plpgsql;

-- Creates a new User
-- Variables: first name, last name, username, email, passwordhash, role_id
-- Username and email should be unique - do we have to change that in the table itself or in the procedure here?
-- To be used in User Management

-- -----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE CreateComment(varchar(255), integer, integer)
AS $$
	BEGIN

		INSERT
		INTO comments
		VALUES (default, $1, $2, $3, CURRENT_TIMESTAMP);
	END
$$ LANGUAGE plpgsql;

-- Creates a new comment - comment_id and created are filled automatically
-- Variables Text, ticket_id, user_id
-- To be used in assigned tickets etc

-- -----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE CreateProject(varchar(50), varchar(255), integer)
AS $$
	BEGIN
		INSERT
		INTO projects
		VALUES (default, $1, $2, $3, true);
	END
$$ LANGUAGE plpgsql;

-- Creates a new project
-- Variables: Title, Description, User_id

-- -----------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE AssignUserToTicket(integer, integer)
AS $$
	BEGIN
		INSERT
		INTO projects
		VALUES (default, $1, $2);
	END
$$ LANGUAGE plpgsql;

-- Creates a new project
-- Variables: ticket_id, user_id


-- DELETE TRIGGERS ==========================================================================================================
-- Hab die Triggers schon so erstellt und kann sie nicht mehr löschen, weil ich nicht der owner bin - sorry!

CREATE OR REPLACE FUNCTION DeleteUser()
RETURNS trigger AS $$
	BEGIN
		UPDATE users
		SET username = '', password = null, email = null, is_active = false
		WHERE user_id = OLD.user_id;
		return null;
	END
$$ LANGUAGE plpgsql;

CREATE TRIGGER DeleteUser BEFORE DELETE ON users
FOR EACH ROW
EXECUTE PROCEDURE DeleteUser();

-- Creates a trigger, so users aren’t actually deleted but just set to inactive while also setting username, password and email to null

-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION DeleteProject()
RETURNS trigger AS $$
	BEGIN
		UPDATE projects
		SET is_active = false
		WHERE project_id = OLD.project_id;
		return null;
	END
$$ LANGUAGE plpgsql;

CREATE TRIGGER DeleteProject BEFORE DELETE ON projects
FOR EACH ROW
EXECUTE PROCEDURE DeleteProject();

-- Creates a trigger, so projects aren’t actually deleted but just set to inactive


-- GET FUNCTIONS ==========================================================================================================

CREATE OR REPLACE FUNCTION GetAllUsers()
RETURNS setof users AS $$
	BEGIN
		SELECT *
		FROM users
		WHERE is_active = true;
	END
$$ LANGUAGE plpgsql;

-- Gets all active users to be displayed
-- To be used in user management

-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION GetTicketsAssignedTo(integer)
RETURNS setof tickets AS $$
	BEGIN
		SELECT *
		FROM tickets
		JOIN tickets_assigned_users USING (ticket_id)
		WHERE user_id = $1
	END
$$ LANGUAGE plpgsql;

-- Gets tickets assigned to a given user_id
-- To be used in assigned tickets

-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION SearchTicketByID(integer)
RETURNS setof tickets AS $$
	BEGIN
		SELECT *
		FROM tickets
		WHERE ticket_id = $1
	END
$$ LANGUAGE plpgsql;

-- Gets a ticket by id
-- To be used for single ticket search

-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION GetCommentsByTicketID(integer)
RETURNS setof comments AS $$
	BEGIN
		SELECT *
		FROM comments
		WHERE ticket_id = $1;
	END
$$ LANGUAGE plpgsql;

-- Gets comments belonging to a certain ticket
-- To be used in multiple views, that display a detail page of the ticket (assigned tickets, single ticket,…)

-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION GetAssignedUsersByTicketID(integer)
RETURNS setof users AS $$
	BEGIN
		SELECT *
		FROM tickets_assigned_users
		JOIN users USING (user_id)
		WHERE ticket_id = $1;
	END
$$ LANGUAGE plpgsql;

-- Gets assigned users of a ticket by ticket_id
-- To be used in multiple views, that display a detail page of the ticket (assigned tickets, single ticket, …)

-- WISHLIST :) =====================================================================================================================

-- GetUser(): get a single user by ID (or maybe username/email, if we make those unique) and return all infos for the my profile view

-- UpdateUser(): for the user management view I need a function that allows me to change the details of a user. That can be any of the fields except ID and is_active.
-- I might also need a version of this for the my profile view, where the user cant change his own role!

-- UpdateTicket(): on the assigned tickets view I can change the type, status, project and assignees - the rest cant be changed from here.

-- GetAllSubmittedTickets(): count all tickets that were submitted each day (for a timeline statistic of submitted tickets per day - view dashboard)

-- TicketSubmissionDays(): (maybe sorted by type) get the average of the tickets submitted on each weekday - for dashboard view

-- GetStatusCount(): count all tickets of every status (except done/closed, because realistically after a while this column would be much larger than the rest) - for dashboard

-- GetTypeCount(): Count how many tickets of the separate types are in the database - for dashboard view

-- GetTurnaroundTime(): get alls resolved tickets, split them by type and calculate the average turnaround time per type -- for dashboard

-- GetResolvedTickets(): count how many tickets werde resolved each day (for a timeline statistic - dashboard)


-- DROPS =======================================================================================================================

DROP function GetAllUsers();
DROP function GetTicketsAssignedTo(integer);
DROP function SearchTicketByID(integer);
DROP function GetCommentsByTicketID(integer);
DROP function GetAssignedUsersByTicketID(integer);
DROP Procedure CreateTicket(text, text, integer, integer, integer, integer, integer);
DROP procedure CreateComment(varchar(255), integer, integer);
DROP procedure CreateUser(varchar(50), varchar(50), varchar(100), varchar(150), varchar(255), integer);
DROP procedure AssignUserToTicket(integer, integer);
DROP procedure CreateProject(varchar(50), varchar(255), integer)
-- Only owner can drop triggers
-- DROP Trigger DeleteProject ON projects;
-- DROP Trigger DeleteUser ON users;
-- DROP function DeleteProject();
-- DROP function DeleteUser();
