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
    private Long id;
    private LocalDateTime vremeKreiranja;
    private UserDTO user;
    private FirmaDTO firma;
    @JsonIgnore
    private Set<StavkaNalogaDTO> stavkeNaloga = new HashSet<>();



    public NalogDTO() {
    }

    public NalogDTO(Long id, LocalDateTime vremeKreiranja, UserDTO user, FirmaDTO firma) {
        this.id = id;
        this.vremeKreiranja = vremeKreiranja;
        this.user = user;
        this.firma = firma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(LocalDateTime vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FirmaDTO getFirma() {
        return firma;
    }

    public void setFirma(FirmaDTO firma) {
        this.firma = firma;
    }

    public Set<StavkaNalogaDTO> getStavkeNaloga() {
        return stavkeNaloga;
    }

    public void setStavkeNaloga(Set<StavkaNalogaDTO> stavkeNaloga) {
        this.stavkeNaloga = stavkeNaloga;
    }
}
