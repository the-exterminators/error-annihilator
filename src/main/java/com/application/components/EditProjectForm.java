package com.application.components;

import com.application.data.entity.TicketProject;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;

@PermitAll
public class EditProjectForm extends FormLayout {
    Binder<TicketProject> binder = new BeanValidationBinder<>(TicketProject.class); // to bind to Ticket Entity
    TicketProject ticketProject; // To replace with current ticket, that is selected

    // Ticket entity fields
    TextField projectName = new TextField("Title");
    TextField projectDescription = new TextField("Description");
    ComboBox<String> projectLead = new ComboBox<>("Project Lead");

    // Buttons
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Button delete = new Button("Delete");
    HorizontalLayout buttonSection;

    // Constructor
    public EditProjectForm() {
        addClassName("project-form");

        binder.bind(projectName, "projectName");
        binder.bind(projectDescription, "projectDescription");
        binder.bind(projectLead, "projectLead.userName");

        setProjectLeadSampleData(projectLead);

        buttonSection = createButtonsLayout();

        setColspan(buttonSection, 2);

        // add to form layout
        add(projectName,
            projectDescription,
            projectLead,
            buttonSection
        );
    }

    // This sets the currently selected ticket to be used in all functions and updates the comments
    public void setProject(TicketProject ticketProject){
        this.ticketProject = ticketProject;
        binder.readBean(ticketProject);
    }

    // Creates the button layout to save and cancel and delete a project
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new EditProjectForm.CloseEvent(this)));
        delete.addClickListener(event -> fireEvent(new EditProjectForm.CloseEvent(this))); // Change to delete

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    // this function validates and saves the ticket according to the form (comments are saved when written)
    private void validateAndSave() {
        try {
            binder.writeBean(ticketProject);
            fireEvent(new SaveEvent(this, ticketProject));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Parent Class of Save and Cancel Events
    public static abstract class EditProjectFormEvent extends ComponentEvent<EditProjectForm> {
        public TicketProject ticketProject;

        protected EditProjectFormEvent(EditProjectForm source, TicketProject ticketProject) {
            super(source, false);
            this.ticketProject = ticketProject;
        }
    }

    private void setProjectLeadSampleData(ComboBox<String> comboBox){
        comboBox.setItems("burnsjana", "stefanelabdellaoui", "isabellemariacher", "danielkihn", "manuelaudino");
    }

    // Save Event for saving ticket
    public static class SaveEvent extends EditProjectForm.EditProjectFormEvent {
        SaveEvent(EditProjectForm source, TicketProject ticketProject){
            super(source, ticketProject);
        }
    }

    // Close Event for closing form
    public static class CloseEvent extends EditProjectForm.EditProjectFormEvent {
        CloseEvent(EditProjectForm source){
            super(source, null);
        }
    }

    // Listeners
    public Registration addSaveListener(ComponentEventListener<EditProjectForm.SaveEvent> listener) {
        return addListener(EditProjectForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<EditProjectForm.CloseEvent> listener) {
        return addListener(EditProjectForm.CloseEvent.class, listener);
    }
}
