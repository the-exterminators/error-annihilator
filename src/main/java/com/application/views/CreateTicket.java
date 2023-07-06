package com.application.views;

import com.application.components.Header;
import com.application.data.service.DatabaseManager;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@PermitAll
@PageTitle("Create Ticket | Error Annihilator")
@Route(value = "") // create-ticket-view
public class CreateTicket extends VerticalLayout {

    private final DatabaseService databaseService;
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

    public CreateTicket(AuthenticationContext authenticationContext) {
        this.databaseService = DatabaseManager.getDatabaseService();
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

        // Avatar needs to be changed to the real user later on
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

        //set the combo boxes
        setUrgencyComboBox(urgency);
        setTicketTypeComboBox(ticketType);
        setProjectTypeComboBox(projectType);


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
        setMenuBar(buttons);
        fullContent.add(buttons);

        return fullContent;
    }

    private void setUrgencyComboBox(ComboBox<String> comboBox) {
        List<String> urgencyItems = databaseService.getAllUrgencyItems();
        comboBox.setItems(urgencyItems);
        comboBox.setValue(urgencyItems.get(0));
    }


    private void setTicketTypeComboBox(ComboBox<String> comboBox) {
        List<String> ticketTypes = databaseService.getAllTicketTypes();
        comboBox.setItems(ticketTypes);
        comboBox.setValue(ticketTypes.get(0));
    }

    private void setProjectTypeComboBox(ComboBox<String> comboBox) {
        List<String> projectTypes = databaseService.getAllProjectItems();
        comboBox.setItems(projectTypes);
        comboBox.setValue(projectTypes.get(0));
    }



     //Button bar containing a Create button
    private void setMenuBar(MenuBar menuBar) {
        MenuItem creatItem = menuBar.addItem("Create");
        creatItem.addClickListener(event-> {

            // Get the ticketName and description as added by the user
            String title = ticketName.getValue();
            String desc = description.getValue();

            // Validate inputs
            if (title.isEmpty() || desc.isEmpty()) {
                Notification.show("Please enter a title and description!.",
                        5000,
                        Notification.Position.MIDDLE);
                return;
            }

            try {
                // Always sets the status of a newly created ticket to "New"
                int statusId = 1;

                // Get the ticket type
                String selectedTicketType = ticketType.getValue();
                int typeId = databaseService.getTicketTypeId(selectedTicketType);

                // Get the urgency
                String selectedUrgency = urgency.getValue();
                int urgencyId = databaseService.getUrgencyId(selectedUrgency);

                // Get the project type
                String selectedProjectItem = projectType.getValue();
                int projectId = databaseService.getProjectId(selectedProjectItem);

                // Get the id of the user that created the ticket, added later on
                int creatorId = 1; // Defined later on

                // Create the ticket
                databaseService.createTicket(title, desc, statusId, typeId, creatorId, urgencyId, projectId);

                // Provide success feedback after creating the ticket
                Notification.show("Ticket created successfully!",
                        5000,
                        Notification.Position.MIDDLE);

                // Clear the data in the title and description fields and reset the combo boxes to the first value in the list
                ticketName.clear();
                description.clear();
                ticketType.setValue(databaseService.getTicketTypeName(1));
                urgency.setValue(databaseService.getUrgencyName(1));
                projectType.setValue(databaseService.getProjectName(1));
            } catch (Exception e) {
                e.printStackTrace();
                // Handle database connection error
                Notification.show("Failed to create ticket. No connection to the database. Please try again later.",
                        8000,
                        Notification.Position.MIDDLE);
            }
        });
    }
}

