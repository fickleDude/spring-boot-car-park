package com.example.vaadin_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "route")
public class RouteEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //нужно, чтобы не ругался при удалении пути, а удалял записи с ним
    @OneToMany(mappedBy = "routeEntity", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Journal> records = new LinkedList<>();

    public void addRecord(Journal record){
        if(record != null)
            records.add(record);
    }
    public void removeRecord(Journal record){
        records.remove(record);
    }
}
