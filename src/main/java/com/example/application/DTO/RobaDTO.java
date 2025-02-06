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
    private String id;
    private String naziv;
    private String sifra;
    @JsonIgnore
    private Set<String> stavkeNaloga = new HashSet<>();

    public RobaDTO() {
    }

    public RobaDTO(String id, String naziv, String sifra) {
        this.id = id;
        this.naziv = naziv;
        this.sifra = sifra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Set<String> getStavkeNaloga() {
        return stavkeNaloga;
    }

    public void setStavkeNaloga(Set<String> stavkeNaloga) {
        this.stavkeNaloga = stavkeNaloga;
    }
}
