package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.EvaluationCritiriaEntity;

@Repository
public interface EvaluationCritiriaRepository extends JpaRepository<EvaluationCritiriaEntity, Long> {
    List<EvaluationCritiriaEntity> findByEvaluationComponentId(Long componentId);
}