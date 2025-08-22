package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.EconomicNatureEntity;

@Repository
public interface EconomicNatureRepository extends JpaRepository<EconomicNatureEntity, Long> {
    Optional<EconomicNatureEntity> findByName(String name);

}
