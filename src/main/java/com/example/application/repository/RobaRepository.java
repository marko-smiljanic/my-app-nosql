package com.example.application.repository;

import com.example.application.entity.Roba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RobaRepository extends JpaRepository<Roba, Long>, JpaSpecificationExecutor<Roba> {
    Roba findBySifra(String sifra);
}
