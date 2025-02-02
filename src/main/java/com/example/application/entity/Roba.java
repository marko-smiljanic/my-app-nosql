package com.example.application.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Roba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false, unique = true)
    private String sifra;


    @OneToMany(mappedBy = "roba")
    private Set<StavkaNaloga> stavkeNaloga = new HashSet<StavkaNaloga>();

    
    public Roba() {
    }

    public Roba(Long id, String naziv, String sifra) {
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

    public Set<StavkaNaloga> getStavkeNaloga() {
        return stavkeNaloga;
    }

    public void setStavkeNaloga(Set<StavkaNaloga> stavkeNaloga) {
        this.stavkeNaloga = stavkeNaloga;
    }
}
