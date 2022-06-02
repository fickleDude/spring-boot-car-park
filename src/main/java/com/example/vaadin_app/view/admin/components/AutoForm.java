package com.example.vaadin_app.view.admin.components;

import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Personnel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AutoForm extends FormLayout {
    Binder<Auto> binder = new BeanValidationBinder<>(Auto.class);
    private Auto auto;
    TextField mark = new TextField("Mark");
    TextField number = new TextField("Number");
    TextField color = new TextField("Color");

    ComboBox<Personnel> personnel = new ComboBox<>("Personnel");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public AutoForm(List<Personnel> personnelList) {
        addClassName("auto-form");
        binder.forField(personnel)
                        .bind(Auto::getPerson, Auto::setPerson);
        binder.bindInstanceFields(this);
        personnel.setItems(personnelList);
        personnel.setItemLabelGenerator(p -> p.getLastname()+" "+p.getFirstname()+" "
                + p.getLastname());

        add(mark,
                number,
                color,
                personnel,
                createButtonsLayout());
    }

    @Autowired
    public void setAuto(Auto auto){
        this.auto = auto;
        binder.readBean(auto);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(e->validateAndSave());
        delete.addClickListener(e->fireEvent(new DeleteEvent(this, auto)));
        close.addClickListener(e->fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    public ComboBox<Personnel> getPersonnel() {
        return personnel;
    }

    private void validateAndSave() {
        try {
            binder.writeBean(auto);
            auto.addPerson(personnel.getValue());
            fireEvent(new SaveEvent(this, auto));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class AutoFormEvent extends ComponentEvent<AutoForm> {
        private Auto auto;

        protected AutoFormEvent(AutoForm source, Auto auto) {
            super(source, false);
            this.auto = auto;
        }

        public Auto getAuto() {
            return auto;
        }
    }

    public static class SaveEvent extends AutoFormEvent {
        SaveEvent(AutoForm source, Auto auto) {
            super(source, auto);
        }
    }

    public static class DeleteEvent extends AutoFormEvent {
        DeleteEvent(AutoForm source, Auto auto) {
            super(source, auto);
        }

    }

    public static class CloseEvent extends AutoFormEvent {
        CloseEvent(AutoForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
