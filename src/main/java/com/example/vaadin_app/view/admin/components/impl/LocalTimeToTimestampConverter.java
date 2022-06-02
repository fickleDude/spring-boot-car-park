package com.example.vaadin_app.view.admin.components.impl;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeToTimestampConverter implements Converter<LocalTime, Timestamp> {
    // receives a value from the user
    @Override
    public Result<Timestamp> convertToModel(LocalTime localTime, ValueContext valueContext) {
        if(localTime == null)
            return Result.ok(null);
        try{
            LocalDateTime value = localTime.atDate(LocalDate.now());
            return Result.ok(Timestamp.valueOf(value.format(DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss"))));
        }catch(IllegalArgumentException e){
            return Result.error(e.getMessage());
        }

    }
    //receives a value from the business object
    @Override
    public LocalTime convertToPresentation(Timestamp timestamp, ValueContext valueContext) {
        if(timestamp == null)
            return null;
        return timestamp.toLocalDateTime().toLocalTime();
    }
}
