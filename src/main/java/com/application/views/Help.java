package com.application.views;

import com.application.components.Header;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PermitAll
@Route(value = "help")
@PageTitle("Help | Error Annihilator")
public class Help extends VerticalLayout {

    private final SecurityService securityService;
    private String authorities;
    public Help(AuthenticationContext authenticationContext){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authorities = authentication.getAuthorities().toString();
        this.securityService = new SecurityService(authenticationContext);
        Header header = new Header(authenticationContext);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
        setSizeFull();

    }

    // all content is created here
    private VerticalLayout getContent(){
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        // Title
        H1 title = new H1("Help");
        content.add(title);

        // Menu Tabs
        TabSheet menuTabs = new TabSheet();
        menuTabs.add("Login", loginContent());
        menuTabs.add("Create Ticket", createTicketContent());
        menuTabs.add("Ticket History", historyContent());
        menuTabs.add("User Profile", profileContent());
        if(authorities.contains("ADMIN")) {
            menuTabs.add("Dashboard", dashboardContent());
            menuTabs.add("User Management", userContent());
            menuTabs.add("Project Management", projectContent());
        }

        if (authorities.contains("DEVELOPER") || authorities.contains("PROJECT_LEAD") || authorities.contains("ADMIN")){
            menuTabs.add("Project Overview", overviewContent());
            menuTabs.add("Assigned Tickets", assignedContent());
        }

        content.add(menuTabs);

        return content;
    }

    // Content of Menu Tab Dashboard
    private Component dashboardContent() {
        return new Paragraph("In the Dashboard view a manager/admin can view all kinds " +
                "of statistics revolving around the application Error Annihilator.");
    }

    // Content of Menu Tab Create Ticket
    private Component createTicketContent() {
        return new Paragraph("Here you can create and submit a ticket. You need to " +
                "fill out the title, description, type, urgency and the project it belongs " +
                "to. The ticket number and created date will be set automatically upon " +
                "submitting the ticket.");
    }

    // Content of Menu Tab Dashboard
    private Component loginContent() {
        return new Paragraph("Our application is only accessible to logged in users. As this application is intended " +
                "for companies to use with the clients they sold their products to, we do not want any other people to be able " +
                "to register. The users are created and maintained by the admin. The idea is that the admin creates a user for " +
                "the client that bought their product, so they can create tickets when necessary." +
                "Having the application not accessible  for external users, reduces the risk of forms being filled with spam.");
    }

    // Content of Menu Tab Ticket History
    private Component historyContent(){
        return new Paragraph("Here a user can view all tickets they have submitted. " +
                "They can get a quick overview of the status of a ticket. They can also view " +
                "the details of their tickets here. As long as the status of a ticket is still " +
                "'new', the user can adjust the ticket details, in case they made a mistake. " +
                "As soon as, it isn't new anymore, they can't change the details, as we want to " +
                "be able to track changes through comments and communication. This way changes " +
                "would be lost, as the developer might already be working on the ticket." +
                "If a ticket is already closed and you feel as though it wasn't resolved, you can " +
                "reopen the ticket with a short comment on why you need this ticket reopened.");
    }


    // Content of Menu Tab User Profile
    private Component profileContent(){
        return new Paragraph("Here you can edit the details of your profile. " +
                "These details include first name, last name, username, email and " +
                "password. You can't change your own user role, that has to be done " +
                "by an admin!");
    }

    // Content of Menu Tab Project Overview
    private Component overviewContent(){
        Accordion accordion = new Accordion();
        accordion.add("Overview", new Paragraph("Here admins, project leads and developers can view " +
                "all active projects, with a quick view of their title, description and " +
                "project lead. When they click on a project, it leads to the Project Single " +
                "View"));
        accordion.add("Single Project", new Paragraph("Here admins, project leads and developers can view all " +
                "information regarding a certain project, like title, description, project " +
                "lead and all tickets belonging to that project. From here it is easy to get " +
                "an overview, what tickets, need to be done or are currently in progress in a " +
                "project. A project lead or admin can easily assign tickets to developers " +
                "from this view. You can also click on a ticket, to edit certain details or " +
                "view more details!"));
        return accordion;
    }

    // Content of Menu Tab Assigned Tickets
    private Component assignedContent(){
        return new Paragraph("Here developers can easily see what tickets have been " +
                "assigned to them to work on. This way they don't get lost in an overview " +
                "for all tickets, but only have their own there to work on. They can click " +
                "on a ticket to view more details or change certain details like the status.");
    }

    // Content of Menu Tab User Management
    private Component userContent(){
        return new Paragraph("An admin has an easy overview of all users in the system " +
                "on this view. They see certain info right away and for more they must click " +
                "on the user. Here they can see more, edit or delete the user. They can also add " +
                "a new user, by clicking the button 'New User'. This is the only way for a user " +
                "to be register in the system.");
    }

    // Content of Menu Tab Project Management
    private Component projectContent(){
        return new Paragraph("Here an admin can manage all exisiting projects. There " +
                "is a quick overview of all projects and when you click on one, you can " +
                "edit or delete it. You can also add a new project from here.");
    }

}
