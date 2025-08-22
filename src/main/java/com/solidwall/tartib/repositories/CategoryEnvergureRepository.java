package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.CategoryEnvergureEntity;

@Repository
public interface CategoryEnvergureRepository extends JpaRepository<CategoryEnvergureEntity, Long> {

    Optional<CategoryEnvergureEntity> findByName(String name);
}
