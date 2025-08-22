package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.FundingTrancheEntity;

@Repository
public interface FundingTrancheRepository extends JpaRepository<FundingTrancheEntity, Long> {

}
