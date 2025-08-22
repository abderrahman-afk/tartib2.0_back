package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ClassificationEntity;
@Repository
public interface ClassificationRepository extends JpaRepository<ClassificationEntity, Long>{
    Optional<ClassificationEntity> findByActive(Boolean active);
    
    // Check if a code is already in use
    boolean existsByCodeAndIdNot(String code, Long id);
    
    // Find classifications using a specific nomenclature
    List<ClassificationEntity> findByNomenclatureSecteurId(Long nomenclatureSecteurId);
    List<ClassificationEntity> findByNomenclatureGeographicId(Long nomenclatureGeographicId);
}
