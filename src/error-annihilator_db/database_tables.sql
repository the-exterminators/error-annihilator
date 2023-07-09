-- This SQL File is a representation of the Tables implemented in our Database

CREATE TABLE roles (
    role_id serial,
    role_name varchar(50),
    role_security varchar(255),
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