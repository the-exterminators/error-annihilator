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
