package com.example.application.entity;

import com.arangodb.springframework.annotation.Document;
//import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;


@Document("Roba")
public class Roba {
    @Id
    private String id;

    private String naziv;
    private String sifra;           //kako da bude unique??


    public Roba() {
    }

    public Roba(String naziv, String sifra) {
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
}
