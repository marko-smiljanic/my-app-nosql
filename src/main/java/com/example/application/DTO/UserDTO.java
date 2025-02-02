package com.example.application.DTO;


import com.example.application.entity.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashSet;
import java.util.Set;

//ako hocu da se neko polje ne mapira u grid (view komponentu) moram skloniti getere i setere, json ignore ne radi posao u ovom slucaju
//json ignore je u stvari za pozive sa apijem, odnosno sta se vraca kroz json u http zahtevu
//ako hocu da neka polja izbacim iz dto, moram da sklonim getere i setere


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserDTO {
    private Long id;
    private String ime;
    private String prezime;
    private String telefon;
    private String username;
    private byte[] profilePicture;
    @JsonIgnore
    private String hashedPassword;
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();  // Dodato za uloge
    @JsonIgnore
    private Set<NalogDTO> nalog = new HashSet<>();


    public UserDTO() {}

    // Konstruktor sa svim potrebnim poljima
    public UserDTO(Long id, String ime, String prezime, String telefon, String username, byte[] profilePicture) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.username = username;
        this.profilePicture = profilePicture;
    }



    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", telefon='" + telefon + '\'' +
                ", username='" + username + '\'' +
                ", profilePicture=" + (profilePicture != null ? "[byte array of length " + profilePicture.length + "]" : "null") +
                ", roles=" + roles +
                ", nalog=" + nalog +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<NalogDTO> getNalog() {
        return nalog;
    }

    public void setNalog(Set<NalogDTO> nalog) {
        this.nalog = nalog;
    }
}
