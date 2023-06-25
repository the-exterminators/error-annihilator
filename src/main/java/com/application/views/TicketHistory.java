package com.application.views;

import com.application.components.EditTicketForm;
import com.application.components.Header;
import com.application.components.TicketHistoryForm;
import com.application.data.entity.Ticket;
import com.application.data.entity.TicketComment;
import com.application.data.entity.TicketStatus;
import com.application.data.entity.User;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@PermitAll // Declare roles dev or project lead
@PageTitle("Ticket History | Error Annihilator")
@Route(value = "Ticket-History")
public class TicketHistory extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    TicketHistoryForm TH_Form; // Form/Editor

    private final SecurityService securityService;

    // Constructor
    public TicketHistory(AuthenticationContext authenticationContext) {
        SecurityService securityService = new SecurityService(authenticationContext);
        this.securityService = securityService;
        addClassName("TicketHistory-view");

        // This is how to implement the header
        setSizeFull();
        Header header = new Header(authenticationContext);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    // Have all content be gathered in this function
    private VerticalLayout getContent(){
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        // Main Page Title
        H1 title = new H1("Ticket History");
        content.add(title);

        // Add grid and form to content
        configureGrid();
        configureForm();
        content.add(grid, TH_Form);

        closeEditor(); // standard closed form

        return content;
    }

    // FORM =======================================
    // Configure the editor/form
    private void configureForm() {
        TH_Form = new TicketHistoryForm(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()); // Replace with actual lists
        TH_Form.setSizeFull();
        TH_Form.addCloseListener(e -> closeEditor()); // add listener to close form
        TH_Form.addSaveListener(this::saveTicket); // add listener to save ticket - doesn't work yet
    }

    // Handles selected ticket from grid
    private void editTicket(Ticket ticket) {
        // If a ticket is selected or unselected open/close editor/form and set current ticket
        if(ticket == null){
            closeEditor();
        } else {
            TH_Form.setTicket(ticket);
            TH_Form.setVisible(true);
            addClassName("editing");
            grid.getStyle().set("display", "none");
        }
    }

    // Closes the editor/form
    private void closeEditor() {
        TH_Form.setTicket(null);
        TH_Form.setVisible(false);
        removeClassName("editing");
        grid.getStyle().set("display", "block");
        grid.asSingleSelect().clear(); // deselect ticket in grid
    }

    // Saves ticket, updates the grid and closes editor/form
    private void saveTicket(TicketHistoryForm.SaveEvent event) {
        // service.saveTicket(event.getTicket()); // After DB integration
        updateList();
        closeEditor();
    }

    // update the grid
    private void updateList() {
        // grid.setItems(service.findallTickets(filterText.getValue())); // After DB integration
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Test users
        List<Ticket> testTickets = new ArrayList<>();
        List<User> testUsers = new LinkedList<>();
        User testUser = new User("Jana", "Burns", "Burnsjana", "email", "1234", "dev");
        testUsers.add(testUser);

        // Test ticket 1
        Ticket ticketOne = new Ticket("I have a problem with Vaadin", "bug", "this ist a test", new TicketStatus("resolved"), testUser, testUsers);
        List<TicketComment> listOne = new ArrayList<>();
        listOne.add(new TicketComment("Dear Jana", testUser, ticketOne));
        ticketOne.setTicketComment(listOne);
        testTickets.add(ticketOne);

        // Test ticket 2
        Ticket ticketTwo = new Ticket("hello", "error", "Hello, something isn't working", new TicketStatus("in progress"), testUser, testUsers);
        List<TicketComment> listTwo = new ArrayList<>();
        listTwo.add(new TicketComment("hello", testUser, ticketTwo));
        ticketTwo.setTicketComment(listTwo);
        testTickets.add(ticketTwo);

        // Test ticket 3
        Ticket ticketThree = new Ticket("Help", "defect", "I need your help", new TicketStatus("in progress"), testUser, testUsers);
        List<TicketComment> listThree = new ArrayList<>();
        listThree.add(new TicketComment("hello", testUser, ticketTwo));
        ticketTwo.setTicketComment(listThree);
        testTickets.add(ticketThree);

        // Columns
        Grid.Column<Ticket> titleColumn = grid.addColumn("ticketName").setHeader("Title");
        Grid.Column<Ticket> descrColumn = grid.addColumn("description");

        // assigned to
        Grid.Column<Ticket> assignedToColumn = grid.addColumn(ticket -> ticket.getAssignedUsers()).setHeader("assigned to");

        // no need for progress bar for the devs, they only need categories
        Grid.Column<Ticket> statusColum = grid.addColumn(ticket -> ticket.getTicketStatus().getStatusName()).setHeader("Status");

        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> editTicket(e.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> TH_Form.validateAndUpdate());
        grid.asSingleSelect().addValueChangeListener(e -> TH_Form.updateAssignedUsers());

        // Set items for grid
        GridListDataView<Ticket> dataView = grid.setItems(testTickets); // replace with dataservice.getTickets()

        // Filter - https://vaadin.com/docs/latest/components/grid
        TicketFilter ticketFilter = new TicketFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(titleColumn).setComponent(createFilterHeader("Filter Title", ticketFilter::setTitle));
        headerRow.getCell(descrColumn).setComponent(createFilterHeader("Filter Description", ticketFilter::setDescription));
        headerRow.getCell(assignedToColumn).setComponent(createFilterHeader("Filter assigned to", ticketFilter::setAssignedTo));
        headerRow.getCell(statusColum).setComponent(createFilterHeader("Filter Status", ticketFilter::setStatus));

        // Grid Size Settings
        grid.setSizeFull();
        grid.addClassName("assignedTickets-grid");
        grid.setMinWidth("90vw");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    // FILTER ==================================
    // This creates the filter header with the textfield etc
    private static Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder(labelText + " ...");
        textField.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return textField;
    }

    // Class to filter tickets in grid
    private static class TicketFilter {
        private final GridListDataView<Ticket> dataView;
        private String title;
        private String description;
        private String status;

        private String assignedTo;

        public TicketFilter(GridListDataView<Ticket> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setTitle(String title) {
            this.title = title;
            this.dataView.refreshAll();
        }

        public void setDescription(String description) {
            this.description = description;
            this.dataView.refreshAll();
        }

        public void setStatus(String status) {
            this.status = status;
            this.dataView.refreshAll();
        }

        public void setAssignedTo(String assignedTo){
            this.assignedTo = assignedTo;
            this.dataView.refreshAll();
        }

        public boolean test(Ticket ticket) {
            boolean matchesTitle = matches(ticket.getTicketName(), title);
            boolean matchesDescription = matches(ticket.getDescription(), description);
            boolean matchesStatus = matches(ticket.getTicketStatus().getStatusName(), status);
            //boolean matchesAssignedTo= matches(ticket.getAssignedUsers(), assignedTo);

            return matchesDescription && matchesTitle && matchesStatus;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

}
