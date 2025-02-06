package com.example.application.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.application.entity.Roba;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RobaRepository extends ArangoRepository<Roba, String> {
    @Query("FOR r IN Roba FILTER r.sifra == @sifra RETURN r")
    Roba findBySifra(@Param("sifra") String sifra);
}
