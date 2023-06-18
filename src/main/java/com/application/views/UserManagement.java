package com.application.views;

import com.application.components.EditTicketForm;
import com.application.components.EditUserForm;
import com.application.components.Header;
import com.application.data.entity.Ticket;
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
import org.hibernate.annotations.Columns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;


@PermitAll // Declare Roles Manager
@PageTitle("User Management | Error Annihilator")
@Route(value = "user-management")
public class UserManagement extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class, false);
    EditUserForm userForm; // Form/Editor
    H1 title = new H1("User Management");

    private final SecurityService securityService;

    public UserManagement(AuthenticationContext authenticationContext) {
        SecurityService securityService = new SecurityService(authenticationContext);
        this.securityService = securityService;
        addClassName("userManagement-view");

        // This is how to implement the header
        setSizeFull();
        Header header = new Header(securityService);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        // Main Page Title
        content.add(title);

        // Add grid and form to content
        configureGrid();
        configureForm();
        content.add(grid, userForm);

        closeEditor(); // standard closed form

        return content;
    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
        removeClassName("editing");
        grid.getStyle().set("display", "block");
        grid.asSingleSelect().clear(); // deselect ticket in grid
    }

    // Handles selected ticket from grid
    private void editUser(User user) {
        // If a ticket is selected or unselected open/close editor/form and set current ticket
        if(user == null){
            closeEditor();
        } else {
            userForm.setUser(user);
            userForm.setVisible(true);
            addClassName("editing");
            grid.getStyle().set("display", "none");
        }
    }

    private void configureForm() {
        userForm = new EditUserForm(); // Replace with actual lists
        userForm.setSizeFull();
        userForm.addCloseListener(e -> closeEditor()); // add listener to close form
        userForm.addSaveListener(this::saveUser); // add listener to save ticket - doesn't work yet
    }

    // Saves ticket, updates the grid and closes editor/form
    private void saveUser(EditUserForm.SaveEvent event) {
        // service.saveTicket(event.getTicket()); // After DB integration
        updateList();
        closeEditor();
    }

    // update the grid
    private void updateList() {
        // grid.setItems(service.findallTickets(filterText.getValue())); // After DB integration
    }

    private void configureGrid() {
        // Test users
        List<User> testUsers = new LinkedList<>();
        User testUser = new User("Jana", "Burns", "Burnsjana", "bj4780@mci4me.at", "1234", "Manager");
        User testUser2 = new User("Isabelle", "Mariacher", "IsabelleM", "mi4780@mci4me.at", "hallo", "Developer");
        testUsers.add(testUser);
        testUsers.add(testUser2);

        //Columns
        Grid.Column<User> titleColumn = grid.addColumn("userName").setHeader("Username");
        Grid.Column<User> firstNameColumn = grid.addColumn("firstName").setHeader("First Name");
        Grid.Column<User> lastNameColumn = grid.addColumn("lastName").setHeader("Last Name");
        Grid.Column<User> emailColumn = grid.addColumn("email").setHeader("Email");
        Grid.Column<User> userRoleColumn = grid.addColumn("userRole").setHeader("User Role");

        // add Listeners
        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));

        // Set items for grid
        GridListDataView<User> dataView = grid.setItems(testUsers); // replace with dataservice.getUsers()

        // Filter - https://vaadin.com/docs/latest/components/grid
        UserManagement.UserFilter userFilter = new UserManagement.UserFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(titleColumn).setComponent(createFilterHeader("Filter Username", userFilter::setUserName));
        headerRow.getCell(firstNameColumn).setComponent(createFilterHeader("Filter First Name", userFilter::setFirstName));
        headerRow.getCell(lastNameColumn).setComponent(createFilterHeader("Filter Last Name", userFilter::setLastName));
        headerRow.getCell(emailColumn).setComponent(createFilterHeader("Filter Email", userFilter::setEmail));
        headerRow.getCell(userRoleColumn).setComponent(createFilterHeader("Filter User Role", userFilter::setUserRole));

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
    private static class UserFilter {
        private final GridListDataView<User> dataView;
        private String userName;
        private String firstName;
        private String lastName;
        private String email;
        private String userRole;

        public UserFilter(GridListDataView<User> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setUserName(String userName) {
            this.userName = userName;
            this.dataView.refreshAll();
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
            this.dataView.refreshAll();
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
            this.dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.email = email;
            this.dataView.refreshAll();
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
            this.dataView.refreshAll();
        }

        public boolean test(User user) {
            boolean matchesUserName = matches(user.getUserName(), userName);
            boolean matchesFirstName = matches(user.getFirstName(), firstName);
            boolean matchesLastName = matches(user.getLastName(), lastName);
            boolean matchesEmail = matches(user.getEmail(), email);
            boolean matchesUserRole = matches(user.getUserRole(), userRole);

            return matchesUserName && matchesFirstName && matchesLastName && matchesEmail && matchesUserRole;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
