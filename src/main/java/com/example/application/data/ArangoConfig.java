package com.example.application.data;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"com.example.application.repository"})
public class ArangoConfig implements ArangoConfiguration {

//    private static final String[] COLLECTIONS = {"Nesto"}; // Dodaj svoje kolekcije
//    private static final String DATABASE_NAME = "projekatNoSQL";

    @Override
    public ArangoDB.Builder arango() {
        //          ArangoDB arangoDB = new ArangoDB.Builder()
//                .host("localhost", 8529)
//                .user("root")
//                .password("")
//                .build();
//
//        // Automatsko kreiranje baze ako ne postoji
//        if (!arangoDB.getDatabases().contains(DATABASE_NAME)) {
//            arangoDB.createDatabase(DATABASE_NAME);
//        }
//
//        // Automatsko kreiranje kolekcija ako ne postoje
//        for (String collection : COLLECTIONS) {
//            if (!arangoDB.db(DATABASE_NAME).collection(collection).exists()) {
//                arangoDB.db(DATABASE_NAME).createCollection(collection);
//            }
//        }

        return new ArangoDB.Builder()
                .host("localhost", 8529)
                .user("root")
                .password("");
    }

    @Override
    public String database() {
        return "projekatNoSQL";
    }

    @Override
    public boolean returnOriginalEntities() {
        return true;
    }












}
