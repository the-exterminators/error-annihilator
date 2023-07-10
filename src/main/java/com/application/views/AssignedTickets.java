package com.application.views;

import com.application.components.EditTicketForm;
import com.application.components.Header;
import com.application.data.entity.*;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@PermitAll // Declare roles dev or project lead
@PageTitle("Assigned Tickets | Error Annihilator")
@Route(value = "assigned-tickets")
public class AssignedTickets extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    EditTicketForm ticketForm; // Form/Editor
    String currentPrincipalName ="";
    H1 title = new H1("My Assigned Tickets");
    GridListDataView<Ticket> dataView;

    private final SecurityService securityService;
    private final DatabaseService databaseService;

    // Constructor
    public AssignedTickets(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        addClassName("assignedTickets-view");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            currentPrincipalName = authentication.getName();
        }

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
        content.add(title);

        // Add grid and form to content
        configureGrid();
        configureForm();
        content.add(grid, ticketForm);

        closeEditor(); // standard closed form

        return content;
    }

    // FORM =======================================
    // Configure the editor/form
    private void configureForm() {
        ticketForm = new EditTicketForm(databaseService); // Replace with actual lists
        ticketForm.setSizeFull();
        ticketForm.addCloseListener(e -> closeEditor()); // add listener to close form
        ticketForm.addSaveListener(this::saveTicket); // add listener to save ticket - doesn't work yet
    }

    // Handles selected ticket from grid
    private void editTicket(Ticket ticket) {
        // If a ticket is selected or unselected open/close editor/form and set current ticket
        if(ticket == null){
            closeEditor();
        } else {
            ticketForm.setTicket(ticket);
            ticketForm.setVisible(true);
            addClassName("editing");
            grid.getStyle().set("display", "none");
            getUI().ifPresent(ui -> ui.access(() -> title.setText("Edit Ticket")));
        }
    }

    // Closes the editor/form
    private void closeEditor() {
        ticketForm.setTicket(null);
        ticketForm.setVisible(false);
        removeClassName("editing");
        grid.getStyle().set("display", "block");
        grid.asSingleSelect().clear(); // deselect ticket in grid
        getUI().ifPresent(ui -> ui.access(() -> title.setText("My Assigned Tickets")));
    }

    // Saves ticket, updates the grid and closes editor/form
    private void saveTicket(EditTicketForm.SaveEvent event) {
        //service.saveTicket(event.getTicket()); // After DB integration
        updateList();
        closeEditor();
    }

    // update the grid
    private void updateList() {
        Integer user_id = Math.toIntExact(databaseService.getUserByUsername(currentPrincipalName).getUser_id());
        dataView = grid.setItems(databaseService.getAssignedTickets(user_id));
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Columns
        Grid.Column<Ticket> numberColumn = grid.addColumn("ticketNumber").setHeader("Number").setWidth("0.5em");
        Grid.Column<Ticket> createdColumn = grid.addColumn(new LocalDateRenderer<>(ticket -> ticket.getCreatedTimeStamp().toLocalDateTime().toLocalDate())).setHeader("Created");
        createdColumn.setSortable(true);
        Grid.Column<Ticket> titleColumn = grid.addColumn("ticketName").setHeader("Title");
        Grid.Column<Ticket> typeColumn = grid.addColumn("ticketType").setHeader("Type");
        Grid.Column<Ticket> statusColumn = grid.addColumn(new ComponentRenderer<>(ticket -> {
            Span span = new Span();
            String text = ticket.getTicketStatus().getStatusName();
            span.setText(text);
            span.getElement().setAttribute("theme", "badge " + text.toLowerCase());
            switch (text.toLowerCase()) {
                case "new" -> span.getElement().getThemeList().add("badge");
                case "in progress" -> span.getElement().getThemeList().add("badge success");
                case "waiting for approval" -> span.getElement().getThemeList().add("badge contrast");
                case "resolved" -> span.getElement().getThemeList().add("badge success primary");
                case "reopened" -> span.getElement().getThemeList().add("badge primary");
                case "rejected" -> span.getElement().getThemeList().add("badge error");
            }
            return span;
        })).setHeader("Status");
        Grid.Column<Ticket> projectColumn = grid.addColumn(ticket -> ticket.getTicketProject().getProjectName()).setHeader("Project");


        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> editTicket(e.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> ticketForm.validateAndUpdate());
        grid.asSingleSelect().addValueChangeListener(e -> ticketForm.updateAssignedUsers());

        // Set items for grid
        //GridListDataView<Ticket> dataView = grid.setItems(testTickets); // replace with dataservice.getTickets()
        Integer user_id = Math.toIntExact(databaseService.getUserByUsername(currentPrincipalName).getUser_id());
        dataView = grid.setItems(databaseService.getAssignedTickets(user_id));

        // Filter - https://vaadin.com/docs/latest/components/grid
        TicketFilter ticketFilter = new TicketFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(numberColumn).setComponent(createFilterHeader(ticketFilter::setNumber));
        headerRow.getCell(titleColumn).setComponent(createFilterHeader(ticketFilter::setTitle));
        editComboFilter(headerRow, typeColumn, databaseService.getAllTicketTypes(), ticketFilter::setType);
        editComboFilter(headerRow, projectColumn, databaseService.getAllProjectItems(), ticketFilter::setProject);
        editComboFilter(headerRow, statusColumn, databaseService.getAllTicketStatus(), ticketFilter::setStatus);
        createDateHeader(headerRow, createdColumn, dataView);

        // Grid Size Settings
        grid.setSizeFull();
        grid.addClassName("assignedTickets-grid");
        grid.setWidth("90vw");
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    // FILTER ==================================
    // This creates the filter header with the textfield etc
    private static Component createFilterHeader(Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder("Filter");
        textField.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return textField;
    }

    private static Component createDateHeader(HeaderRow headerRow, Grid.Column<Ticket> column, GridListDataView<Ticket> dataView) {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.addClassName("filter-header-item");
        dateFilter.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        dateFilter.getStyle().set("max-width", "100%");
        dateFilter.setClearButtonVisible(true);
        dateFilter.addValueChangeListener(
                event -> dataView.addFilter(ticket -> areDatesEqual(ticket, dateFilter)));
        headerRow.getCell(column).setComponent(dateFilter);
        return dateFilter;
    }

    private static boolean areDatesEqual(Ticket ticket, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate createdDate = ticket.getCreatedTimeStamp().toLocalDateTime().toLocalDate();
            return dateFilterValue.equals(createdDate);
        }
        return true;
    }

    private ComboBox editComboFilter(HeaderRow headerRow, Grid.Column<Ticket> gridColumn, List items, Consumer<String> consumer) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addClassName("filter-header-item");
        comboBox.setPlaceholder("Filter");
        comboBox.setClearButtonVisible(true);
        comboBox.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        comboBox.setWidth("100%");
        comboBox.setItems(items);

        comboBox.addValueChangeListener(e -> consumer.accept(e.getValue()));
        headerRow.getCell(gridColumn).setComponent(comboBox);

        return comboBox;
    }

    // Class to filter tickets in grid
    private static class TicketFilter {
        private final GridListDataView<Ticket> dataView;
        private String title;
        private String status;
        private String number;
        private String type;
        private String project;

        public TicketFilter(GridListDataView<Ticket> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setNumber(String number) {
            this.number = number;
            this.dataView.refreshAll();
        }

        public void setTitle(String title) {
            this.title = title;
            this.dataView.refreshAll();
        }

        public void setType(String type) {
            this.type = type;
            this.dataView.refreshAll();
        }

        public void setStatus(String status) {
            this.status = status;
            this.dataView.refreshAll();
        }

        public void setProject(String project){
            this.project = project;
            this.dataView.refreshAll();
        }

        public boolean test(Ticket ticket) {
            boolean matchesNumber = matches(ticket.getTicketNumber().toString(), number);
            boolean matchesTitle = matches(ticket.getTicketName(), title);
            boolean matchesType = matches(ticket.getTicketType(), type);
            boolean matchesStatus = matches(ticket.getTicketStatus().getStatusName(), status);
            boolean matchesProject = matches(ticket.getTicketProject().getProjectName(), project);

            return matchesTitle && matchesStatus && matchesNumber && matchesType && matchesProject;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}


