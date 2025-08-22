package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.GeographicBonusEntity;
@Repository
public interface GeographicBonusRepository extends JpaRepository<GeographicBonusEntity, Long> {
    // Find all geographic bonus configurations for a specific classification
    List<GeographicBonusEntity> findByClassificationId(Long classificationId);
    
    // Delete all geographic bonus configurations for a specific classification
    @Modifying
    @Query("DELETE FROM GeographicBonusEntity gb WHERE gb.classification.id = :classificationId")
    void deleteByClassificationId(Long classificationId);
}