package com.example.vaadin_app.view.admin;

import com.example.vaadin_app.view.admin.components.AutoForm;
import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.service.AutoService;
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

@Route(value = "auto")
@PageTitle("Autos")
@RolesAllowed("ADMIN")
public class AutoRequest extends VerticalLayout {
    Grid<Auto> grid = new Grid<>();
    ComboBox<Personnel> filterText = new ComboBox<>();
    AutoService service;
    AutoForm form;

    @Autowired
    public AutoRequest(AutoService service) {
        this.service = service;
        addClassName("auto-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateRepository();
        closeEditor();
    }

    private void closeEditor() {
        form.setAuto(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRepository() {
        grid.setItems(service.findAllAutos(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("auto-content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new AutoForm(service.findAllPersonnel());
        form.setWidth("25em");

        form.addListener(AutoForm.SaveEvent.class, this::saveAuto);
        form.addListener(AutoForm.DeleteEvent.class, this::deleteAuto);
        form.addListener(AutoForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveAuto(AutoForm.SaveEvent event) {
        service.saveAuto(event.getAuto(), form.getPersonnel().getValue());
        updateRepository();
        closeEditor();
    }

    private void deleteAuto(AutoForm.DeleteEvent event) {
        service.deleteAuto(event.getAuto());
        updateRepository();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("auto-grid");
        grid.setSizeFull();
        grid.addColumn(Auto::getMark).setHeader("Mark");
        grid.addColumn(Auto::getNumber).setHeader("Number");
        grid.addColumn(Auto::getColor).setHeader("Color");
        grid.addColumn(auto -> auto.getPerson().getLastname() + " " + auto.getPerson().getFirstname() +
                " " + auto.getPerson().getFathername()).setHeader("Personnel");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editAuto(e.getValue()));
    }

    private void editAuto(Auto auto) {
        if (auto == null) {
            closeEditor();
        } else {
            form.setAuto(auto);
            form.setVisible(true);
            setClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by personnel...");
        filterText.setClearButtonVisible(true);
        filterText.setItems(service.getPersonnelRepository().findAll());
        filterText.setItemLabelGenerator(personnel -> personnel.getLastname() + ' '
                + personnel.getFirstname() + ' ' + personnel.getFathername());
        filterText.addValueChangeListener(e -> updateRepository());

        Button addAutoButton = new Button("Add auto");
        addAutoButton.addClickListener(e -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addAutoButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editAuto(new Auto());
    }
}
