package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ClassificationEntity;
import com.solidwall.tartib.entities.GeneratedClassificationEntity;

@Repository
public interface GeneratedClassificationRepository extends JpaRepository<GeneratedClassificationEntity, Long> {
    
    List<GeneratedClassificationEntity> findByClassificationSystem(ClassificationEntity classificationSystem);
    
    @Query("SELECT gc FROM GeneratedClassificationEntity gc WHERE gc.name LIKE %:name%")
    List<GeneratedClassificationEntity> findByNameContaining(@Param("name") String name);
    
    Optional<GeneratedClassificationEntity> findByNameAndClassificationSystem(String name, ClassificationEntity classificationSystem);
        Optional<GeneratedClassificationEntity> findFirstByClassificationSystemOrderByGenerationDateDesc(ClassificationEntity classificationSystem);
        boolean existsByName(String name);
        Optional<GeneratedClassificationEntity> findTopByOrderByGenerationDateDesc();

}
