package com.example.vaadin_app.repository;

import com.example.vaadin_app.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, Integer> {
    @Query(value = "SELECT * FROM Personnel p WHERE concat(p.lastname, ' ', p.firstname, ' ', p.fathername) " +
            "like concat('%', :name, '%')",
            nativeQuery = true)
    List<Personnel> findByName(@Param("name") String name);
}
