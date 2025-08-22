package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.TypologyEntity;

@Repository
public interface TypologyRepository extends JpaRepository<TypologyEntity, Long> {

    Optional<TypologyEntity> findByName(String name);
}
