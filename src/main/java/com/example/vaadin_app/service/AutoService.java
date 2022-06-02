package com.example.vaadin_app.service;

import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.repository.AutoRepository;
import com.example.vaadin_app.repository.PersonnelRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
public class AutoService {
    private final AutoRepository autoRepository;
    private final PersonnelRepository personnelRepository;

    public List<Auto> findFree(){
        return autoRepository.findAll().stream().filter(auto -> !auto.getPerson()
                .onRoute()).collect(Collectors.toList());
    }

    public List<Auto> findAllAutos(Personnel personnel) {
        if (personnel == null) {
            return autoRepository.findAll();
        } else {
            return autoRepository.findByPersonId(personnel.getId());
        }
    }

    public void deleteAuto(Auto auto) {
        auto.removePerson();
        autoRepository.delete(auto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAuto(Auto auto, Personnel personnel) {
        if (auto == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        autoRepository.save(auto);
        //auto.addPerson(personnel);
    }

    public List<Personnel> findAllPersonnel() {
        return personnelRepository.findAll();
    }
}
