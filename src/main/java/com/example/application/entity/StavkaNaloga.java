package com.example.application.entity;

import jakarta.persistence.*;

@Entity
public class StavkaNaloga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int kolicina;

    @ManyToOne
    private Nalog nalog;

    @ManyToOne
    private Roba roba;



    public StavkaNaloga() {
    }

    public StavkaNaloga(Long id, int kolicina, Nalog nalog, Roba roba) {
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
