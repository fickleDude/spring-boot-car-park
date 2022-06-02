package com.example.vaadin_app.view.admin.components;

import com.example.vaadin_app.entity.Personnel;
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

public class PersonnelForm extends FormLayout {
    Binder<Personnel> binder = new BeanValidationBinder<>(Personnel.class);
    private Personnel personnel;
    TextField firstname = new TextField("First name");
    TextField lastname = new TextField("Last name");
    TextField fathername = new TextField("Father name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public PersonnelForm() {
        addClassName("personnel-form");
        binder.bindInstanceFields(this);

        add(firstname,
                lastname,
                fathername,
                createButtonsLayout());
    }

    @Autowired
    public void setPersonnel(Personnel personnel){
        this.personnel = personnel;
        binder.readBean(personnel);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(e->validateAndSave());
        delete.addClickListener(e->fireEvent(new PersonnelForm.DeleteEvent(this, personnel)));
        close.addClickListener(e->fireEvent(new PersonnelForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(personnel);
            fireEvent(new PersonnelForm.SaveEvent(this, personnel));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class PersonnelFormEvent extends ComponentEvent<PersonnelForm> {
        private Personnel personnel;

        protected PersonnelFormEvent(PersonnelForm source, Personnel personnel) {
            super(source, false);
            this.personnel = personnel;
        }

        public Personnel getPersonnel() {
            return personnel;
        }
    }

    public static class SaveEvent extends PersonnelForm.PersonnelFormEvent {
        SaveEvent(PersonnelForm source, Personnel personnel) {
            super(source, personnel);
        }
    }

    public static class DeleteEvent extends PersonnelForm.PersonnelFormEvent {
        DeleteEvent(PersonnelForm source, Personnel personnel) {
            super(source, personnel);
        }

    }

    public static class CloseEvent extends PersonnelForm.PersonnelFormEvent {
        CloseEvent(PersonnelForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
