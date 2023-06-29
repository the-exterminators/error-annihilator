package com.application.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;



public class NewProjectForm extends FormLayout {
    TextField projectName = new TextField("Title");
    TextField projectDescription = new TextField("Description");
    ComboBox<String> projectLead = new ComboBox<>("Project Lead");

    // Buttons
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    public NewProjectForm() {
        addClassName("new-project-form");

        setProjectLeadSampleData(projectLead);

        // add to form layout
        add(projectName,
            projectDescription,
            projectLead,
            createButtonsLayout()
        );

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new NewProjectForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, close);
    }

    private void setProjectLeadSampleData(ComboBox<String> comboBox){
        comboBox.setItems("burnsjana", "stefanelabdellaoui", "isabellemariacher", "danielkihn", "manuelaudino");
    }

    // this function validates and saves the user according to the form
    private void validateAndSave() {
        fireEvent(new SaveEvent(this));
    }

    // Parent Class of Save and Cancel Events
    public static abstract class NewProjectFormEvent extends ComponentEvent<NewProjectForm> {

        protected NewProjectFormEvent(NewProjectForm source) {
            super(source, false);
        }
    }

    // Save Event for saving user
    public static class SaveEvent extends NewProjectForm.NewProjectFormEvent {
        SaveEvent(NewProjectForm source){
            super(source);
        }
    }

    // Close Event for closing form
    public static class CloseEvent extends NewProjectForm.NewProjectFormEvent {
        CloseEvent(NewProjectForm source){
            super(source);
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<NewProjectForm.SaveEvent> listener) {
        return addListener(NewProjectForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<NewProjectForm.CloseEvent> listener) {
        return addListener(NewProjectForm.CloseEvent.class, listener);
    }
}
