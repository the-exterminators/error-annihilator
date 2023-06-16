package com.application.views.createTicketView;

import com.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;

@AnonymousAllowed
@PageTitle("CreateTicketView")
@Route(value = "Create-Ticket-View", layout = MainLayout.class)

public class CreateTicketViewView extends Composite<VerticalLayout> {

    private HorizontalLayout layoutRow = new HorizontalLayout();

    private VerticalLayout layoutColumn2 = new VerticalLayout();

    private Avatar avatar = new Avatar();

    private NumberField numberField = new NumberField();

    private ComboBox comboBox = new ComboBox();

    private TextArea textArea = new TextArea();

    private DateTimePicker dateTimePicker = new DateTimePicker();

    private MenuBar menuBar = new MenuBar();

    public CreateTicketViewView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidthFull();
        avatar.setName("Isabelle Mariacher");
        numberField.setLabel("Ticket number");
        comboBox.setLabel("Urgency 🐞?");
        setComboBoxSampleData(comboBox);
        textArea.setLabel("Description");
        textArea.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, textArea);
        dateTimePicker.setLabel("Ticket created:");
        setMenuBarSampleData(menuBar);
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        layoutColumn2.add(avatar);
        layoutColumn2.add(numberField);
        layoutColumn2.add(comboBox);
        layoutColumn2.add(textArea);
        layoutColumn2.add(dateTimePicker);
        layoutColumn2.add(menuBar);
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        comboBox.setItems("Low", "Medium", "High");
        comboBox.setValue("Medium");
    }

    private void setMenuBarSampleData(MenuBar menuBar) {
        menuBar.addItem("Save");
        menuBar.addItem("Create");
        menuBar.addItem("Delete");

    }
}
