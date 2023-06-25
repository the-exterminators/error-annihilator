package com.application.views;

import com.application.components.EditTicketForm;
import com.application.components.EditUserForm;
import com.application.components.Header;
import com.application.components.UserProfileForm;
import com.application.data.entity.User;
import com.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;

@PermitAll // Declare roles dev or project lead
@PageTitle("My Profile | Error Annihilator")
@Route(value = "my-profile")
public class UserProfile extends VerticalLayout {
    UserProfileForm userForm; // Form/Editor
    String currentPrincipalName ="test";

    Object test;
    private final SecurityService securityService;

    // Constructor
    public UserProfile(AuthenticationContext authenticationContext) {
        this.securityService = new SecurityService(authenticationContext);;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            currentPrincipalName = authentication.getName();
        }

        addClassName("myProfile-view");

        // This is how to implement the header
        setSizeFull();
        Header header = new Header(authenticationContext);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    private VerticalLayout getContent(){
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        // Main Page Title
        H1 title = new H1("Hello "+currentPrincipalName+"!");
        content.add(title);
        content.add(new Paragraph("Here you can edit your details"));

        // Add grid and form to content
        configureForm();
        content.add(userForm);

        return content;
    }

    private void configureForm() {
        userForm = new UserProfileForm(); // Replace with actual lists
        userForm.setUser(new User("Jana", "Burns", "Burnsjana", "bj4780@mci4me.at", "1234", "Manager")); // change to current user
        userForm.setSizeFull();
        userForm.addSaveListener(this::saveUser); // add listener to save ticket - doesn't work yet
    }

    // Saves user, updates the grid and closes editor/form
    private void saveUser(UserProfileForm.SaveEvent event) {
        // service.saveUser(event.getUser()); // After DB integration
    }
}
