package com.application.views;

import com.application.components.Header;
import com.application.components.SingleTicketForm;
import com.application.data.entity.*;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

/*
* Only problem I have is on reload it doesn't reload the content correctly
* And it also needs binding with DB
* At the moment I cant enter a ticket number twice, because I am creating new tickets with the same number and it doesnt like that
* Once we have it working with the database, I need to change it from creating new tickets, to searching for existing ones
* Also need to create a fallback for when the ticket number doesnt exist!
*/
@PermitAll // all roles
@PageTitle("Searched Ticket | Error Annihilator")
@Route(value = "ticket")
@PreserveOnRefresh
public class SingleTicket extends VerticalLayout implements HasUrlParameter<Integer> {
    public SingleTicketForm ticketForm; // Form/Editor
    private final SecurityService securityService;
    Ticket ticket = new Ticket(); // set this ticket when searched
    H1 title = new H1("Ticket # " + ticket.getTicketNumber());
    Header header;
    DatabaseService databaseService;

    public SingleTicket(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService =  new SecurityService(authenticationContext);
        addClassName("assignedTickets-view");

        // This is how to implement the header
        setSizeFull();
        header = new Header(authenticationContext);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    public VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        // Main Page Title
        content.add(title);

        // Add grid and form to content
        configureForm();
        content.add(ticketForm);

        return content;
    }

    // FORM =======================================
    // Configure the editor/form
    private void configureForm() {
        ticketForm = new SingleTicketForm(databaseService); // Replace with actual lists of status
        //ticketForm.setTicket(ticket); // find ticket based on search term
        ticketForm.setSizeFull();
    }

    public void setTitle(Integer param){
        getUI().ifPresent(ui -> ui.access(() -> title.setText("Ticket # " + param)));
    }

    @Override
    public void setParameter(BeforeEvent event, Integer parameter) {
        getUI().flatMap(ui -> {
            ui.access(() -> title.setText("Ticket # " + parameter));
            return Optional.empty();
        });
        try {
            ticket = databaseService.getTicketByIdEntity(parameter);
            ticketForm.setTicket(ticket);
        }
        catch (EmptyResultDataAccessException e){
            getUI().flatMap(ui -> {
                ui.access(() -> title.setText("No Ticket found with number " + parameter));
                return Optional.empty();
            });
        }

    }
}
