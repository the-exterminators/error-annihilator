package com.application.components;

import com.application.data.entity.*;
import com.application.data.service.DatabaseService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@PermitAll
public class TicketHistoryForm extends FormLayout {
    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class); // to bind to Ticket Entity
    Ticket ticket; // To replace with current ticket, that is selected

    // Ticket entity fields
    TextField ticketName = new TextField("Title");
    TextField ticketNumber = new TextField("Ticket Number");
    TextArea description = new TextArea("Description");
    ComboBox<String> ticketType = new ComboBox<>("Type"); // I want this to be a dropdown of predefined types
    ComboBox<String> ticketProject = new ComboBox<>("Project"); // Change it to Projects
    DateTimePicker createdTimeStamp = new DateTimePicker("Date Created");
    ComboBox<TicketStatus> ticketStatus = new ComboBox<>("Status"); // I want this to be a dropdown of predefined statuses
    TextField ticketCreator = new TextField("Creator");
    EmailField creatorMail = new EmailField("Creator Email");
    ComboBox<List<User>> assignedUsers = new ComboBox<>("Assignees"); // for binding
    MultiSelectComboBox<String> assignedUsersMulti = new MultiSelectComboBox<>("Assignees"); // for display

    // Comment variables
    MessageList ticketComments = new MessageList(); // The end display
    ComboBox<List<TicketComment>> ticketComment = new ComboBox<>("Comments"); // to bind
    MessageInput input = new MessageInput();
    List<TicketComment> commentList = new ArrayList<>(); // temp List

    // Buttons
    Button reopen = new Button("Reopen");
    Button close = new Button("Close");

    // comments
    Component commentSection = createCommentsLayout();
    // buttons
    HorizontalLayout buttonSection;

    DatabaseService databaseService;

    // Constructor
    public TicketHistoryForm(DatabaseService databaseService) {
        this.databaseService = databaseService;
        addClassName("ticket-form");

        ticketNumber.setPattern("\\d*");

        configureBind();

        buttonSection = createButtonsLayout();

        // set items and labels for lists
        ticketStatus.setItemLabelGenerator(TicketStatus::getStatusName);
        ticketComment.setItems(Collections.emptyList());

        // set items Assigned Users
        assignedUsers.setItems(Collections.emptyList());
        updateAssignedUsers();
        assignedUsers.addValueChangeListener(event -> updateAssignedUsers());

        setProjectSampleData(ticketProject);
        setTypeSampleData(ticketType);
        setStatusSampleData(ticketStatus);

        HorizontalLayout formRow = new HorizontalLayout(createdTimeStamp, ticketCreator, creatorMail);
        formRow.addClassName("formRow");
        setColspan(formRow, 2);

        configureFormLayout();

        // add to form layout
        add(buttonSection,
            ticketNumber,
            ticketName,
            description,
            formRow,
            ticketType,
            ticketStatus,
            ticketProject,
            assignedUsersMulti,
            commentSection
        );

    }
    // Sample data for project, type and status
    private void setProjectSampleData(ComboBox<String> comboBox){
        List<String> projects = databaseService.getAllProjectItems();
        comboBox.setItems(projects);
        comboBox.setValue(projects.get(0));
    }
    private void setTypeSampleData(ComboBox<String> comboBox){
        List<String> ticketTypes = databaseService.getAllTicketTypes();
        comboBox.setItems(ticketTypes);
        comboBox.setValue(ticketTypes.get(0));
    }
    private void setStatusSampleData(ComboBox<TicketStatus> comboBox){
        comboBox.setItems(new TicketStatus("unassigned"), new TicketStatus("open"), new TicketStatus("in progress"), new TicketStatus("waiting for approval"), new TicketStatus("closed"));
    }

    // Update Assigned Users on select in grid
    public void updateAssignedUsers() {
        List<String> list = new ArrayList<>();
        if(ticket != null) {
            for (User user : this.ticket.getAssignedUsers()) {
                list.add(user.getUserName());
            }
            assignedUsersMulti.setItems(list);
            assignedUsersMulti.select(list);
        }
    }

    // To bind everything to ticket entity
    private void configureBind() {
        // Bind created Timestamp
        binder.bind(createdTimeStamp,
                (ValueProvider<Ticket, LocalDateTime>) ticket -> ticket.getCreatedTimeStamp() == null ? null :
                        LocalDateTime.ofInstant(ticket.getCreatedTimeStamp().toInstant(), ZoneId.systemDefault()),
                (Setter<Ticket, LocalDateTime>) (ticket, ldt) -> {
                    Timestamp toSet = ldt == null ? null : Timestamp.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    ticket.setCreatedTimeStamp(toSet);
                }
        );

        // Bind rest
        //binder.bind(ticketNumber, "ticketNumber");
        binder.forField(ticketNumber)
                .withValidator(new RegexpValidator("Only numbers allowed!", "\\d*"))
                .bind(Ticket::getTicketNumber, Ticket::setTicketNumber);
        binder.bind(ticketName, "ticketName");
        binder.bind(description, "description");
        binder.bind(ticketType, "ticketType");
        binder.bind(ticketStatus, "ticketStatus");
        binder.bind(ticketProject, "ticketProject.projectName");
        binder.bind(ticketCreator, "ticketCreator.userName");
        binder.bind(creatorMail, "ticketCreator.email");
        binder.bind(assignedUsers, "assignedUsers");
        binder.bind(ticketComment, "ticketComment");
    }

    // Style and Design stuff
    private void configureFormLayout() {
        // Styles
        createdTimeStamp.getStyle().set("flex", "1");
        ticketCreator.getStyle().set("flex", "1");
        creatorMail.getStyle().set("flex", "1");

        // Layout reform
        setColspan(buttonSection, 2);
        setColspan(description, 2);
        setColspan(commentSection, 2);
    }

    // This sets the currently selected ticket to be used in all functions and updates the comments
    public void setTicket(Ticket ticket){
        this.ticket = ticket;
        binder.readBean(ticket);
        validateAndUpdate();
    }

    // Creates the comments layout and updates the comments
    private Component createCommentsLayout() {
        input.addSubmitListener(submitEvent -> {
            User user = new User();
            user.setUserName("UserName");
            ticket.getTicketComment().add(new TicketComment(submitEvent.getValue(), user, ticket));
            validateAndUpdate();
        });
        return new VerticalLayout(new H2("Comments"), ticketComments, input);
    }

    // Creates the button layout to save and cancel the form
    private HorizontalLayout createButtonsLayout() {
        reopen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        reopen.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        reopen.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        HorizontalLayout content = new HorizontalLayout(reopen, close);

        content.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return content;
    }

    // this function validates and saves the ticket according to the form (comments are saved when written)
    private void validateAndSave() {
        try {
            binder.writeBean(ticket);
            fireEvent(new SaveEvent(this, ticket));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // updates the comments
    public void validateAndUpdate() {
        if(ticket != null) {
            this.commentList = ticket.getTicketComment();
            List<MessageListItem> realComments = new ArrayList<>();
            if (this.commentList != null) {
                for(TicketComment element : this.commentList){
                    realComments.add(new MessageListItem(element.getCommentText(),
                            Instant.now(),
                            element.getCommentAuthor().getUserName())
                    );
                }
            }
            this.ticketComments.setItems(realComments);
            getUI().ifPresent(ui -> {
                ui.access(() -> {
                    if(ticket.getTicketStatus().getStatusName().endsWith("esolved") ||
                        ticket.getTicketStatus().getStatusName().endsWith("ejected")) {
                        reopen.setVisible(true);
                        input.setVisible(false);
                    } else {
                        reopen.setVisible(false);
                        input.setVisible(true);
                    }
                    if(!ticket.getTicketStatus().getStatusName().equalsIgnoreCase("new")){
                        ticketName.setReadOnly(true);
                        description.setReadOnly(true);
                        createdTimeStamp.setReadOnly(true);
                        ticketCreator.setReadOnly(true);
                        creatorMail.setReadOnly(true);
                        ticketNumber.setReadOnly(true);
                        ticketType.setReadOnly(true);
                        ticketStatus.setReadOnly(true);
                        ticketProject.setReadOnly(true);
                        assignedUsersMulti.setReadOnly(true);
                    } else {
                        ticketName.setReadOnly(false);
                        description.setReadOnly(false);
                        createdTimeStamp.setReadOnly(true);
                        ticketCreator.setReadOnly(true);
                        creatorMail.setReadOnly(true);
                        ticketNumber.setReadOnly(false);
                        ticketType.setReadOnly(false);
                        ticketStatus.setReadOnly(true);
                        ticketProject.setReadOnly(false);
                        assignedUsersMulti.setReadOnly(true);
                    }
                });
            });
        }
    }

    // Parent Class of Save and Cancel Events
    public static abstract class TicketHistoryFormFormEvent extends ComponentEvent<TicketHistoryForm> {
        public Ticket ticket;

        protected TicketHistoryFormFormEvent(TicketHistoryForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }
    }

    // Save Event for saving ticket
    public static class SaveEvent extends TicketHistoryFormFormEvent{
        SaveEvent(TicketHistoryForm source, Ticket ticket){
            super(source, ticket);
        }
    }

    // Close Event for closing form
    public static class CloseEvent extends TicketHistoryFormFormEvent{
        CloseEvent(TicketHistoryForm source){
            super(source, null);
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}
