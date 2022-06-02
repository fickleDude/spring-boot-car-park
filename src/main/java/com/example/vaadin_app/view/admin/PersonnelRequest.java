package com.example.vaadin_app.view.admin;

import com.example.vaadin_app.view.admin.components.PersonnelForm;
import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.service.PersonnelService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "personnel")
@PageTitle("Personnel")
@RolesAllowed("ADMIN")
public class PersonnelRequest extends VerticalLayout {
    Grid<Personnel> grid = new Grid<>();
    TextField filterText = new TextField();
    PersonnelService service;
    PersonnelForm form;

    ComboBox<Auto> auto = new ComboBox<>("Autos");

    @Autowired
    public PersonnelRequest(PersonnelService service) {
        this.service = service;
        addClassName("personnel-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateRepository();
        closeEditor();
    }

    private void closeEditor() {
        form.setPersonnel(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRepository() {
        grid.setItems(service.findAllPersonnel(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("personnel-content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new PersonnelForm();
        form.setWidth("25em");

        form.addListener(PersonnelForm.SaveEvent.class, this::savePersonnel);
        form.addListener(PersonnelForm.DeleteEvent.class, this::deletePersonnel);
        form.addListener(PersonnelForm.CloseEvent.class, e -> closeEditor());
    }

    private void savePersonnel(PersonnelForm.SaveEvent event) {
        service.savePersonnel(event.getPersonnel());
        updateRepository();
        closeEditor();
    }

    private void deletePersonnel(PersonnelForm.DeleteEvent event) {
        service.deletePersonnel(event.getPersonnel());
        updateRepository();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("personnel-grid");
        grid.setSizeFull();
        grid.addColumn(Personnel::getLastname).setHeader("Last name");
        grid.addColumn(Personnel::getFirstname).setHeader("First name");
        grid.addColumn(Personnel::getFathername).setHeader("Father name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editPersonnel(e.getValue()));
    }

    private void editPersonnel(Personnel personnel) {
        if (personnel == null) {
            closeEditor();
        } else {
            form.setPersonnel(personnel);
            form.setVisible(true);
            setClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateRepository());

        Button addAutoButton = new Button("Add personnel");
        addAutoButton.addClickListener(e -> addPersonnel());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addAutoButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addPersonnel() {
        grid.asSingleSelect().clear();
        editPersonnel(new Personnel());
    }
}
