package com.example.vaadin_app.view.admin.components;

import com.example.vaadin_app.view.admin.components.impl.LocalTimeToTimestampConverter;
import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Journal;
import com.example.vaadin_app.entity.RouteEntity;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

public class JournalForm extends FormLayout {
    Binder<Journal> binder = new BeanValidationBinder<>(Journal.class);
    private Journal journal;
    TimePicker timein = new TimePicker("Time in");
    TimePicker timeout = new TimePicker("Time out");

    ComboBox<Auto> auto = new ComboBox<>("Auto");
    ComboBox<RouteEntity> route = new ComboBox<>("Route");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public JournalForm(List<RouteEntity> routeList, List<Auto> autoList) {
        addClassName("journal-form");

        Binder.Binding<Journal, Timestamp> timeBinding = binder.forField(timein)
                .withValidator(time -> time == null || time.isAfter(timeout.getValue()),
                        "time in have to be after time out")
                .withConverter(new LocalTimeToTimestampConverter())
                .bind(Journal::getTimeIn, Journal::setTimeIn);
        binder.forField(timeout)
                .withConverter(new LocalTimeToTimestampConverter())
                .bind(Journal::getTimeOut, Journal::setTimeOut);
        timeout.addValueChangeListener(event->timeBinding.validate());
        binder.forField(route)
                        .bind(Journal::getRouteEntity, Journal::setRouteEntity);
        binder.bindInstanceFields(this);

        auto.setItems(autoList);
        auto.setItemLabelGenerator(auto->auto.getMark()+" "+auto.getNumber()
                +" "+auto.getColor());
        route.setItems(routeList);
        route.setItemLabelGenerator(RouteEntity::getName);

        add(timein,
                timeout,
                route,
                auto,
                createButtonsLayout());
    }

    @Autowired
    public void setJournal(Journal journal){
        this.journal = journal;
        binder.readBean(journal);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(e->validateAndSave());
        delete.addClickListener(e->deleteRecord());
        close.addClickListener(e->fireEvent(new JournalForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);
    }

    private void deleteRecord() {
        auto.getValue().removeRecord(journal);
        route.getValue().removeRecord(journal);
        fireEvent(new JournalForm.DeleteEvent(this, journal));
    }


    private void validateAndSave() {
        try {
            binder.writeBean(journal);
            journal.setAuto(auto.getValue());
            journal.setRouteEntity(route.getValue());
            fireEvent(new JournalForm.SaveEvent(this, journal));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class JournalFormEvent extends ComponentEvent<JournalForm> {
        private Journal journal;

        protected JournalFormEvent(JournalForm source, Journal journal) {
            super(source, false);
            this.journal = journal;
        }

        public Journal getJournal() {
            return journal;
        }
    }

    public static class SaveEvent extends JournalForm.JournalFormEvent {
        SaveEvent(JournalForm source, Journal journal) {
            super(source, journal);
        }
    }

    public static class DeleteEvent extends JournalForm.JournalFormEvent {
        DeleteEvent(JournalForm source, Journal journal) {
            super(source, journal);
        }

    }

    public static class CloseEvent extends JournalForm.JournalFormEvent {
        CloseEvent(JournalForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
