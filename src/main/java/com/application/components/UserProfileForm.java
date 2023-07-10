package com.application.components;

import com.application.data.entity.User;
import com.application.data.service.DatabaseService;
import com.application.security.SecurityService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserProfileForm extends FormLayout {
    Binder<User> binder = new BeanValidationBinder<>(User.class); // to bind to User Entity
    User user; // To replace with current user, that is selected
    TextField userName = new TextField("Username");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    PasswordField dummyPassword = new PasswordField("Change Password");
    private final SecurityService securityService;
    private final DatabaseService databaseService;
    ConfirmDialog dialog = new ConfirmDialog();

    // Buttons
    Button save = new Button("Save");
    Button delete = new Button("Delete my Profile");
    public UserProfileForm(DatabaseService databaseService, AuthenticationContext authenticationContext) {
        this.databaseService = databaseService;
        this.securityService = new SecurityService(authenticationContext);
        addClassName("user-form");

        configureBind();

        HorizontalLayout buttonSection = createButtonsLayout();
        setColspan(buttonSection, 2);

        // Delete dialog
        dialog.setHeader("Are you sure?");
        dialog.setText("Do you really want to delete your user?");

        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.addConfirmListener(event -> deleteUser());


        // add to form layout
        add(userName,
                firstName,
                lastName,
                email,
                dummyPassword,
                buttonSection
        );

    }

    private void configureBind() {
        binder.bind(userName, "userName");
        binder.bind(firstName, "firstName");
        binder.bind(lastName, "lastName");
        binder.bind(email, "email");
    }

    public void setUser(User user){
        this.user = user;
        binder.readBean(user);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(e -> dialog.open()); // Change to delete

        save.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(save, delete);
    }

    private void deleteUser() {
        databaseService.setUserInactive(Math.toIntExact(user.getUser_id()));
        securityService.logout();
    }

    // this function validates and saves the user according to the form (comments are saved when written)
    private void validateAndSave() {
        try {
            binder.writeBean(user);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            databaseService.updateCurrentUserInfo(Math.toIntExact(user.getUser_id()), user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail(), databaseService.getRoleByName(user.getUserRole()));
            databaseService.updateCurrentUserPasswordHash(Math.toIntExact(user.getUser_id()), encoder.encode(dummyPassword.getValue()));
            // Provide feedback after update
            Notification notification = new Notification(
                    "Details updated successfully!",
                    5000,
                    Notification.Position.MIDDLE);
            notification.open();
            dummyPassword.clear();
            fireEvent(new UserProfileForm.SaveEvent(this, user));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Parent Class of Save and Cancel Events
    public static abstract class UserProfileFormEvent extends ComponentEvent<UserProfileForm> {
        public User user;

        protected UserProfileFormEvent(UserProfileForm source, User user) {
            super(source, false);
            this.user = user;
        }
    }

    // Save Event for saving user
    public static class SaveEvent extends UserProfileForm.UserProfileFormEvent {
        SaveEvent(UserProfileForm source, User user){
            super(source, user);
        }
        public User getUser(){
            return user;
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<UserProfileForm.SaveEvent> listener) {
        return addListener(UserProfileForm.SaveEvent.class, listener);
    }
}
