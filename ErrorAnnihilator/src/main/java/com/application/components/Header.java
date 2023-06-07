package com.application.components;

import com.application.views.AssignedTickets;
import com.application.views.Help;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Header extends AppLayout {
    public Header() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        HorizontalLayout content = new HorizontalLayout();
        content.add(new DrawerToggle());

        content.setWidthFull();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        content.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        Image logo = new Image("images/logo_big_mixed.svg", "Logo");
        logo.setWidth("200px");
        content.add(logo);



        addToNavbar(content);
    }

    private void createDrawer() {
        VerticalLayout content = new VerticalLayout();

        TextField searchbar = new TextField();
        searchbar.setPlaceholder("Ticket Number");
        searchbar.setClearButtonVisible(true);
        searchbar.setPrefixComponent(VaadinIcon.SEARCH.create());
        content.add(searchbar);

        content.add(new RouterLink("Assigned Tickets", AssignedTickets.class)); // Home
        content.add(new RouterLink("Help", Help.class));
        content.add(new Paragraph("Notifications")); // Placeholder

        Button logout = new Button("Log out");// + u, e -> securityService.logout());
        content.add(logout); // Placeholder

        addToDrawer(content);
    }

}
