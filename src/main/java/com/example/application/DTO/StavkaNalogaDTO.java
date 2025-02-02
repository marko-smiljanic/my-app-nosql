package com.example.application.DTO;

import com.example.application.entity.Nalog;
import com.example.application.entity.Roba;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class StavkaNalogaDTO {
    private Long id;
    private int kolicina;
    private NalogDTO nalog;
    private RobaDTO roba;

    public StavkaNalogaDTO() {
    }

    public StavkaNalogaDTO(Long id, int kolicina, NalogDTO nalog, RobaDTO roba) {
        this.id = id;
        this.kolicina = kolicina;
        this.nalog = nalog;
        this.roba = roba;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public NalogDTO getNalog() {
        return nalog;
    }

    public void setNalog(NalogDTO nalog) {
        this.nalog = nalog;
    }

    public RobaDTO getRoba() {
        return roba;
    }

    public void setRoba(RobaDTO roba) {
        this.roba = roba;
    }
}
