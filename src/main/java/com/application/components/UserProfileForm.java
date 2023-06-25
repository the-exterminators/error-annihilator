package com.application.components;

import com.application.data.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UserProfileForm extends FormLayout {
    Binder<User> binder = new BeanValidationBinder<>(User.class); // to bind to User Entity
    User user; // To replace with current user, that is selected
    TextField userName = new TextField("Username");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    PasswordField dummyPassword = new PasswordField("Password");
    ComboBox<String> userRole = new ComboBox<>("User Role"); // Combobox in future

    // Buttons
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    public UserProfileForm() {
        addClassName("user-form");

        setUserRoleSampleData(userRole);

        configureBind();

        // add to form layout
        add(userName,
                firstName,
                lastName,
                email,
                dummyPassword,
                userRole,
                createButtonsLayout()
        );

    }

    private void configureBind() {
        binder.bind(userName, "userName");
        binder.bind(firstName, "firstName");
        binder.bind(lastName, "lastName");
        binder.bind(email, "email");
        binder.bind(dummyPassword, "dummyPassword");
        binder.bind(userRole, "userRole");
    }

    public void setUser(User user){
        this.user = user;
        binder.readBean(user);
    }

    private void setUserRoleSampleData(ComboBox<String> comboBox){
        comboBox.setItems("Manager", "Project Lead", "Developer", "User");
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        //delete.addClickListener(event -> deleteEvent()); // Change to delete

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
