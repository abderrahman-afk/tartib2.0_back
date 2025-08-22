package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.SecteurBonusEntity;
@Repository
public interface SecteurBonusRepository extends JpaRepository<SecteurBonusEntity, Long> {
    // Find all bonus configurations for a specific classification
    List<SecteurBonusEntity> findByClassificationId(Long classificationId);
    
    // Delete all bonus configurations for a specific classification
    @Modifying
    @Query("DELETE FROM SecteurBonusEntity sb WHERE sb.classification.id = :classificationId")
    void deleteByClassificationId(Long classificationId);
}