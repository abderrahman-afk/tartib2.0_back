package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.StrategyEntity;

@Repository
public interface StrategyRepository extends JpaRepository<StrategyEntity, Long> {

    Optional<StrategyEntity> findByName(String name);

}
