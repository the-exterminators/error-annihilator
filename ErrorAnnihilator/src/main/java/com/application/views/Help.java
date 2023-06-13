package com.application.views;

import com.application.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "help")
@PageTitle("Help | Error Annihilator")
public class Help extends VerticalLayout {
    public Help(){
        Header header = new com.application.components.Header();
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
        menuTabs.add("Assigned Tickets", assignedTicketsContent());

        content.add(menuTabs);

        return content;
    }

    // Content of Menu Tab AssignedTickets
    private Component assignedTicketsContent() {
        // Accordion for better orientation
        // https://vaadin.com/docs/latest/components/accordion
        Accordion accordion = new Accordion();

        Paragraph helpOne = new Paragraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        accordion.add("Placeholder 1", helpOne);

        Paragraph helpTwo = new Paragraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        accordion.add("Placeholder 2", helpTwo);

        return accordion;
    }

    // Content of Menu Tab Dashboard
    private Component dashboardContent() {
        // Accordion for better orientation
        // https://vaadin.com/docs/latest/components/accordion
        Accordion accordion = new Accordion();

        Paragraph helpOne = new Paragraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        accordion.add("Placeholder I", helpOne);

        Paragraph helpTwo = new Paragraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        accordion.add("Placeholder II", helpTwo);

        return accordion;
    }

}
