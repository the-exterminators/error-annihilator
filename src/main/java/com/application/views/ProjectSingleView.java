package com.application.views;


import com.application.components.Header;
import com.application.security.SecurityService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;

@PermitAll
@PageTitle("Project Single View | Error Annihilator")
@Route(value = "project-overview")
public class ProjectSingleView extends VerticalLayout {

    private Grid.Column<AssignedUser> ticketTitleColumn;
    private GridPro<AssignedUser> grid;
    private GridListDataView<AssignedUser> gridListDataView;
    private Grid.Column<AssignedUser> assignedUserColumn;
    private Grid.Column<AssignedUser> projectColumn;
    private Grid.Column<AssignedUser> statusColumn;
    private Grid.Column<AssignedUser> dateColumn;

    private final SecurityService securityService;

    public ProjectSingleView(AuthenticationContext authenticationContext) {
        this.securityService = new SecurityService(authenticationContext);
        addClassName("project-overview");

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
        H1 title = new H1("Project Title");
        content.add(title);

        // Add grid and form to content
        createGrid();
        content.add(grid);

        return content;
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");
        grid.setWidth("90vw");

        List<AssignedUser> assignedUser = getAssignedUser();
        gridListDataView = grid.setItems(assignedUser);
    }

    private void addColumnsToGrid() {
        createAssignedUserColumn();
        createTicketTitleColumn();
        createProjectColumn();
        createStatusColumn();
        createDateColumn();
    }

