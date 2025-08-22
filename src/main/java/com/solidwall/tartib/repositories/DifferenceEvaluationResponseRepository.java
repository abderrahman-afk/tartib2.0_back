package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.DifferenceEvaluationResponseEntity;

@Repository
public interface DifferenceEvaluationResponseRepository extends JpaRepository<DifferenceEvaluationResponseEntity, Long> {
    // Find differences for a specific project evaluation
    Optional<List<DifferenceEvaluationResponseEntity>> findByProjectEvaluationId(Long projectEvaluationId);
    
    // Find differences for a specific admin evaluation  
    Optional<List<DifferenceEvaluationResponseEntity>> findByAdminEvaluationId(Long adminEvaluationId);
    
    @Query("SELECT DISTINCT pe.project.id FROM DifferenceEvaluationResponseEntity d JOIN d.projectEvaluation pe")
Optional<List<Long>> findDistinctProjectIds();
    
    // Find specific difference by indicator and evaluations
    Optional<DifferenceEvaluationResponseEntity> findByIndicatorIdAndProjectEvaluationIdAndAdminEvaluationId(
        Long indicatorId, 
        Long projectEvaluationId, 
        Long adminEvaluationId
    );
}