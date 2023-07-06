package com.application.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;



public class NewUserForm extends FormLayout {
    TextField userName = new TextField("Username");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    PasswordField dummyPassword = new PasswordField("Password");
    ComboBox<String> userRole = new ComboBox<>("User Role"); // Combobox in future

    // Buttons
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    public NewUserForm() {
        addClassName("new-user-form");

        setUserRoleSampleData(userRole);

        // add to form layout
        add(new H2("New User"),
            userName,
            firstName,
            lastName,
            email,
            dummyPassword,
            userRole,
            createButtonsLayout()
        );

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new NewUserForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, close);
    }

    private void setUserRoleSampleData(ComboBox<String> comboBox){
        comboBox.setItems("Manager", "Project Lead", "Developer", "User");
    }

    // this function validates and saves the user according to the form
    private void validateAndSave() {
        fireEvent(new SaveEvent(this));
    }

    // Parent Class of Save and Cancel Events
    public static abstract class NewUserFormEvent extends ComponentEvent<NewUserForm> {

        protected NewUserFormEvent(NewUserForm source) {
            super(source, false);
        }
    }

    // Save Event for saving user
    public static class SaveEvent extends NewUserForm.NewUserFormEvent {
        SaveEvent(NewUserForm source){
            super(source);
        }
    }

    // Close Event for closing form
    public static class CloseEvent extends NewUserForm.NewUserFormEvent {
        CloseEvent(NewUserForm source){
            super(source);
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<NewUserForm.SaveEvent> listener) {
        return addListener(NewUserForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<NewUserForm.CloseEvent> listener) {
        return addListener(NewUserForm.CloseEvent.class, listener);
    }
}
