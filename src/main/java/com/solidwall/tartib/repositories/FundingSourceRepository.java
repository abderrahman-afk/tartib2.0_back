package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.FundingSourceEntity;

@Repository
public interface FundingSourceRepository extends JpaRepository<FundingSourceEntity, Long> {

    Optional<FundingSourceEntity> findByName(String name);
}
