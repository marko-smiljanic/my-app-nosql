package com.example.application.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.application.entity.StavkaNaloga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StavkaNalogaRepository extends ArangoRepository<StavkaNaloga, String> {
    @Query("FOR s IN StavkaNaloga FILTER s.nalogId == @nalogId RETURN s")
    List<StavkaNaloga> findByNalogId(@Param("nalogId") String nalogId);

    @Query("FOR s IN StavkaNaloga FILTER s.nalogId == @nalogId REMOVE s IN StavkaNaloga")
    void deleteByNalogId(@Param("nalogId") String nalogId);


}
