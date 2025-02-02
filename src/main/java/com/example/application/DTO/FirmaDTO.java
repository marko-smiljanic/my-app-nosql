package com.example.application.DTO;

import com.example.application.entity.Nalog;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FirmaDTO {
    private Long id;
    private String naziv;
    private String pib;
    @JsonIgnore
    private Set<NalogDTO> nalozi = new HashSet<>();

    public FirmaDTO() {
    }

    public FirmaDTO(Long id, String naziv, String pib) {
        this.id = id;
        this.naziv = naziv;
        this.pib = pib;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public Set<NalogDTO> getNalozi() {
        return nalozi;
    }

    public void setNalozi(Set<NalogDTO> nalozi) {
        this.nalozi = nalozi;
    }
}
