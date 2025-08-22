package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ResponseAdminEvaluationComponentEntity;

@Repository
public interface ResponseAdminEvaluationComponentRepository extends JpaRepository<ResponseAdminEvaluationComponentEntity, Long> {
    List<ResponseAdminEvaluationComponentEntity> findByAdminEvaluationResponseId(Long adminEvaluationId);
      @Modifying
    @Query("DELETE FROM ResponseAdminEvaluationComponentEntity r WHERE r.adminEvaluationResponse.id = :evaluationId")
    void deleteByAdminEvaluationResponseId(@Param("evaluationId") Long evaluationId);

}