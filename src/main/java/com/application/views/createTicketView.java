package com.application.views;

import com.application.components.Header;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("CreateTicketView | Error Annihilator")
@Route(value = "create-ticket-view")
public class createTicketView extends VerticalLayout {
    private Avatar avatar = new Avatar();
    private NumberField ticketNumber = new NumberField();
    private ComboBox<String> ticketType = new ComboBox<>();
    private ComboBox<String> urgency = new ComboBox<>();
    private TextArea description = new TextArea();

    private TextField ticketName = new TextField();
    private DateTimePicker dateCreated = new DateTimePicker();
    private MenuBar buttons = new MenuBar();

    public createTicketView() {
        Header header = new com.application.components.Header();
        header.setContent(getCreateTicketContent());
        add(header);
    }

    private VerticalLayout getCreateTicketContent() {
        VerticalLayout fullContent = new VerticalLayout();
        fullContent.setHeightFull();
        fullContent.setWidthFull();

        avatar.setName("Isabelle Mariacher");
        fullContent.add(avatar);

        FormLayout formContent = new FormLayout();
        formContent.setHeightFull();
        formContent.setWidthFull();

        ticketNumber.setLabel("Ticket number");
        dateCreated.setLabel("Ticket created");
        ticketName.setLabel("Ticket name");
        ticketType.setLabel("Ticket Type");
        urgency.setLabel("Urgency");
        description.setLabel("Description");

        //set the read only fields
        ticketNumber.setReadOnly(true);
        dateCreated.setReadOnly(true);

        //set sample items
        setUrgencyComboBoxSampleData(urgency);
        setTicketTypeComboBoxSampleData(ticketType);


        //set column spans
        formContent.setColspan(urgency, 1);
        formContent.setColspan(ticketType, 1);
        formContent.setColspan(ticketName, 2);
        formContent.setColspan(description, 2);

        //add components to form layout

        formContent.add(ticketNumber);
        formContent.add(dateCreated);
        formContent.add(ticketName);
        formContent.add(ticketType);
        formContent.add(urgency);
        formContent.add(description);


        //add form to main layout
        fullContent.add(formContent);

        //add buttons to main layout
        setMenuBarSampleData(buttons);
        fullContent.add(buttons);

        return fullContent;
    }

    //set sample data for urgency
    private void setUrgencyComboBoxSampleData(ComboBox<String> comboBox) {
        comboBox.setItems("Low", "Medium", "High");
        comboBox.setValue("Medium");
    }

    private void setTicketTypeComboBoxSampleData(ComboBox<String> comboBox) {
        comboBox.setItems("bugs", "defects", "errors");
        comboBox.setValue("defects");
    }

    //set sample data for the buttons bar
    private void setMenuBarSampleData(MenuBar menuBar) {
        menuBar.addItem("Save");
        menuBar.addItem("Create");
        menuBar.addItem("Delete");
    }
}
