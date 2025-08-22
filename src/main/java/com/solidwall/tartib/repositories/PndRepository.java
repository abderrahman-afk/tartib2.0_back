package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.PndEntity;

@Repository
public interface PndRepository extends JpaRepository<PndEntity, Long> {

    Optional<PndEntity> findByName(String name);

}
