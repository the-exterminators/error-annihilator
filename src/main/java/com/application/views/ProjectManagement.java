package com.application.views;

import com.application.components.*;
import com.application.data.entity.*;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@PermitAll // Declare roles manager/admin
@PageTitle("Project Management | Error Annihilator")
@Route(value = "project-management")
public class ProjectManagement extends VerticalLayout {
    Grid<TicketProject> grid = new Grid<>(TicketProject.class, false);
    EditProjectForm editProjectForm; // Form/Editor
    NewProjectForm newProjectForm = new NewProjectForm(); // Form/Editor for new users
    Button newProject = new Button("New Project");
    private final SecurityService securityService;

    public ProjectManagement(AuthenticationContext authenticationContext) {
        this.securityService = new SecurityService(authenticationContext);
        addClassName("projectManagement-view");

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
        content.add(new H1("Project Management"));

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
        }
    }

    // Handles new Project
    private void newProjectNew() {
        // If a project is selected or unselected open/close editor/form and set current ticket
        addClassName("editing");
        grid.getStyle().set("display", "block");
        newProjectForm.getStyle().set("display", "block");
        editProjectForm.getStyle().set("display", "none");
    }

    // Saves project, updates the grid and closes editor/form
    private void saveProject(EditProjectForm.SaveEvent event) {
        //service.saveTicket(event.getTicket()); // After DB integration
        updateList();
        closeEditor();
    }

    // update the grid
    private void updateList() {
        // grid.setItems(service.findallTickets(filterText.getValue())); // After DB integration
    }

    // FORM =======================================
    // Configure the editor/form
    private void configureForm() {
        editProjectForm = new EditProjectForm(); // Replace with actual lists
        editProjectForm.setSizeFull();
        editProjectForm.addCloseListener(e -> closeEditor()); // add listener to close form
        editProjectForm.addSaveListener(this::saveProject); // add listener to save ticket - doesn't work yet

        newProjectForm.setSizeFull();
        newProjectForm.addCloseListener(e -> closeEditor()); // add listener to close form
        //newProjectForm.addSaveListener(this::saveProject); // add listener to save ticket - doesn't work yet
    }

    // GRID ====================================
    // Configure the grid
    private void configureGrid() {
        // Test projects
        User user = new User("Jana", "Burns", "Burnsjana", "email", "1234", "dev");
        List<TicketProject> testProjects = new ArrayList<>();
        testProjects.add(new TicketProject("Project 1", "Test Description 1",user));
        testProjects.add(new TicketProject("Project 2", "Test Description 2", user));
        testProjects.add(new TicketProject("Project 3", "Test Description 3", user));

        // Columns
        Grid.Column<TicketProject> titleColumn = grid.addColumn("projectName").setHeader("Title");
        Grid.Column<TicketProject> descriptionColumn = grid.addColumn("projectDescription").setHeader("Description");
        Grid.Column<TicketProject> leaderColumn = grid.addColumn("projectLead").setHeader("Project Lead");

        // Add listeners
        grid.asSingleSelect().addValueChangeListener(e -> editProject(e.getValue()));

        // Set items for grid
        GridListDataView<TicketProject> dataView = grid.setItems(testProjects); // replace with dataservice.getTickets()

        // Filter - https://vaadin.com/docs/latest/components/grid
        ProjectFilter projectFilter = new ProjectFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(titleColumn).setComponent(createFilterHeader("Filter Title", projectFilter::setTitle));
        headerRow.getCell(descriptionColumn).setComponent(createFilterHeader("Filter Description", projectFilter::setDescription));
        headerRow.getCell(leaderColumn).setComponent(createFilterHeader("Filter Project Leader", projectFilter::setLead));

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
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
