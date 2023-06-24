package com.application.views;

import com.application.components.Header;
import com.application.security.SecurityService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@PermitAll
@PageTitle("Dashboard | Error Annihilator")
@Route(value = "dashboard")
public class Dashboard extends VerticalLayout {
    private final SecurityService securityService;
    //private final Service service;

    public Dashboard(AuthenticationContext authenticationContext/*, Service service*/) {
        this.securityService = new SecurityService(authenticationContext);
        //this.service = service;
        addClassName("dashboard-view");

        // This is how to implement the header
        setSizeFull();
        Header header = new Header(securityService);
        header.setContent(getContent()); // getContent should contain all the pages contents
        add(header); // adds Header with content into the View
    }

    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("content");

        // Main Page Title
        H1 title = new H1("Dashboard");
        content.add(title);

        Chart statusPie = getTicketStatusPieChart();
        Chart typePie = getTicketTypePieChart();
        HorizontalLayout dayColumns = getTicketDayColumns();
        HorizontalLayout turnaroundColumns = getTurnaroundTime();
        HorizontalLayout allTicketsTimeline = getAllTicketsSubmittedTimeline();
        HorizontalLayout allTicketsResolvedTimeline = getAllTicketsResolvedTimeline();


        allTicketsTimeline.setWidth("90vw");
        content.add(allTicketsTimeline);

        HorizontalLayout rowOne = new HorizontalLayout();
        rowOne.add(dayColumns, statusPie);
        rowOne.setFlexGrow(1, dayColumns);
        rowOne.setFlexGrow(1, statusPie);
        rowOne.setWidth("90vw");
        content.add(rowOne);
        content.setFlexGrow(1, rowOne);

        HorizontalLayout rowTwo = new HorizontalLayout();
        rowTwo.add(typePie, turnaroundColumns);
        rowTwo.setFlexGrow(1, turnaroundColumns);
        rowTwo.setFlexGrow(1, typePie);
        rowTwo.setWidth("90vw");
        content.add(rowTwo);
        content.setFlexGrow(1, rowTwo);

        allTicketsResolvedTimeline.setWidth("90vw");
        content.add(allTicketsResolvedTimeline);


