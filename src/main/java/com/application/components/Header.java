package com.application.components;

import com.application.security.SecurityService;
import com.application.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
public class Header extends AppLayout {
    private final SecurityService securityService;

    TextField ticketSearch = new TextField(); // search field - not functional atm


    // Constructor
    public Header(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }


    // Create the header with toggle and logo
    private void createHeader() {
        HorizontalLayout content = new HorizontalLayout();
        content.add(new DrawerToggle());

        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        content.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        Image logo = new Image("images/logo_big_mixed.svg", "Logo");
        logo.setWidth("200px");
        content.add(logo);

        addToNavbar(content);
    }

    // Create Navigation Drawer content
    private void createDrawer() {
        VerticalLayout content = new VerticalLayout();

        ticketSearch.setPlaceholder("Ticket Number");
        ticketSearch.setClearButtonVisible(true);
        ticketSearch.setWidth("170px");
        Button searchButton = new Button(VaadinIcon.SEARCH.create());
        content.add(new HorizontalLayout(ticketSearch, searchButton));

        content.add(new RouterLink("Create Ticket", CreateTicket.class)); // Home (for now)
        content.add(new RouterLink("Assigned Tickets", AssignedTickets.class));
        content.add(new RouterLink("User Management", UserManagement.class));
        content.add(new RouterLink("Help", Help.class));
        content.add(new RouterLink ("Ticket History", TicketHistory.class ));


        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());
        content.add(logout); // Placeholder

        setDrawerOpened(false);

        addToDrawer(content);
    }

}
