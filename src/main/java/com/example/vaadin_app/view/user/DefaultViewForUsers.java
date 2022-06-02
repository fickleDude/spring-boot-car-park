package com.example.vaadin_app.view.user;

import com.example.vaadin_app.view.admin.components.JournalForm;
import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Journal;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.service.JournalService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "", layout = MainLayoutForUsers.class)
@RolesAllowed({"ADMIN", "USER"})
@PageTitle("Journal")
public class DefaultViewForUsers extends VerticalLayout {
    Grid<Journal> grid = new Grid<>();
    ComboBox<Personnel> filterByPersonnel = new ComboBox<>();
    ComboBox<Auto> filterByAuto = new ComboBox<>();
    JournalService service;
    JournalForm form;

    @Autowired
    public DefaultViewForUsers(JournalService service) {
        this.service = service;
        addClassName("journal-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateRepository();
        closeEditor();
    }

    private void closeEditor() {
        form.setJournal(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRepository() {
        grid.setItems(service.findAllJournals(filterByPersonnel.getValue(),
                filterByAuto.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("journal-content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new JournalForm(service.findAllRoutes(), service.findAllFreeAutos());
        form.setWidth("25em");

        form.addListener(JournalForm.SaveEvent.class, this::saveJournal);
        form.addListener(JournalForm.DeleteEvent.class, this::deleteJournal);
        form.addListener(JournalForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveJournal(JournalForm.SaveEvent event) {
        service.saveJournal(event.getJournal(), event.getJournal().getAuto(),
                event.getJournal().getRouteEntity());
        updateRepository();
        closeEditor();
    }

    private void deleteJournal(JournalForm.DeleteEvent event) {
        service.deleteJournal(event.getJournal(), event.getJournal().getAuto(),
                event.getJournal().getRouteEntity());
        updateRepository();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("journal-grid");
        grid.setSizeFull();
        grid.addColumn(Journal::getTimeOut).setHeader("Time out");
        grid.addColumn(Journal::getTimeIn).setHeader("Time in");
        grid.addColumn(journal -> journal.getAuto().getMark() + " " + journal.getAuto().getNumber()
                        + " " + journal.getAuto().getColor())
                .setHeader("Auto");
        grid.addColumn(journal -> journal.getRouteEntity().getName()).setHeader("Route");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editJournal(e.getValue()));
    }

    private void editJournal(Journal journal) {
        if (journal == null) {
            closeEditor();
        } else {
            form.setJournal(journal);
            form.setVisible(true);
            setClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterByPersonnel.setPlaceholder("Filter by personnel...");
        filterByPersonnel.setClearButtonVisible(true);
        filterByPersonnel.setItems(service.findAllPersonnel());
        filterByPersonnel.setItemLabelGenerator(personnel -> personnel.getLastname() + ' '
                + personnel.getFirstname() + ' ' + personnel.getFathername());
        filterByPersonnel.addValueChangeListener(e -> updateRepository());

        filterByAuto.setPlaceholder("Filter by auto...");
        filterByAuto.setClearButtonVisible(true);
        filterByAuto.setItems(service.findAllAutos());
        filterByAuto.setItemLabelGenerator(auto -> auto.getMark() + " " + auto.getNumber() + " " + auto.getColor());
        filterByPersonnel.addValueChangeListener(e -> updateRepository());

        Button addAutoButton = new Button("Make record");
        addAutoButton.addClickListener(e -> addJournal());

        HorizontalLayout toolbar = new HorizontalLayout(filterByPersonnel, addAutoButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addJournal() {
        grid.asSingleSelect().clear();
        editJournal(new Journal());
    }
}
