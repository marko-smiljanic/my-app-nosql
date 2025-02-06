package com.example.application.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.application.DTO.NalogDTO;
import com.example.application.entity.Nalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NalogRepository extends ArangoRepository<Nalog, String> {
    @Query("FOR n IN Nalog FILTER n.userId == @userId RETURN n")
    List<Nalog> findByUserId(@Param("userId") Long id);

    @Query("REMOVE { _key: @id } IN Nalog")
    void deleteById(@Param("id") String id);

    //paginacija sa arango upitom, ovo se doradjudje u servisu

//    @Query("FOR n IN Nalog SORT n._key LIMIT @offset, @limit RETURN n")
//    List<Nalog> findAllPaginated(@Param("offset") int offset, @Param("limit") int limit);

}
