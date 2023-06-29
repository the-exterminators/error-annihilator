package com.application.components;

import com.application.security.SecurityService;
import com.application.views.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
public class Header extends AppLayout{
    private final SecurityService securityService;

    NumberField ticketSearch = new NumberField(); // search field - not functional atm
    Button searchButton = new Button(VaadinIcon.SEARCH.create());


    // Constructor
    public Header(AuthenticationContext authenticationContext) {
        this.securityService = new SecurityService(authenticationContext);
        Div searchPrefix = new Div();
        searchPrefix.setText("#");
        ticketSearch.setPrefixComponent(searchPrefix);

        createHeader();
        createDrawer();
    }


    // Create the header with toggle and logo
    private void createHeader() {
        addClassName("navbar-container");

        HorizontalLayout content = new HorizontalLayout();
        content.add(new DrawerToggle());

        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        content.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        Image logo = new Image("images/logo_big_mixed.svg", "Logo");
        logo.setWidth("200px");
        logo.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CreateTicket.class)));
        content.add(new HorizontalLayout(logo));

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());
        HorizontalLayout logoutFlex = new HorizontalLayout(logout);
        logoutFlex.addClassName("logoutFlex");

        content.add(logoutFlex);

        addToNavbar(content);
    }

    // Create Navigation Drawer content
    private void createDrawer() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("drawer");

        ticketSearch.setPlaceholder("Ticket Number");
        ticketSearch.setClearButtonVisible(true);
        ticketSearch.setWidth("170px");
        searchButton.addClickListener(event -> buttonClick());
        searchButton.addClickShortcut(Key.ENTER);

        content.add(new HorizontalLayout(ticketSearch, searchButton));

        addMenuItemToContent(content, VaadinIcon.TICKET, "Create a Ticket", CreateTicket.class);

        VerticalLayout adminSubMenu = new VerticalLayout();
        adminSubMenu.addClassName("management-submenu");
        Icon icon = new Icon(VaadinIcon.BULLETS);
        Paragraph management = new Paragraph(icon, new Paragraph("Management"));
        management.addClickListener(e -> {
            if(adminSubMenu.hasClassName("open")){
                adminSubMenu.removeClassName("open");
            } else {
                adminSubMenu.addClassName("open");
            }
        });
        adminSubMenu.add(management);

        addMenuItemToContent(adminSubMenu, VaadinIcon.CLIPBOARD_TEXT, "Project Management", ProjectManagement.class, true);
        addMenuItemToContent(adminSubMenu, VaadinIcon.CLIPBOARD_USER, "User Management", UserManagement.class, true);
        addMenuItemToContent(adminSubMenu, VaadinIcon.LINE_BAR_CHART, "Dashboard", Dashboard.class, true);

        //admin.add(adminSubMenu);
        content.add(adminSubMenu);

        addMenuItemToContent(content, VaadinIcon.USER, "My Profile", UserProfile.class);
        addMenuItemToContent(content, VaadinIcon.FLAG, "My Assigned Tickets", AssignedTickets.class);
        addMenuItemToContent(content, VaadinIcon.TICKET, "My Submitted Tickets", TicketHistory.class);
        addMenuItemToContent(content, VaadinIcon.QUESTION, "Help", Help.class);

        setDrawerOpened(false);

        addToDrawer(content);
    }

    private MenuItem addMenuItemToContent(VerticalLayout content, VaadinIcon icon, String title, Class component){
        return addMenuItemToContent(content, icon, title, component, false);
    }

    private MenuItem addMenuItemToContent(VerticalLayout content, VaadinIcon icon, String title, Class component, Boolean child){
        MenuItem item;
        MenuBar menuBar = new MenuBar();
        item = createIconItem(menuBar, icon, title, child);
        if(component != null) {
            item.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(component)));
        }
        item = createIconItem(menuBar, icon, title, child);
        if(component != null) {
            item.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(component)));
        }
        content.add(item);
        return item;
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,
                                    String label, boolean isChild) {
        Icon icon = new Icon(iconName);

        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }

        MenuItem item = menu.addItem(icon, e -> {
        });

        if (label != null) {
            item.add(new Text(label));
        }

        return item;
    }

    public void buttonClick() {
        if(ticketSearch.getValue() != null){
            searchButton.getUI().flatMap(ui -> ui.navigate(SingleTicket.class)).ifPresent(editor -> {
                editor.setup((int) Math.round(ticketSearch.getValue()));
                editor.reroute();
            });
        }
    }
}
