package com.application.components;

import com.application.data.entity.TicketStatus;
import com.application.data.entity.User;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class EditTicketForm extends FormLayout {
    TextField title = new TextField("Title");
    TextArea description = new TextArea("Description");
    TextField type = new TextField("Type");
    TextField time = new TextField("Time");
    ComboBox<TicketStatus> status = new ComboBox<>("Status");
    ComboBox<User> creator = new ComboBox<>("Creator");
    MultiSelectComboBox<User> assginee = new MultiSelectComboBox<>("Assignees");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Button comments = new Button("Comments");

    public EditTicketForm(List<TicketStatus> statuses, List<User> assignees) {
        addClassName("contact-form");

        status.setItems(statuses);
        status.setItemLabelGenerator(TicketStatus::getStatusName);
        assginee.setItems(assignees);
        assginee.setItemLabelGenerator(User::getUserName);

        title.setReadOnly(true);
        description.setReadOnly(true);
        time.setReadOnly(true);
        creator.setReadOnly(true);

        add(title,
            description,
            time,
            creator,
            type,
            status,
            assginee,
            createButtonsLayout()
        );
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        comments.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, comments, close);
    }
}
