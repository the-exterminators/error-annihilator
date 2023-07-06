package com.application.components;

import com.application.data.entity.User;
import com.application.security.SecurityService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.security.AuthenticationContext;

public class UserProfileForm extends FormLayout {
    Binder<User> binder = new BeanValidationBinder<>(User.class); // to bind to User Entity
    User user; // To replace with current user, that is selected
    TextField userName = new TextField("Username");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    PasswordField dummyPassword = new PasswordField("Password");
    private final SecurityService securityService;

    // Buttons
    Button save = new Button("Save");
    Button delete = new Button("Delete my Profile");
    public UserProfileForm(AuthenticationContext authenticationContext) {
        this.securityService = new SecurityService(authenticationContext);
        addClassName("user-form");

        configureBind();

        HorizontalLayout buttonSection = createButtonsLayout();
        setColspan(buttonSection, 2);

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
        binder.bind(dummyPassword, "dummyPassword");
    }

    public void setUser(User user){
        this.user = user;
        binder.readBean(user);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(e -> securityService.logout()); // Change to delete

        save.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(save, delete);
    }

    // this function validates and saves the user according to the form (comments are saved when written)
    private void validateAndSave() {
        try {
            binder.writeBean(user);
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
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<UserProfileForm.SaveEvent> listener) {
        return addListener(UserProfileForm.SaveEvent.class, listener);
    }
}
