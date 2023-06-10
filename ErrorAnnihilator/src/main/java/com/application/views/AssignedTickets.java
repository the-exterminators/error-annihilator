package com.application.views;

import com.application.components.EditTicketForm;
import com.application.components.Header;
import com.application.data.entity.Ticket;
import com.application.data.entity.TicketStatus;
import com.application.data.entity.User;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@PageTitle("Assigned Tickets | Error Annihilator")
@Route(value = "")
public class AssignedTickets extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    EditTicketForm ticketForm;
    public AssignedTickets() {
        // This is how to implement the header
        setSizeFull();
        Header header = new com.application.components.Header();
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }


    private VerticalLayout getContent(){
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");

        // Main Page Title
        H1 title = new H1("Assigned Tickets");
        content.add(title);

        // Add grid to content
        configureGrid();
        grid.setMinWidth("800px");
        content.setFlexGrow(2, grid);

        // Add form to content
        configureForm();
        content.setFlexGrow(1, ticketForm);

        content.add(new HorizontalLayout(grid, ticketForm));

        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        ticketForm = new EditTicketForm(Collections.emptyList(), Collections.emptyList());
        ticketForm.setWidth("25em");
    }

    private void configureGrid() {
        // Test items
        List<Ticket> testTickets = new ArrayList<>();
        testTickets.add(new Ticket("I need help", "type", "test test test", new TicketStatus("open"), new User("Jana", "Burns", "Burnsjana", "email", "1234", "dev"), null));
        testTickets.add(new Ticket("hello", "type", "hallo hallo", new TicketStatus("in progress"), new User("Jana", "Burns", "Burnsjana", "email", "1234", "dev"), null));

        // Columns
        Grid.Column<Ticket> titleColumn = grid.addColumn("ticketName").setHeader("Title");
        Grid.Column<Ticket> descrColumn = grid.addColumn("description");
        Grid.Column<Ticket> statusColum = grid.addColumn(ticket -> ticket.getTicketStatus().getStatusName()).setHeader("Status"); // Need a progress bar integrate in ticketstatus

        // Set items for grid
        List<Ticket> tickets = Collections.<Ticket>emptyList(); // replace with dataservice.getTickets()
        GridListDataView<Ticket> dataView = grid.setItems(testTickets); // replace with tickets after testing

        // Filter
        // filter by https://vaadin.com/docs/latest/components/grid
        TicketFilter ticketFilter = new TicketFilter(dataView);
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(titleColumn).setComponent(
                createFilterHeader("Filter Title", ticketFilter::setTitle));
        headerRow.getCell(descrColumn).setComponent(
                createFilterHeader("Filter Description", ticketFilter::setDescription));
        headerRow.getCell(statusColum).setComponent(
                createFilterHeader("Filter Status", ticketFilter::setStatus));

        // Grid Size Settings
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    // FILTER ==================================
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

    private static class TicketFilter {
        private final GridListDataView<Ticket> dataView;
        private String title;
        private String description;
        private String status;

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

        public boolean test(Ticket ticket) {
            boolean matchesTitle = matches(ticket.getTicketName(), title);
            boolean matchesDescription = matches(ticket.getDescription(), description);
            boolean matchesStatus = matches(ticket.getTicketStatus().getStatusName(), status);

            return matchesDescription && matchesTitle && matchesStatus;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }


}


