package com.example.vaadin_app.service;

import com.example.vaadin_app.entity.Auto;
import com.example.vaadin_app.entity.Journal;
import com.example.vaadin_app.entity.Personnel;
import com.example.vaadin_app.entity.RouteEntity;
import com.example.vaadin_app.repository.AutoRepository;
import com.example.vaadin_app.repository.JournalRepository;
import com.example.vaadin_app.repository.PersonnelRepository;
import com.example.vaadin_app.repository.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JournalService {
    private final JournalRepository journalRepository;
    private final AutoRepository autoRepository;
    private final PersonnelRepository personnelRepository;
    private final RouteRepository routeRepository;

    public List<Journal> findAllJournals(Personnel personnel, Auto auto) {
        if (auto != null && personnel != null) {
            return journalRepository.findByAutoNumberAndAutoPersonId(auto.getNumber(),
                    personnel.getId());
        } else if (auto != null) {
            return journalRepository.findByAutoNumber(auto.getNumber());
        } else if (personnel != null) {
            return journalRepository.findByAutoPersonId(personnel.getId());
        }
        return journalRepository.findAll();
    }

    @Transactional
    public void deleteJournal(Journal journal, Auto auto, RouteEntity route) {
        autoRepository.save(auto);
        routeRepository.save(route);
        journalRepository.delete(journal);
    }

    @Transactional
    public void saveJournal(Journal journal, Auto auto, RouteEntity route) {
        autoRepository.save(auto);
        routeRepository.save(route);
        journalRepository.save(journal);
    }

    public List<Personnel> findAllPersonnel() {
        return personnelRepository.findAll();
    }

    public List<Auto> findAllAutos() {
        return autoRepository.findAll();
    }

    public List<Auto> findAllFreeAutos() {
        List<Auto> freeAuto = new ArrayList<>();
        for (Personnel personnel : personnelRepository.findAll()) {
            if (!personnel.onRoute()) {
                freeAuto.addAll(personnel.getAutos());
            }
        }
        return freeAuto;
    }

    public List<RouteEntity> findAllRoutes() {
        return routeRepository.findAll();
    }
}
