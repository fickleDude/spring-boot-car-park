package com.example.vaadin_app.service;

import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.repository.AutoRepository;
import com.example.vaadin_app.repository.PersonnelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
    private final AutoRepository autoRepository;

    public void savePersonnel(Personnel personnel) {
        personnelRepository.save(personnel);
    }

    public void deletePersonnel(Personnel personnel){
        personnelRepository.delete(personnel);
    }

    public List<Personnel> findAllPersonnel(String name){
        if(name != null){
            return personnelRepository.findByName(name);
        }
        return personnelRepository.findAll();
    }

    public Optional<Personnel> findById(int id){
        return personnelRepository.findById(id);
    }
}
