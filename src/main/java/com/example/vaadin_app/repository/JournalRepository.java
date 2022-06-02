package com.example.vaadin_app.repository;

import com.example.vaadin_app.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal,Integer> {
    List<Journal> findByAutoPersonId(int id);

    List<Journal> findByAutoNumber(String number);

    List<Journal> findByAutoNumberAndAutoPersonId(String number, int id);


}
