package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.FundingSourceTypeEntity;

@Repository
public interface FundingSourceTypeRepository extends JpaRepository<FundingSourceTypeEntity, Long> {

    Optional<FundingSourceTypeEntity> findByName(String name);
}
