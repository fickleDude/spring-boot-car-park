package com.example.vaadin_app.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Journal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "timein")
    private Timestamp timeIn;
    @Column(name = "timeout")
    private Timestamp timeOut;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autoid")
    private Auto auto;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routeid")
    private RouteEntity routeEntity;

    public void setAuto(Auto auto) {
        if (auto != null)
            auto.addRecord(this);
        if (this.auto != null) {
            this.removeAutoRecord();
        }
        this.auto = auto;
    }

    public void removeAutoRecord() {
        auto.removeRecord(this);
    }


    public void setRouteEntity(RouteEntity routeEntity) {
        if (routeEntity != null)
            routeEntity.addRecord(this);
        if (this.routeEntity != null)
            this.removeRouteRecord();
        this.routeEntity = routeEntity;
    }

    public void removeRouteRecord() {
        routeEntity.removeRecord(this);
    }
}
