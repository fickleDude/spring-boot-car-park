package com.example.vaadin_app.repository;

import com.example.vaadin_app.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RouteEntity, Integer> {
    RouteEntity findByName(String name);

}
