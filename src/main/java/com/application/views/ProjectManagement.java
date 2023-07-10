package com.application.views;

import com.application.components.*;
import com.application.data.entity.*;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.function.Consumer;

@PermitAll
@PageTitle("Project Management | Error Annihilator")
@Route(value = "project-management")
public class ProjectManagement extends VerticalLayout {
    Grid<TicketProject> grid = new Grid<>(TicketProject.class, false);
    EditProjectForm editProjectForm; // Form/Editor
    NewProjectForm newProjectForm; // Form/Editor for new users
    Button newProject = new Button("New Project");
    private final SecurityService securityService;
    private final DatabaseService databaseService;
    H1 title = new H1("Project Management");

    public ProjectManagement(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        addClassName("projectManagement-view");
        newProjectForm = new NewProjectForm(databaseService);

        // This is how to implement the header
        setSizeFull();
        Header header = new Header(authenticationContext);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        HorizontalLayout horizontalContent = new HorizontalLayout();
        horizontalContent.addClassNames("content");
        horizontalContent.setSizeFull();
        horizontalContent.setWidth("90vw");

        // Main Page Title
        content.add(title);

        // Create Project button
        newProject.addClickListener(e -> newProjectNew());
        newProject.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        content.add(newProject);

        // Add grid and form to content
        configureGrid();
        configureForm();

        horizontalContent.add(editProjectForm, grid, newProjectForm);
        content.add(horizontalContent);

        closeEditor(); // standard closed form

        return content;
    }

    // Closes the editor/form
    private void closeEditor() {
        editProjectForm.setProject(null);
        removeClassName("editing");
        grid.getStyle().set("display", "block");
        newProject.getStyle().set("display", "block");
        newProjectForm.getStyle().set("display", "none");
        editProjectForm.getStyle().set("display", "none");
        grid.asSingleSelect().clear(); // deselect ticket in grid
        getUI().ifPresent(ui -> ui.access(() -> title.setText("Project Management")));
    }

    // Handles selected ticket from grid
    private void editProject(TicketProject ticketProject) {
        // If a ticket is selected or unselected open/close editor/form and set current ticket
        if(ticketProject == null){
            closeEditor();
        } else {
            editProjectForm.setProject(ticketProject);
            editProjectForm.getStyle().set("display", "block");
            newProjectForm.getStyle().set("display", "none");
            newProject.getStyle().set("display", "none");
            addClassName("editing");
            grid.getStyle().set("display", "none");
            getUI().ifPresent(ui -> ui.access(() -> title.setText("Edit Project")));
        }
    }

    // Handles new Project
    private void newProjectNew() {
        // If a project is selected or unselected open/close editor/form and set current ticket
        addClassName("editing");
        grid.getStyle().set("display", "block");
        newProjectForm.getStyle().set("display", "block");
        editProjectForm.getStyle().set("display", "none");
        getUI().ifPresent(ui -> ui.access(() -> title.setText("Project Management")));
    }

    // Saves project, updates the grid and closes editor/form
    private void saveProject(EditProjectForm.SaveEvent event) {
        //service.saveTicket(event.getTicket()); // After DB integration
        updateList();
        closeEditor();
    }

    // update the grid
    private void updateList() {
         grid.setItems(databaseService.getAllProjectItems2()); // After DB integration
    }

    // FORM =======================================
    // Configure the editor/form
    private void configureForm() {
        editProjectForm = new EditProjectForm(databaseService); // Replace with actual lists
        editProjectForm.setSizeFull();
        editProjectForm.addCloseListener(e -> closeEditor()); // add listener to close form
        editProjectForm.addSaveListener(this::saveProject); // add listener to save ticket - doesn't work yet

        newProjectForm.setSizeFull();
        newProjectForm.addCloseListener(e -> closeEditor()); // add listener to close form
        newProjectForm.addSaveListener(this::saveProject); // add listener to save ticket - doesn't work yet
    }

    private void saveProject(NewProjectForm.SaveEvent saveEvent) {
        updateList();
        closeEditor();
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Columns
        Grid.Column<TicketProject> titleColumn = grid.addColumn("projectName").setHeader("Title");
        Grid.Column<TicketProject> descriptionColumn = grid.addColumn("projectDescription").setHeader("Description");
        Grid.Column<TicketProject> leaderColumn = grid.addColumn("projectLead").setHeader("Project Lead");

        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> editProject(e.getValue()));

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
        grid.getStyle().set("display", "block");
        newProjectForm.getStyle().set("display", "none");
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
            boolean matchesLead = matches(ticketProject.getProjectLead().toString(), lead);

            return matchesTitle && matchesDescr && matchesLead;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().replace(" ", "").contains(searchTerm.toLowerCase().replace(" ", ""));
        }
    }
}