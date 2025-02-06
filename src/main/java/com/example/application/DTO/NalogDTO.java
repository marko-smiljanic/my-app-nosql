package com.example.application.DTO;

import com.example.application.entity.Firma;
import com.example.application.entity.StavkaNaloga;
import com.example.application.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NalogDTO {

    private String id;
    private LocalDateTime vremeKreiranja;
    private Long userId;
    private Long firmaId;
    @JsonIgnore
    private Set<String> stavkeNalogaIds = new HashSet<>();



    public NalogDTO() {
    }

    public NalogDTO(String id, LocalDateTime vremeKreiranja, Long userId, Long firmaId) {
        this.id = id;
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

    public Set<String> getStavkeNalogaIds() {
        return stavkeNalogaIds;
    }

    public void setStavkeNalogaIds(Set<String> stavkeNalogaIds) {
        this.stavkeNalogaIds = stavkeNalogaIds;
    }

}
