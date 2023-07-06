package com.application.views;

import com.application.components.Header;
import com.application.data.entity.TicketProject;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.util.List;
import java.util.function.Consumer;

@PermitAll // Declare roles dev or project lead
@PageTitle("Project Overview | Error Annihilator")
@Route(value = "project-overview")
public class ProjectOverview extends VerticalLayout {
    Grid<TicketProject> grid = new Grid<>(TicketProject.class, false);
    ProjectSingleView singleProject;
    H1 title = new H1("Projects");

    private final SecurityService securityService;
    private final DatabaseService databaseService;

    // Constructor
    public ProjectOverview(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        addClassName("project-overview-view");
        singleProject = new ProjectSingleView(databaseService, authenticationContext);

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
        content.add(singleProject, grid);
        singleProject.getStyle().set("display", "none");

        return content;
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Columns
        Grid.Column<TicketProject> titleColumn = grid.addColumn("projectName").setHeader("Title");
        Grid.Column<TicketProject> descriptionColumn = grid.addColumn("projectDescription").setHeader("Description");
        Grid.Column<TicketProject> leaderColumn = grid.addColumn("projectLead").setHeader("Project Lead");

        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> {
            String customRoute = e.getValue().getProjectName().replace(" ", "-").toLowerCase();
            grid.getUI().flatMap(ui -> ui.navigate(ProjectSingleView.class, customRoute)).ifPresent(editor -> editor.setProject(e.getValue()));
        });

        // Set items for grid
        GridListDataView<TicketProject> dataView = grid.setItems(databaseService.getAllProjectItems2());
        // Filter - https://vaadin.com/docs/latest/components/grid
        ProjectFilter projectFilter = new ProjectFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(titleColumn).setComponent(createFilterHeader(projectFilter::setTitle));
        headerRow.getCell(descriptionColumn).setComponent(createFilterHeader(projectFilter::setDescription));
        editComboFilter(headerRow, leaderColumn, databaseService.getAllUsernames(), projectFilter::setLead);

        // Grid Size Settings
        grid.setSizeFull();
        grid.addClassName("projectManagement-grid");
        grid.setWidth("90vw");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
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

    private ComboBox editComboFilter(HeaderRow headerRow, Grid.Column<TicketProject> gridColumn, List items, Consumer<String> consumer) {
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

    // Class to filter projects in grid
    private static class ProjectFilter {
        private final GridListDataView<TicketProject> dataView;
        private String title;
        private String description;
        private String lead;

        public ProjectFilter(GridListDataView<TicketProject> dataView) {
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

        public void setLead(String lead) {
            this.lead = lead;
            this.dataView.refreshAll();
        }


        public boolean test(TicketProject ticketProject) {
            boolean matchesTitle = matches(ticketProject.getProjectName(), title);
            boolean matchesDescr = matches(ticketProject.getProjectDescription(), description);
            boolean matchesLead = matches(ticketProject.getProjectLead().getUserName(), lead);

            return matchesTitle && matchesDescr && matchesLead;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().replace(" ", "").contains(searchTerm.toLowerCase().replace(" ", ""));
        }
    }

}
