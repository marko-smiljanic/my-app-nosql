package com.example.application.entity;

import com.arangodb.springframework.annotation.Document;
//import jakarta.persistence.*;
import org.springframework.data.annotation.Id;   //ZA ARANGO ENTITET MORA OVA ANOTACIJA, NE JAKARTINA !!!!!!

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Document("Nalog")
public class Nalog {
    @Id
    private String id;

    private LocalDateTime vremeKreiranja;
    private Long userId;   //Id korisnika iz SQL baze
    private Long firmaId;   //Id firme iz SQL baze


    public Nalog() {
    }

    public Nalog(LocalDateTime vremeKreiranja, Long userId, Long firmaId) {
        this.vremeKreiranja = vremeKreiranja;
        this.userId = userId;
        this.firmaId = firmaId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(LocalDateTime vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFirmaId() {
        return firmaId;
    }

    public void setFirmaId(Long firmaId) {
        this.firmaId = firmaId;
    }
}
