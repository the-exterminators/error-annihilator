package com.application.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Assigned Tickets | Error Annihilator")
@Route(value = "")
public class AssignedTickets extends VerticalLayout {
    public AssignedTickets() {
        add(new com.application.components.Header(),
                getContent());
    }

    private VerticalLayout getContent(){
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");
        content.setSizeFull();

        H1 title = new H1("Assigned Tickets");
        content.add(title);

        content.add(new Paragraph("View your open tickets and work on them :)"));

        return content;
    }

}


