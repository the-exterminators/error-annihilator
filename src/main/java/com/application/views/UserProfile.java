package com.application.views;

import com.application.components.Header;
import com.application.components.UserProfileForm;
import com.application.data.entity.User;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PermitAll // Declare roles dev or project lead
@PageTitle("My Profile | Error Annihilator")
@Route(value = "my-profile")
public class UserProfile extends VerticalLayout {
    UserProfileForm userForm; // Form/Editor
    String currentPrincipalName ="";
    User currentUser;
    private final SecurityService securityService;
    private final DatabaseService databaseService;

    // Constructor
    public UserProfile(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            currentPrincipalName = authentication.getName();
        }

        currentUser = databaseService.getUserByUsername(currentPrincipalName);

        addClassName("myProfile-view");

        userForm = new UserProfileForm(databaseService, authenticationContext);

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

        // Add grid and form to content
        configureForm();
        content.add(userForm);

        return content;
    }

    private void configureForm() {
        userForm.setUser(currentUser); // change to current user
        userForm.setSizeFull();
        userForm.addSaveListener(this::saveUser); // add listener to save ticket - doesn't work yet
    }

    // Saves user, updates the grid and closes editor/form
    private void saveUser(UserProfileForm.SaveEvent event) {
         //databaseService.manageUpdateUser(event.getUser().); // After DB integration
    }
}
