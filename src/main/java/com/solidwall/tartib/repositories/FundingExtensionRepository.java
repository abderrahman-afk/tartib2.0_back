package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.FundingExtensionEntity;
import java.util.*;


@Repository
public interface FundingExtensionRepository extends JpaRepository<FundingExtensionEntity, Long> {

  Optional<FundingExtensionEntity> findByName(String name);
  
}