        return content;
    }

    private Chart getTicketStatusPieChart() {
        VerticalLayout content = new VerticalLayout();

        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Ticket Status");
        conf.setSubTitle("See how many of the ticket statuses are currently in the system");

        // Count ticket status from DB
        Long unassignedCount = 1l;
        Long openCount = 10l;
        Long progressCount = 20l;
        Long approvalCount = 5l;
        // I dont think we should have the type done in the stats because it will just become more and more over time, never less
        // and then it will overpower the rest

        /* for(Tickets.RangeData data){
                if(data.getStatus().getStatusName().lowercase == "unassigned"){
                    unassignedCount++;
                } else if(...)...

         */

        DataSeries series = new DataSeries("Tickets");
        series.add(new DataSeriesItem("unassigned", unassignedCount));
        series.add(new DataSeriesItem("open", openCount));
        series.add(new DataSeriesItem("in progress", progressCount));
        series.add(new DataSeriesItem("waiting for approval", approvalCount));

        conf.addSeries(series);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        content.add(chart);
        content.setWidth("90vw");
        return chart;
    }

    private Chart getTicketTypePieChart() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Ticket Type");
        conf.setSubTitle("See what type of tickets are currently in the system");

        // Count ticket type from DB
        Long bugs = 15l;
        Long defects = 10l;
        Long errors = 20l;

        /* for(Tickets.RangeData data){
                if(data.getTicketType().lowercase == "bugs"){
                    bugs++;
                } else if(...)...

         */

        DataSeries series = new DataSeries("Tickets");
        series.add(new DataSeriesItem("bugs", bugs));
        series.add(new DataSeriesItem("defects", defects));
        series.add(new DataSeriesItem("errors", errors));
        conf.addSeries(series);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        content.add(chart);
        content.setWidth("90vw");
        return chart;
    }

    // https://demo.vaadin.com/charts/BarChart
    private HorizontalLayout getTicketDayColumns() {
        HorizontalLayout content = new HorizontalLayout();

        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Ticket Income Days");
        conf.setSubTitle("See what days the tickets usually come in");

        XAxis x = new XAxis();
        x.setCategories("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Number of Tickets");
        conf.addyAxis(y);

        /*
        AtomicReference<Long> mondays = new AtomicReference<>(0l);
        AtomicReference<Long> tuesdays = new AtomicReference<>(0l);
        AtomicReference<Long> wednesdays = new AtomicReference<>(0l);
        AtomicReference<Long> thursdays = new AtomicReference<>(0l);
        AtomicReference<Long> fridays = new AtomicReference<>(0l);
        AtomicReference<Long> saturdays = new AtomicReference<>(0l);
        AtomicReference<Long> sundays = new AtomicReference<>(0l);
        AtomicReference<Long> bugsMo = new AtomicReference<>(0l);
        AtomicReference<Long> bugsTu = new AtomicReference<>(0l);
        AtomicReference<Long> bugsWe = new AtomicReference<>(0l);
        AtomicReference<Long> bugsTh = new AtomicReference<>(0l);
        AtomicReference<Long> bugsFr = new AtomicReference<>(0l);
        AtomicReference<Long> bugsSa = new AtomicReference<>(0l);
        AtomicReference<Long> bugsSu = new AtomicReference<>(0l);
        AtomicReference<Long> defectsMo = new AtomicReference<>(0l);
        AtomicReference<Long> defectsTu = new AtomicReference<>(0l);
        AtomicReference<Long> defectsWe = new AtomicReference<>(0l);
        AtomicReference<Long> defectsTh = new AtomicReference<>(0l);
        AtomicReference<Long> defectsFr = new AtomicReference<>(0l);
        AtomicReference<Long> defectsSa = new AtomicReference<>(0l);
        AtomicReference<Long> defectsSu = new AtomicReference<>(0l);
        AtomicReference<Long> errorsMo = new AtomicReference<>(0l);
        AtomicReference<Long> errorsTu = new AtomicReference<>(0l);
        AtomicReference<Long> errorsWe = new AtomicReference<>(0l);
        AtomicReference<Long> errorsTh = new AtomicReference<>(0l);
        AtomicReference<Long> errorsFr = new AtomicReference<>(0l);
        AtomicReference<Long> errorsSa = new AtomicReference<>(0l);
        AtomicReference<Long> errorsSu = new AtomicReference<>(0l);

        service.findAllTickets().forEach(ticket -> {
            if(ticket.getTicketType().lowercase() == "bug"){
                if(ticket.getCreatedTimeStamp() == 1{
                    mondays.getAndSet(mondays.get() + 1);
                    bugsMo.getAndSet(bugsMo.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 2{
                    tuesdays.getAndSet(tuesdays.get() + 1);
                    bugsTu.getAndSet(bugsTu.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 3{
                    wednesdays.getAndSet(wednesdays.get() + 1);
                    bugsWe.getAndSet(bugsWe.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 4{
                    thursdays.getAndSet(thursdays.get() + 1);
                    bugsTh.getAndSet(bugsTh.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 5{
                    fridays.getAndSet(fridays.get() + 1);
                    bugsFr.getAndSet(bugsFr.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 6{
                    saturdays.getAndSet(saturdays.get() + 1);
                    bugsSa.getAndSet(bugsSa.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 7{
                    sundays.getAndSet(sundays.get() + 1);
                    bugsSu.getAndSet(bugsSu.get() + 1);
                }
            } else if(ticket.getTicketType().lowercase() == "defect"){
                if(ticket.getCreatedTimeStamp() == 1{
                    mondays.getAndSet(mondays.get() + 1);
                    defectsMo.getAndSet(defectsMo.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 2{
                    tuesdays.getAndSet(tuesdays.get() + 1);
                    defectsTu.getAndSet(defectsTu.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 3{
                    wednesdays.getAndSet(wednesdays.get() + 1);
                    defectsWe.getAndSet(defectsWe.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 4{
                    thursdays.getAndSet(thursdays.get() + 1);
                    defectsTh.getAndSet(defectsTh.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 5{
                    fridays.getAndSet(fridays.get() + 1);
                    defectsFr.getAndSet(defectsFr.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 6{
                    saturdays.getAndSet(saturdays.get() + 1);
                    defectsSa.getAndSet(defectsSa.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 7{
                    sundays.getAndSet(sundays.get() + 1);
                    defectsSu.getAndSet(defectsSu.get() + 1);
                }
            } else if(ticket.getTicketType().lowercase() == "error"){
                if(ticket.getCreatedTimeStamp() == 1{
                    mondays.getAndSet(mondays.get() + 1);
                    errorsMo.getAndSet(errorsMo.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 2{
                    tuesdays.getAndSet(tuesdays.get() + 1);
                    errorsTu.getAndSet(errorsTu.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 3{
                    wednesdays.getAndSet(wednesdays.get() + 1);
                    errorsWe.getAndSet(errorsWe.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 4{
                    thursdays.getAndSet(thursdays.get() + 1);
                    errorsTh.getAndSet(errorsTh.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 5{
                    fridays.getAndSet(fridays.get() + 1);
                    errorsFr.getAndSet(errorsFr.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 6{
                    saturdays.getAndSet(saturdays.get() + 1);
                    errorsSa.getAndSet(errorsSa.get() + 1);
                } else if(ticket.getCreatedTimeStamp() == 7{
                    sundays.getAndSet(sundays.get() + 1);
                    errorsSu.getAndSet(errorsSu.get() + 1);
                }
            }
        });

        conf.addSeries(new ListSeries("bugs", bugsMo/mondays, bugsTu/tuesdays, bugsWe/wednesdays, bugsTh/thursdays, bugsFr/fridays, bugsSa/saturdays, bugsSu/sundays));
        conf.addSeries(new ListSeries("defects", defectsMo/mondays, defectsTu/tuesdays, defectsWe/wednesdays, defectsTh/thursdays, defectsFr/fridays, defectsSa/saturdays, defectsSu/sundays));
        conf.addSeries(new ListSeries("errors", errorsMo/mondays, errorsTu/tuesdays, errorsWe/wednesdays, errorsTh/thursdays, errorsFr/fridays, errorsSa/saturdays, errorsSu/sundays));
        */


        conf.addSeries(new ListSeries("bugs", 25, 20, 30, 15, 5, 1, 0));
        conf.addSeries(new ListSeries("defects", 15, 16, 10, 2, 0, 0, 0));
        conf.addSeries(new ListSeries("errors", 2, 5, 7, 4, 1, 0, 0));

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        content.add(chart);
        content.setWidth("90vw");
        content.setHeight("auto");

        return content;
    }

    private HorizontalLayout getTurnaroundTime() {
        HorizontalLayout content = new HorizontalLayout();

        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Ticket Turnaround time");
        conf.setSubTitle("See how much time a ticket stays per status in hours");

        XAxis x = new XAxis();
        x.setCategories("bugs", "defects", "errors");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Turnaround time in hours");
        conf.addyAxis(y);

        /*
        AtomicReference<Long> bugs = new AtomicReference<>(0l);
        AtomicReference<Long> timeResolvedBugs = new AtomicReference<>(0l);
        AtomicReference<Long> defects = new AtomicReference<>(0l);
        AtomicReference<Long> timeResolvedDefects = new AtomicReference<>(0l);
        AtomicReference<Long> errors = new AtomicReference<>(0l);
        AtomicReference<Long> timeResolvedErrors = new AtomicReference<>(0l);

        service.findAllTickets().forEach(ticket -> {
            if(ticket.getTicketType().lowercase() == "bug"){
                bugs.getAndSet(bugs.get() + 1);
                timeResolvedBugs.getAndSet(timeResolvedBugs.get() + (ticket.getResolvedTimeStamp() - ticket.getCreatedTimeStamp()));
            } else if(ticket.getTicketType().lowercase() == "defect"){
                defects.getAndSet(defects.get() + 1);
                timeResolvedDefects.getAndSet(timeResolvedDefects.get() + (ticket.getResolvedTimeStamp() - ticket.getCreatedTimeStamp()));
            } else if(ticket.getTicketType().lowercase() == "error"){
                errors.getAndSet(errors.get() + 1);
                timeResolvedErrors.getAndSet(timeResolvedErrors.get() + (ticket.getResolvedTimeStamp() - ticket.getCreatedTimeStamp()));
            }
        });

        conf.addSeries(new ListSeries(timeResolvedBugs.get()/bugs.get(), timeResolvedDefects.get()/defects.get(), timeResolvedErrors.get()/errors.get())); // in hrs
         */

        conf.addSeries(new ListSeries(9, 11, 30)); // in hrs

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        content.add(chart);
        content.setWidth("90vw");
        content.setHeight("auto");

        return content;
    }

    // https://demo.vaadin.com/charts/ColumnRange
    private HorizontalLayout getAllTicketsSubmittedTimeline() {
        HorizontalLayout content = new HorizontalLayout();

        final Chart chart = new Chart(ChartType.COLUMNRANGE);

        Configuration configuration = chart.getConfiguration();
        configuration.getTitle().setText("Tickets Submitted");
        configuration.setSubTitle("Number of Tickets submitted per day");

        DataSeries dataSeries = new DataSeries("Submitted Tickets");
        /*
        service.findAllDates().forEach(date ->
                dataSeries.add(new DataSeriesItem(date, 0, service.getTicketSubmittedCount()))); // <5>
        */

        int i = 0;
        while(i < 200){
            int randomNum = ThreadLocalRandom.current().nextInt(0, 30 + 1);
            dataSeries.add(new DataSeriesItem(Instant.now().plus(i, ChronoUnit.DAYS), 0, randomNum));
            i++;
        }


        configuration.setSeries(dataSeries);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        configuration.setTooltip(tooltip);
        configuration.setTooltip(tooltip);

        chart.setTimeline(true);
        content.add(chart);
        return content;
    }

    private HorizontalLayout getAllTicketsResolvedTimeline() {
        HorizontalLayout content = new HorizontalLayout();

        final Chart chart = new Chart(ChartType.COLUMNRANGE);

        Configuration configuration = chart.getConfiguration();
        configuration.getTitle().setText("Tickets Resolved");
        configuration.setSubTitle("Number of Tickets resolved per day");

        DataSeries dataSeries = new DataSeries("Resolved Tickets");
        /*
        service.findAllDates().forEach(date ->
                dataSeries.add(new DataSeriesItem(date, 0, service.getTicketResolvedCount()))); // <5>
        */
        int i = 0;
        while(i < 200){
            int randomNum = ThreadLocalRandom.current().nextInt(0, 30 + 1);
            dataSeries.add(new DataSeriesItem(Instant.now().plus(i, ChronoUnit.DAYS), 0, randomNum));
            i++;
        }

        configuration.setSeries(dataSeries);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        configuration.setTooltip(tooltip);
        configuration.setTooltip(tooltip);

        chart.setTimeline(true);
        content.add(chart);
        return content;
    }
}
