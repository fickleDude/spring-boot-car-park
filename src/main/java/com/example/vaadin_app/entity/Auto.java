package com.example.vaadin_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auto implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String number;
    private String color;
    @NotBlank
    @Size(max = 10)
    private String mark;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnelid")
    private Personnel person;
    //нужно чтобы не ругался при удалении машины, а удалял записи с ней
    @OneToMany(mappedBy = "auto", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Journal> records = new LinkedList<>();

    //используем в Journal
    public void addRecord(Journal record){
        if(record != null)
            records.add(record);
    }
    public void removeRecord(Journal record){
        records.remove(record);
    }

    public void addPerson(Personnel person){//используем в addAuto водителя
        if(person != null)
            person.addAuto(this);
        if(this.person != null)
            this.person.removeAuto(this);
        this.person = person;
    }
    public void removePerson(){
        if(person!=null)
            person.removeAuto(this);
    }

}
