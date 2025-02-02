package com.example.application.repository;

import com.example.application.entity.Firma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmaRepository extends JpaRepository<Firma, Long>, JpaSpecificationExecutor<Firma> {

    Firma findByNaziv(String naziv);
    Firma findByPib(String pib);
}
