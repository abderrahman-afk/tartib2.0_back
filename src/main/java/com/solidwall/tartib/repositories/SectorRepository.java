package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.SectorEntity;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long> {

    Optional<SectorEntity> findByName(String name);
}
