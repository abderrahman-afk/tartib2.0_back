package com.solidwall.tartib.repositories;

import com.solidwall.tartib.entities.PonderationEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PonderationRepository extends JpaRepository<PonderationEntity, Long> {
    Optional<PonderationEntity> findByEvaluationGridId(Long evaluationGridId);
    
    Optional<PonderationEntity> findByIdAndEvaluationGridId(Long id, Long evaluationGridId);
    
    boolean existsByEvaluationGridId(Long evaluationGridId);

}