    private void createAssignedUserColumn() {
        assignedUserColumn = grid.addColumn(new ComponentRenderer<>(assignedUser-> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Image img = new Image(assignedUser.getImg(), "");
            Span span = new Span();
            span.setClassName("name");
            span.setText(assignedUser.getAssignedUser());
            hl.add(img, span);
            return hl;
        })).setComparator(assignedUser -> assignedUser.getAssignedUser()).setHeader("Assigned User");
    }
    private void createProjectColumn() {
        projectColumn = grid
                .addEditColumn(AssignedUser::getProject,
                        new NumberRenderer<>(AssignedUser::getProject, NumberFormat.getNumberInstance(Locale.US)))
                .text((item, newValue) -> item.setProject(Double.parseDouble(newValue)))
                .setComparator(AssignedUser::getProject)
                .setHeader("Projects");
    }




    private void createStatusColumn() {
        statusColumn = grid.addEditColumn(AssignedUser::getAssignedUser, new ComponentRenderer<>(AssignedUser -> {
                    Span span = new Span();
                    span.setText(AssignedUser.getStatus());
                    span.getElement().setAttribute("theme", "badge " + AssignedUser.getStatus().toLowerCase());
                    return span;
                })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("solved", "in progress", "error", "opened"))
                .setComparator(assignedUser -> assignedUser.getStatus()).setHeader("Status");
    }

    private void createTicketTitleColumn() {
        ticketTitleColumn = grid.addColumn(AssignedUser::getTicketTitle)
                .setHeader("Ticket Title")
                .setKey("Ticket Title")
                .setComparator(Comparator.comparing(AssignedUser::getTicketTitle));
    }
    private void createDateColumn() {
        dateColumn = grid
                .addColumn(new LocalDateRenderer<>(assignedUser-> LocalDate.parse(assignedUser.getDate()),
                        () -> DateTimeFormatter.ofPattern("M/d/yyyy")))
                .setComparator(assignedUser -> assignedUser.getDate()).setHeader("Date").setWidth("180px").setFlexGrow(0);
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField assignedUserFilter = new TextField();
        assignedUserFilter.setPlaceholder("Filter");
        assignedUserFilter.setClearButtonVisible(true);
        assignedUserFilter.setWidth("100%");
        assignedUserFilter.setValueChangeMode(ValueChangeMode.EAGER);
        assignedUserFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(assignedUser -> StringUtils.containsIgnoreCase(assignedUser.getAssignedUser(), assignedUserFilter.getValue())));
        filterRow.getCell(assignedUserColumn).setComponent(assignedUserFilter);

        TextField projectFilter = new TextField();
        projectFilter.setPlaceholder("Filter");
        projectFilter.setClearButtonVisible(true);
        projectFilter.setWidth("100%");
        projectFilter.setValueChangeMode(ValueChangeMode.EAGER);
        projectFilter.addValueChangeListener(event -> gridListDataView.addFilter(assignedUser -> StringUtils
                .containsIgnoreCase(Double.toString(assignedUser.getProject()), projectFilter.getValue())));
        filterRow.getCell(projectColumn).setComponent(projectFilter);


        TextField ticketTitleFilter = new TextField();
        ticketTitleFilter.setPlaceholder("Filter");
        ticketTitleFilter.setClearButtonVisible(true);
        ticketTitleFilter.setWidth("100%");
        ticketTitleFilter.setValueChangeMode(ValueChangeMode.EAGER);
        ticketTitleFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(assignedUser -> StringUtils.containsIgnoreCase(assignedUser.getTicketTitle(), ticketTitleFilter.getValue())));
        filterRow.getCell(ticketTitleColumn).setComponent(ticketTitleFilter);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("solved", "in progress", "error", "opened"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(assignedUser -> areStatusesEqual(assignedUser, statusFilter)));
        filterRow.getCell(statusColumn).setComponent(statusFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(assignedUser -> areDatesEqual(assignedUser, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areStatusesEqual(AssignedUser assignedUser, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(assignedUser.getStatus(), statusFilterValue);
        }
        return true;
    }

    private boolean areDatesEqual(AssignedUser assignedUser, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate assignedUserDate = LocalDate.parse(assignedUser.getDate());
            return dateFilterValue.equals(assignedUserDate);
        }
        return true;
    }

    private List<AssignedUser> getAssignedUser() {
        return Arrays.asList(
                createAssignedUser(4957, "https://randomuser.me/api/portraits/women/42.jpg", "Lisa Eppstein", 1,
                        "mein erstes Ticket", "solved", "2019-05-09"),
                createAssignedUser(675, "https://randomuser.me/api/portraits/women/24.jpg", "Stefan Schatzmann", 2,
                        "mein zweites Ticket","in progress", "2019-05-09"),
                createAssignedUser(6816, "https://randomuser.me/api/portraits/men/42.jpg", "Deborah Fleischmann", 5,
                        "mein drittes Ticket","error", "2019-05-07"),
                createAssignedUser(5144, "https://randomuser.me/api/portraits/women/76.jpg", "Jacqueline Kühne", 6,
                        "mein viertes Ticket","solved", "2019-04-25"),
                createAssignedUser(9800, "https://randomuser.me/api/portraits/men/24.jpg", "Heinz Weingard", 1,
                        "mein fünftes Ticket","open", "2019-04-20"),
                createAssignedUser(223, "https://randomuser.me/api/portraits/women/42.jpg", "Valerie Mark", 1,
                        "mein sechstes Ticket","solved", "2019-05-09"),
                createAssignedUser(224, "https://randomuser.me/api/portraits/women/24.jpg", "Andreas Danner", 1,
                        "mein siebtes Ticket","in progress", "2019-05-09"),
                createAssignedUser(228, "https://randomuser.me/api/portraits/men/42.jpg", "Dietlind Grabowski", 2,
                        "mein achtes Ticket","error", "2019-05-07"),
                createAssignedUser(5299, "https://randomuser.me/api/portraits/women/76.jpg", "Claudia Auer", 6,
                        "mein neuntes Ticket","solved", "2019-04-25"),
                createAssignedUser(1920, "https://randomuser.me/api/portraits/men/24.jpg", "Franz Fuchs", 1,
                        "mein zehntes Ticket","in progress", "2019-04-22"),
                createAssignedUser(120, "https://randomuser.me/api/portraits/women/94.jpg", "Steven Davis", 2,
                        "mein elftes Ticket","open", "2019-04-17"),
                createAssignedUser(1029, "https://randomuser.me/api/portraits/men/76.jpg", "Hugh Grant", 4, "Pending",
                        "mein zwölftes Ticket","2019-04-17"),
                createAssignedUser(10029, "https://randomuser.me/api/portraits/men/94.jpg", "Carol Bing", 3,
                        "mein dreizehntes Ticket","open", "2019-02-26"),
                createAssignedUser(12923, "https://randomuser.me/api/portraits/men/16.jpg", "Jennifer Aniston", 1,
                        "mein vierzehntes Ticket","solved", "2019-02-21"));
    }
    private AssignedUser createAssignedUser(int id, String img, String assignedUser, double project, String ticketTitle, String status, String date) {
        AssignedUser a = new AssignedUser();
        a.setId(id);
        a.setImg(img);
        a.setAssignedUser(assignedUser);
        a.setProject(project);
        a.setTicketTitle(ticketTitle);
        a.setStatus(status);
        a.setDate(date);

        return a;
    }
};