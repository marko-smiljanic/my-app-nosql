package com.example.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    private String telefon;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String hashedPassword;

    //ovo takodje pravi tabelu roles, bez potrebe za entitetom roles
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Lob
    //@Column(length = 1000000)
    private byte[] profilePicture;

    @OneToMany(mappedBy = "user")
    private Set<Nalog> nalog = new HashSet<Nalog>();




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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Nalog> getNalog() {
        return nalog;
    }

    public void setNalog(Set<Nalog> nalog) {
        this.nalog = nalog;
    }



}
