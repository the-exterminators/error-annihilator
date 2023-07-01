package com.application.views;

import com.application.components.Header;
import com.application.data.service.TicketService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Create Ticket | Error Annihilator")
@Route(value = "") // create-ticket-view
public class CreateTicket extends VerticalLayout {

    private final TicketService ticketService;
    private final SecurityService securityService;
    private Avatar avatar = new Avatar();
    private NumberField ticketNumber = new NumberField();
    private ComboBox<String> ticketType = new ComboBox<>();
    private ComboBox<String> urgency = new ComboBox<>();

    private ComboBox<String> projectType = new ComboBox<>();
    private TextArea description = new TextArea();

    private TextField ticketName = new TextField();
    private DateTimePicker dateCreated = new DateTimePicker();
    private MenuBar buttons = new MenuBar();

    public CreateTicket(TicketService ticketService, AuthenticationContext authenticationContext) {
        this.ticketService = ticketService;
        securityService = new SecurityService(authenticationContext);
        Header header = new Header(authenticationContext);
        header.setContent(getCreateTicketContent());
        add(header);
    }

    private VerticalLayout getCreateTicketContent() {
        VerticalLayout fullContent = new VerticalLayout();
        fullContent.setHeightFull();
        fullContent.setWidthFull();

        // Main Page Title
        H1 title = new H1("Create a Ticket");
        fullContent.add(title);

        avatar.setName("Isabelle Mariacher");
        fullContent.add(avatar);

        FormLayout formContent = new FormLayout();
        formContent.setHeightFull();
        formContent.setWidthFull();

        ticketNumber.setLabel("Ticket number");
        dateCreated.setLabel("Ticket created");
        ticketName.setLabel("Ticket title");
        projectType.setLabel("Project Type");
        ticketType.setLabel("Ticket type");
        urgency.setLabel("Urgency");
        description.setLabel("Description");


        //set the read only fields
        ticketNumber.setReadOnly(true);
        dateCreated.setReadOnly(true);

        //set sample items
        setUrgencyComboBoxSampleData(urgency);
        setTicketTypeComboBoxSampleData(ticketType);
        setProjectTypeComboBoxSampleData(projectType);


        //set column spans
        formContent.setColspan(urgency, 1);
        formContent.setColspan(projectType, 1);
        formContent.setColspan(ticketType, 1);
        formContent.setColspan(ticketName, 1);
        formContent.setColspan(description, 2);


        //add components to form layout

        formContent.add(ticketNumber);
        formContent.add(dateCreated);
        formContent.add(ticketName);
        formContent.add(projectType);
        formContent.add(ticketType);
        formContent.add(urgency);
        formContent.add(description);



        //add form to main layout
        fullContent.add(formContent);

        //add buttons to main layout
        setMenuBarSampleData(buttons);
        fullContent.add(buttons);

        return fullContent;
    }

    //set sample data for urgency
    private void setUrgencyComboBoxSampleData(ComboBox<String> comboBox) {
        comboBox.setItems("Low", "Medium", "High");
        comboBox.setValue("Medium");
    }

    private void setTicketTypeComboBoxSampleData(ComboBox<String> comboBox) {
        comboBox.setItems("bugs", "defects", "errors");
        comboBox.setValue("defects");
    }

    private void setProjectTypeComboBoxSampleData(ComboBox<String> comboBox) {
        comboBox.setItems("Project 1", "Project 2", "Project 3");
        comboBox.setValue("Project 3");
    }


    //set sample data for the buttons bar
    private void setMenuBarSampleData(MenuBar menuBar) {
        MenuItem creatItem = menuBar.addItem("Create");
        creatItem.addClickListener(event-> {
            String title = ticketName.getValue();
            String desc = description.getValue();

            // added everything with id = 1 for now just to make sure it does something. Has to me changed later on.
            int status = 1; // Set the status value as needed
            int type = 1; // Set the type value as needed
            int creator = 1; // Set the creator value as needed
            int urgency = 1; // Set the creator value as needed
            int projectId = 1;

            ticketService.createTicket(title, desc, status, type, creator, urgency, projectId);
            // Additional logic after creating the ticket (e.g., navigation or displaying a success message)
        });
    }
}

