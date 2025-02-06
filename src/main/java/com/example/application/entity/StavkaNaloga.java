package com.example.application.entity;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
//import jakarta.persistence.*;
import org.springframework.data.annotation.Id;


@Edge("StavkaNaloga")
public class StavkaNaloga {
    @Id
    private String id;

    private int kolicina;

    @From
    private Nalog nalog;    //veza sa Nalog dokumentom (entitetom)

    @To
    private Roba roba;    //veza sa Roba dokumentom (entitetom)


    public StavkaNaloga() {
    }

    public StavkaNaloga(int kolicina, Nalog nalog, Roba roba) {
        this.kolicina = kolicina;
        this.nalog = nalog;
        this.roba = roba;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public Nalog getNalog() {
        return nalog;
    }

    public void setNalog(Nalog nalog) {
        this.nalog = nalog;
    }

    public Roba getRoba() {
        return roba;
    }

    public void setRoba(Roba roba) {
        this.roba = roba;
    }
}
