package com.solidwall.tartib.repositories;

import com.solidwall.tartib.entities.EvaluationGridEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface EvaluationGridRepository extends JpaRepository<EvaluationGridEntity, Long> {
    Optional<EvaluationGridEntity> findByIsActive(boolean active); // Add this method
}