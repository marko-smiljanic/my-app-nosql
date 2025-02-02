package com.example.application.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Firma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false, unique = true)
    private String pib;

    @OneToMany(mappedBy = "firma")
    private Set<Nalog> nalozi = new HashSet<Nalog>();



    public Firma() {
    }

    public Firma(Long id, String naziv, String pib) {
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

    public Set<Nalog> getNalozi() {
        return nalozi;
    }

    public void setNalozi(Set<Nalog> nalozi) {
        this.nalozi = nalozi;
    }



}
