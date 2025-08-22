package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.CategoryRiskEntity;

@Repository
public interface CategoryRiskRepository extends JpaRepository<CategoryRiskEntity, Long> {

    Optional<CategoryRiskEntity> findByName(String name);
}
