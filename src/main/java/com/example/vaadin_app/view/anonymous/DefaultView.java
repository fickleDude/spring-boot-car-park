package com.example.vaadin_app.view.anonymous;

import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Journal;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.service.JournalService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "welcome-view", layout = MainLayout.class)
@AnonymousAllowed
@PageTitle("Welcome to car park!")
public class DefaultView extends VerticalLayout {
    Grid<Journal> grid = new Grid<>();
    ComboBox<Personnel> filterByPersonnel = new ComboBox<>();
    ComboBox<Auto> filterByAuto = new ComboBox<>();
    JournalService service;

    @Autowired
    public DefaultView(JournalService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateRepository();
    }

    private void updateRepository() {
        grid.setItems(service.findAllJournals(filterByPersonnel.getValue(),
                filterByAuto.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(Journal::getTimeOut).setHeader("Time out");
        grid.addColumn(Journal::getTimeIn).setHeader("Time in");
        grid.addColumn(journal -> journal.getAuto().getMark() + " " + journal.getAuto().getNumber()
                        + " " + journal.getAuto().getColor())
                .setHeader("Auto");
        grid.addColumn(journal -> journal.getRouteEntity().getName()).setHeader("Route");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterByPersonnel.setPlaceholder("Filter by personnel...");
        filterByPersonnel.setClearButtonVisible(true);
        filterByPersonnel.setItems(service.findAllPersonnel());
        filterByPersonnel.setItemLabelGenerator(personnel -> personnel.getLastname() + ' '
                + personnel.getFirstname() + ' ' + personnel.getFathername());
        filterByPersonnel.addValueChangeListener(e -> updateRepository());
        filterByAuto.addValueChangeListener(e -> updateRepository());

        filterByAuto.setPlaceholder("Filter by auto...");
        filterByAuto.setClearButtonVisible(true);
        filterByAuto.setItems(service.findAllAutos());
        filterByAuto.setItemLabelGenerator(auto -> auto.getMark() + " " + auto.getNumber() + " " + auto.getColor());
        filterByPersonnel.addValueChangeListener(e -> updateRepository());

        HorizontalLayout toolbar = new HorizontalLayout(filterByPersonnel, filterByAuto);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
