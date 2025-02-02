package com.example.application.DTO;

import com.example.application.entity.StavkaNaloga;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RobaDTO {
    private Long id;
    private String naziv;
    private String sifra;
    @JsonIgnore
    private Set<StavkaNalogaDTO> stavkeNaloga = new HashSet<>();

    public RobaDTO() {
    }

    public RobaDTO(Long id, String naziv, String sifra) {
        this.id = id;
        this.naziv = naziv;
        this.sifra = sifra;
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

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public Set<StavkaNalogaDTO> getStavkeNaloga() {
        return stavkeNaloga;
    }

    public void setStavkeNaloga(Set<StavkaNalogaDTO> stavkeNaloga) {
        this.stavkeNaloga = stavkeNaloga;
    }
}
