package com.example.vaadin_app.view.admin;

import com.example.vaadin_app.view.admin.components.RouteForm;
import com.example.vaadin_app.entity.RouteEntity;
import com.example.vaadin_app.service.RouteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@Route(value = "route")
@PageTitle("Routes")
@RolesAllowed("ADMIN")
public class RouteRequest extends VerticalLayout {
    Grid<RouteEntity> grid = new Grid<>();
    RouteService service;
    RouteForm form;

    @Autowired
    public RouteRequest(RouteService service) {
        this.service = service;
        addClassName("route-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateRepository();
        closeEditor();
    }

    private void closeEditor() {
        form.setRoute(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRepository() {
        grid.setItems(service.findAll());
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
        form = new RouteForm();
        form.setWidth("25em");

        form.addListener(RouteForm.SaveEvent.class, this::saveRoute);
        form.addListener(RouteForm.DeleteEvent.class, this::deleteRoute);
        form.addListener(RouteForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveRoute(RouteForm.SaveEvent event) {
        service.saveRoute(event.getRoute());
        updateRepository();
        closeEditor();
    }

    private void deleteRoute(RouteForm.DeleteEvent event) {
        service.deleteRoute(event.getRoute());
        updateRepository();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("route-grid");
        grid.setSizeFull();
        grid.addColumn(RouteEntity::getName).setHeader("Name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editRoute(e.getValue()));
    }

    private void editRoute(RouteEntity route) {
        if (route == null) {
            closeEditor();
        } else {
            form.setRoute(route);
            form.setVisible(true);
            setClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        Button addAutoButton = new Button("Add route");
        addAutoButton.addClickListener(e -> addRoute());

        HorizontalLayout toolbar = new HorizontalLayout(addAutoButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addRoute() {
        grid.asSingleSelect().clear();
        editRoute(new RouteEntity());
    }
}
