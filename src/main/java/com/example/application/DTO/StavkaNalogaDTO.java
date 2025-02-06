package com.example.application.DTO;

import com.example.application.entity.Nalog;
import com.example.application.entity.Roba;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class StavkaNalogaDTO {
    private String id;
    private int kolicina;
    private Nalog nalog;
    private Roba roba;

    public StavkaNalogaDTO() {
    }

//    public StavkaNalogaDTO(String id, int kolicina, String nalogId, String robaId) {
//        this.id = id;
//        this.kolicina = kolicina;
//        this.nalogId = nalogId;
//        this.robaId = robaId;
//    }


    public StavkaNalogaDTO(String id, int kolicina, Nalog nalog, Roba roba) {
        this.id = id;
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
