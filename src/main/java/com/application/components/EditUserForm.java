package com.application.components;

import com.application.data.entity.User;
import com.application.data.service.DatabaseService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class EditUserForm extends FormLayout {
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
    Button close = new Button("Cancel");
    Button delete = new Button("Delete");
    ConfirmDialog dialog = new ConfirmDialog();

    private final DatabaseService databaseService;

    public EditUserForm(DatabaseService databaseService) {
        this.databaseService = databaseService;
        addClassName("user-form");

        setUserRoleSampleData(userRole);

        configureBind();

        // Delete dialog
        dialog.setHeader("Are you sure?");
        dialog.setText("Do you really want to delete this user?");

        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.addConfirmListener(event -> deleteUser());


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
        binder.bind(userRole, "userRole");
    }

    public void setUser(User user){
        this.user = user;
        binder.readBean(user);
    }

    private void setUserRoleSampleData(ComboBox<String> comboBox){
        comboBox.setItems(databaseService.getAllRoles());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(e -> dialog.open());
        close.addClickListener(event -> fireEvent(new EditUserForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    private void deleteUser() {
        databaseService.setUserInactive(Math.toIntExact(user.getUser_id()));
        dialog.close();
        fireEvent(new EditUserForm.SaveEvent(this, user));
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
            fireEvent(new EditUserForm.SaveEvent(this, user));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Parent Class of Save and Cancel Events
    public static abstract class EditUserFormEvent extends ComponentEvent<EditUserForm> {
        public User user;

        protected EditUserFormEvent(EditUserForm source, User user) {
            super(source, false);
            this.user = user;
        }
    }

    // Save Event for saving user
    public static class SaveEvent extends EditUserForm.EditUserFormEvent {
        SaveEvent(EditUserForm source, User user){
            super(source, user);
        }
    }

    // Close Event for closing form
    public static class CloseEvent extends EditUserForm.EditUserFormEvent {
        CloseEvent(EditUserForm source){
            super(source, null);
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<EditUserForm.SaveEvent> listener) {
        return addListener(EditUserForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<EditUserForm.CloseEvent> listener) {
        return addListener(EditUserForm.CloseEvent.class, listener);
    }
}
