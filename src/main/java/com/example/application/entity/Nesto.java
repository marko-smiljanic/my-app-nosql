package com.example.application.entity;


import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

@Document("Nesto")  // Ovo je ime kolekcije u ArangoDB
public class Nesto {
    @Id
    private String id;

    @ArangoId
    private String arangoId;

    private String ime;
    private int broj;

    public Nesto() {
    }

    public Nesto(String ime, int broj) {
        this.ime = ime;
        this.broj = broj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getBroj() {
        return broj;
    }

    public void setBroj(int broj) {
        this.broj = broj;
    }
}

