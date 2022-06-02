package com.example.vaadin_app.repository;

import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutoRepository extends JpaRepository<Auto, Integer> {
    List<Auto> findByPersonId(int id);

    Auto findByNumber(String number);
//    @Query(value = "SELECT * FROM Auto a WHERE concat(a.person.lastname, ' ', " +
//            "a.person.firstname, ' ', a.person.fathername) " +
//            "like concat('%', :name, '%')",
//            nativeQuery = true)
//    List<Auto> findByPersonnel(@Param("name") String name);


}
