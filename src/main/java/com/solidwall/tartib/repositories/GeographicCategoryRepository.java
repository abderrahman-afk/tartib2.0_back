package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.GeographicCategoryEntity;
 @Repository
public interface GeographicCategoryRepository extends JpaRepository<GeographicCategoryEntity, Long> {
 

}
