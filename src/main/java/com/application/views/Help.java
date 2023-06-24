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

@PermitAll
@Route(value = "help")
@PageTitle("Help | Error Annihilator")
public class Help extends VerticalLayout {

    private final SecurityService securityService;
    public Help(AuthenticationContext authenticationContext){
        SecurityService securityService = new SecurityService(authenticationContext);
        this.securityService = securityService;
        Header header = new Header(securityService);
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
        menuTabs.add("Dashboard", dashboardContent());
        menuTabs.add("Create Ticket", createTicketContent());
        menuTabs.add("Assigned Tickets", assignedTicketsContent());
        menuTabs.add("User Management", assignedTicketsContent());
        menuTabs.add("Project Management", assignedTicketsContent());
        menuTabs.add("Project Overview", assignedTicketsContent());
        menuTabs.add("Ticket History", assignedTicketsContent());
        menuTabs.add("Ticket Overview", assignedTicketsContent());

        content.add(menuTabs);

        return content;
    }

    // Content of Menu Tab AssignedTickets
    private Component assignedTicketsContent() {
        // Accordion for better orientation
        // https://vaadin.com/docs/latest/components/accordion
        Accordion accordion = new Accordion();

        accordion.add("Basics",
                new Paragraph("In this view you can see all tickets, that were assigned to you! Here you can see the title, description, status and the project it belongs to. You can filter the tickets and view/edit them from here."));

        accordion.add("Filter",
                new Paragraph("You can filter by title, description, status or project by typing in the textfields underneath the column titles. You can combine multiple filter textfields as well."));

        accordion.add("View/Edit",
                new Paragraph("You can view or edit a ticket by clicking on it in the grid. This will open a window with all details. You will only be able to change relevant details and add comments at the bottom."));

        accordion.add("Comments",
                new Paragraph("When viewing/editing a ticket, you can leave comments for example on how you solved the issue etc so your taken steps are understandable in the future."));

        return accordion;
    }

    // Content of Menu Tab Dashboard
    private Component dashboardContent() {
        // Accordion for better orientation
        // https://vaadin.com/docs/latest/components/accordion
        Accordion accordion = new Accordion();

        accordion.add("Basics",
                new Paragraph("In the Dashboard view a manager/admin can view all kinds of statistics revolving around the application Error Annihilator."));

        return accordion;
    }

    // Content of Menu Tab Dashboard
    private Component createTicketContent() {
        // Accordion for better orientation
        // https://vaadin.com/docs/latest/components/accordion
        Accordion accordion = new Accordion();

        accordion.add("Basics",
                new Paragraph("Here you can create and submit a ticket. You need to fill out the title, description, type, urgency and the project it belongs to. The ticket number and created date will be set automatically upon submitting the ticket."));

        return accordion;
    }

}
