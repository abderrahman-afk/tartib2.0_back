package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.EvaluationNormeEntity;

@Repository
public interface EvaluationNormeRepository extends JpaRepository<EvaluationNormeEntity, Long> {
    List<EvaluationNormeEntity> findByEvaluationIndicateurId(Long indicateurId);
}
