package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.MinisterEntity;

@Repository
public interface MinisterRepository extends JpaRepository<MinisterEntity, Long> {

    Optional<MinisterEntity> findByName(String name);

}
