package com.application.views;


import com.application.components.EditTicketForm;
import com.application.components.Header;
import com.application.data.entity.*;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.dao.EmptyResultDataAccessException;

@PermitAll
@PageTitle("Project Single View | Error Annihilator")
@Route(value = "project")
@PreserveOnRefresh
public class ProjectSingleView extends VerticalLayout implements HasUrlParameter<String> {
    private final SecurityService securityService;
    private final DatabaseService databaseService;
    GridListDataView<Ticket> dataView;

    Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    EditTicketForm ticketForm; // Form/Editor
    TicketProject ticketProject = new TicketProject();
    Button backToOverview = new Button("Project Overview", new Icon(VaadinIcon.ARROW_LEFT));
    public H1 title = new H1("Project");
    public Paragraph description = new Paragraph("");

    public ProjectSingleView(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        addClassName("project-single");

        backToOverview.setIconAfterText(false);

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

        backToOverview.addClickListener(e -> getUI().flatMap(ui -> {
            ui.navigate(ProjectOverview.class);
            return Optional.empty();
        }));

        HorizontalLayout horizontalContent = new HorizontalLayout(title, backToOverview);
        horizontalContent.addClassName("title-bar");

        // Main Page Title
        content.add(horizontalContent);
        content.add(description);

        // Add grid and form to content
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
            description.setVisible(false);
            backToOverview.setVisible(false);
            addClassName("editing");
            grid.getStyle().set("display", "none");
            getUI().ifPresent(ui -> ui.access(() -> title.setText("Edit Ticket")));
        }
    }

    // Closes the editor/form
    private void closeEditor() {
        ticketForm.setTicket(null);
        ticketForm.setVisible(false);
        description.setVisible(true);
        backToOverview.setVisible(true);
        removeClassName("editing");
        grid.getStyle().set("display", "block");
        grid.asSingleSelect().clear(); // deselect ticket in grid
        getUI().ifPresent(ui -> ui.access(() -> title.setText("Project")));
    }

    // Saves ticket, updates the grid and closes editor/form
    private void saveTicket(EditTicketForm.SaveEvent event) {
        updateList();
        closeEditor();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            System.out.println("1");
            ticketProject = databaseService.getProjectEntity(databaseService.getProjectId(parameter));
            System.out.println("2");
            getUI().flatMap(ui -> {
                System.out.println("Setting title");
                ui.access(() -> title.setText(ticketProject.getProjectName()));
                ui.access(() -> description.setText(ticketProject.getProjectDescription()));
                return Optional.empty();
            });
            System.out.println("3");
            configureGrid();
            System.out.println("4");
        }
        catch (EmptyResultDataAccessException e){}
    }

    // update the grid
    private void updateList() {
        dataView = grid.setItems(databaseService.getAllTicketsFromProjectEntityList(ticketProject.getProjectId()));
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Columns
        Grid.Column<Ticket> numberColumn = grid.addColumn("ticketNumber").setHeader("Number").setWidth("3em").setSortable(false);
        Grid.Column<Ticket> createdColumn = grid.addColumn(new LocalDateRenderer<>(ticket -> ticket.getCreatedTimeStamp().toLocalDateTime().toLocalDate()))
                .setHeader("Created")
                .setComparator(Ticket::getCreatedTimeStamp);
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
        Grid.Column<Ticket> assignedColumn = grid.addColumn(new ComponentRenderer<>(ticket -> {
            AvatarGroup avatarGroup = new AvatarGroup();
            for(User user : ticket.getAssignedUsers()){
                String name = user.getFirstName() + " " + user.getLastName();
                AvatarGroup.AvatarGroupItem item = new AvatarGroup.AvatarGroupItem(name);
                avatarGroup.add(item);
            }
            return avatarGroup;
        })).setHeader("Assigned Users");
        Grid.Column<Ticket> urgencyColumn = grid.addColumn(new ComponentRenderer<>(ticket -> {
            Span span = new Span();
            String text = ticket.getUrgency();
            span.setText(text);
            span.getElement().getClassList().add("urgency");
            switch (text.toLowerCase()) {
                case "low" -> span.getElement().getClassList().add("low");
                case "medium" -> span.getElement().getClassList().add("medium");
                case "high" -> span.getElement().getClassList().add("high");
                case "higher" -> span.getElement().getClassList().add("higher");
                case "highest" -> span.getElement().getClassList().add("highest");
                case "critical" -> span.getElement().getClassList().add("critical");
                case "cosmetic" -> span.getElement().getClassList().add("cosmetic");
            }
            return span;
        })).setHeader("Urgency");

        GridSortOrder<Ticket> order = new GridSortOrder<>(createdColumn, SortDirection.DESCENDING);
        grid.sort(Arrays.asList(order));

        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> editTicket(e.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> ticketForm.validateAndUpdate());
        grid.asSingleSelect().addValueChangeListener(e -> ticketForm.updateAssignedUsers());

        // Set items for grid
        updateList();

        // Filter - https://vaadin.com/docs/latest/components/grid
        TicketFilter ticketFilter = new TicketFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(numberColumn).setComponent(createFilterHeader(ticketFilter::setNumber));
        headerRow.getCell(titleColumn).setComponent(createFilterHeader(ticketFilter::setTitle));
        editComboFilter(headerRow, assignedColumn, databaseService.getAllUsernames(), ticketFilter::setAssignedUsers);
        editComboFilter(headerRow, typeColumn,  databaseService.getAllTicketTypes(), ticketFilter::setType);
        editComboFilter(headerRow, statusColumn, databaseService.getAllTicketStatus(), ticketFilter::setStatus);
        editComboFilter(headerRow, urgencyColumn, databaseService.getAllUrgencyItems(), ticketFilter::setUrgency);
        headerRow.getCell(createdColumn).setComponent(createDateHeader(ticketFilter::setCreated));

        // Grid Size Settings
        grid.setSizeFull();
        grid.addClassName("assignedTickets-grid");
        grid.setWidth("90vw");
        closeEditor();
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

    private static Component createDateHeader(Consumer<LocalDate> filterChangeConsumer) {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.addClassName("filter-header-item");
        dateFilter.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        dateFilter.getStyle().set("max-width", "100%");
        dateFilter.setClearButtonVisible(true);
        dateFilter.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return dateFilter;
    }

    private static boolean areDatesEqual(Ticket ticket, LocalDate dateFilter) {
        LocalDate dateFilterValue = dateFilter;
        if (dateFilterValue != null) {
            LocalDate createdDate = ticket.getCreatedTimeStamp().toLocalDateTime().toLocalDate();
            return dateFilterValue.equals(createdDate);
        }
        return true;
    }

    private void editComboFilter(HeaderRow headerRow, Grid.Column<Ticket> gridColumn, List items, Consumer<String> consumer) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addClassName("filter-header-item");
        comboBox.setPlaceholder("Filter");
        comboBox.setClearButtonVisible(true);
        comboBox.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        comboBox.setWidth("100%");
        comboBox.setItems(items);

        comboBox.addValueChangeListener(e -> consumer.accept(e.getValue()));
        headerRow.getCell(gridColumn).setComponent(comboBox);

    }



    // Class to filter tickets in grid
    private static class TicketFilter {
        private final GridListDataView<Ticket> dataView;
        private String title;
        private String status;
        private String number;
        private String type;
        private String assignedUsers;
        private String urgency;
        private LocalDate created;

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

        public void setAssignedUsers(String assignedUsers){
            this.assignedUsers = assignedUsers;
            this.dataView.refreshAll();
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
            this.dataView.refreshAll();;
        }

        public void setCreated(LocalDate created){
            this.created = created;
            this.dataView.refreshAll();
        }

        public boolean test(Ticket ticket) {
            boolean matchesNumber = matches(ticket.getTicketNumber(), number);
            boolean matchesTitle = matches(ticket.getTicketName(), title);
            boolean matchesType = matches(ticket.getTicketType(), type);
            boolean matchesStatus = matches(ticket.getTicketStatus().getStatusName(), status);
            String userSpan = "";
            for(User user : ticket.getAssignedUsers()){
                userSpan.concat(user.toString() + " ");
            }
            boolean matchesAssigned = matches(userSpan, assignedUsers);
            boolean matchesCreated = areDatesEqual(ticket, created);
            boolean matchesUrgency = matches(ticket.getUrgency(), urgency);

            return matchesTitle && matchesStatus && matchesNumber && matchesType && matchesAssigned && matchesCreated && matchesUrgency;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

}