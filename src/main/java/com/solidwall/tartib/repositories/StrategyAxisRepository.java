package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.StrategyAxisEntity;

@Repository
public interface StrategyAxisRepository extends JpaRepository<StrategyAxisEntity, Long> {

    Optional<StrategyAxisEntity> findByName(String name);

}
