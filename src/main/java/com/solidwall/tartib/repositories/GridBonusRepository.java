package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.GridBonusEntity;

@Repository
public interface GridBonusRepository extends JpaRepository<GridBonusEntity, Long> {
    List<GridBonusEntity> findByClassificationId(Long classificationId);
    Optional<GridBonusEntity> findByCodeAndClassificationId(String code, Long classificationId);
}