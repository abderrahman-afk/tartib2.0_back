package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.EvaluationIndicateurEntity;

@Repository
public interface EvaluationIndicateurRepository extends JpaRepository<EvaluationIndicateurEntity, Long> {
    List<EvaluationIndicateurEntity> findByEvaluationCritiriaId(Long critiriaId);
}