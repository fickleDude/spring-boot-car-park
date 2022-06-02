package com.example.vaadin_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Personnel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private String fathername;
    @OneToMany(mappedBy = "person", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Auto> autos = new LinkedList<>();

    public Personnel() {
    }

    public Personnel(String firstName, String lastName, String fatherName) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.fathername = fatherName;
    }

    public boolean onRoute(){
        for(Auto auto: autos){
            List<Journal> autosOnRoute = auto.getRecords().stream()
                    .filter(journal -> journal.getTimeIn() == null)
                    .collect(Collectors.toList());
            return !autosOnRoute.isEmpty();
        }
        return false;//т.к. все равно машина не может поехать без водителя
    }
    public void addAuto(Auto auto){
        this.getAutos().add(auto);
    }
    public void removeAuto(Auto auto){
        this.getAutos().remove(auto);//используем перед удалением машины
    }
}

