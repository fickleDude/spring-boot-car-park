package com.example.vaadin_app.view.admin.components;

import com.example.vaadin_app.entity.RouteEntity;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
public class RouteForm extends FormLayout {
    Binder<RouteEntity> binder = new BeanValidationBinder<>(RouteEntity.class);
    private RouteEntity route;
    TextField name = new TextField("Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public RouteForm() {
        addClassName("route-form");
        binder.bindInstanceFields(this);

        add(name,createButtonsLayout());
    }

    @Autowired
    public void setRoute(RouteEntity route){
        this.route = route;
        binder.readBean(route);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(e->validateAndSave());
        delete.addClickListener(e->fireEvent(new RouteForm.DeleteEvent(this, route)));
        close.addClickListener(e->fireEvent(new RouteForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(route);
            fireEvent(new RouteForm.SaveEvent(this, route));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class RouteFormEvent extends ComponentEvent<RouteForm> {
        private RouteEntity route;

        protected RouteFormEvent(RouteForm source, RouteEntity route) {
            super(source, false);
            this.route = route;
        }

        public RouteEntity getRoute() {
            return route;
        }
    }

    public static class SaveEvent extends RouteForm.RouteFormEvent {
        SaveEvent(RouteForm source, RouteEntity route) {
            super(source, route);
        }
    }

    public static class DeleteEvent extends RouteForm.RouteFormEvent {
        DeleteEvent(RouteForm source, RouteEntity route) {
            super(source, route);
        }

    }

    public static class CloseEvent extends RouteForm.RouteFormEvent {
        CloseEvent(RouteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
