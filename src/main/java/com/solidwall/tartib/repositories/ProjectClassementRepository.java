package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.GeneratedClassificationEntity;
import com.solidwall.tartib.entities.ProjectClassementEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;

@Repository
public interface ProjectClassementRepository extends JpaRepository<ProjectClassementEntity, Long> {
    
    List<ProjectClassementEntity> findByGeneratedClassification(GeneratedClassificationEntity generatedClassification);
    
    List<ProjectClassementEntity> findByProjectIdentity(ProjectIdentityEntity projectIdentity);
    
    @Query("SELECT pc FROM ProjectClassementEntity pc WHERE pc.generatedClassification.id = :classificationId ORDER BY pc.rang ASC")
    List<ProjectClassementEntity> findByGeneratedClassificationOrderByRang(@Param("classificationId") Long classificationId);
    
 @Query("SELECT pc FROM ProjectClassementEntity pc WHERE pc.generatedClassification.id = :classificationId ORDER BY pc.scoreBonifie DESC")
    List<ProjectClassementEntity> findByGeneratedClassificationOrderByScoreBonifieDesc(@Param("classificationId") Long classificationId);
}