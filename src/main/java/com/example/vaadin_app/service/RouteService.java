package com.example.vaadin_app.service;

import com.example.vaadin_app.entity.RouteEntity;
import com.example.vaadin_app.repository.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;

    public List<RouteEntity> findAll(){
        return routeRepository.findAll();
    }

    public Optional<RouteEntity> findById(int id){
        return routeRepository.findById(id);
    }

    public void saveRoute(RouteEntity route){
        routeRepository.save(route);
    }

    public void deleteRoute(RouteEntity route){
        routeRepository.delete(route);
    }
}
