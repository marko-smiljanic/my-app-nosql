package com.example.application.repository;

import com.example.application.DTO.NalogDTO;
import com.example.application.entity.Nalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NalogRepository extends JpaRepository<Nalog, Long>, JpaSpecificationExecutor<Nalog> {
    List<Nalog> findByUserId(Long id);
}
