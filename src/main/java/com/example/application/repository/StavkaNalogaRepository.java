package com.example.application.repository;

import com.example.application.entity.StavkaNaloga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StavkaNalogaRepository extends JpaRepository<StavkaNaloga, Long>, JpaSpecificationExecutor<StavkaNaloga> {
}
