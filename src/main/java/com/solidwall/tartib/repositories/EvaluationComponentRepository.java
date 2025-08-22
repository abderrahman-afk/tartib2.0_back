package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.EvaluationComponentEntity;

@Repository
public interface EvaluationComponentRepository extends JpaRepository<EvaluationComponentEntity, Long> {
    List<EvaluationComponentEntity> findByEvaluationGridId(Long gridId);
}