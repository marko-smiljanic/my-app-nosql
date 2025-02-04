package com.example.application.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.example.application.entity.Nesto;
import org.springframework.stereotype.Repository;

@Repository
public interface NestoRepository extends ArangoRepository<Nesto, String> {
}
